package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.dto.ContactRequest;
import co.quest.xms.valuation.application.service.ContactService;
import co.quest.xms.valuation.domain.model.ContactMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.quest.xms.valuation.api.mapper.ContactMapper.toModel;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> handleContact(@RequestBody @Valid ContactRequest contactRequest) {
        ContactMessage contactMessage = contactService.saveContactMessage(toModel(contactRequest));
        return ResponseEntity.ok("Thanks for your message, " + contactMessage.getName());
    }
}

