package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ContactRepository;
import co.quest.xms.valuation.application.service.ContactService;
import co.quest.xms.valuation.domain.model.ContactMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    /**
     * Saves a new contact message.
     *
     * @param contactMessage The contact message to be saved.
     * @return The saved contact message.
     */
    @Override
    public ContactMessage saveContactMessage(ContactMessage contactMessage) {
        return contactRepository.save(contactMessage);
    }
}
