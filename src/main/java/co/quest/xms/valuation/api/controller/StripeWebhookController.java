package co.quest.xms.valuation.api.controller;

import co.quest.xms.valuation.application.webhook.WebhookService;
import co.quest.xms.valuation.domain.exception.StripeSignatureException;
import co.quest.xms.valuation.domain.exception.StripeProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final WebhookService webhookService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String signature) {
        try {
            webhookService.processEvent(payload, signature);
            return ResponseEntity.ok("Event processed successfully");
        } catch (StripeSignatureException e) {
            log.error("Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe signature");
        } catch (StripeProcessingException e) {
            log.error("Error processing Stripe event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
    }
}
