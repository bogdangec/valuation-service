package co.quest.xms.valuation.infrastructure.webhook;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import co.quest.xms.valuation.application.webhook.WebhookService;
import co.quest.xms.valuation.domain.exception.StripeProcessingException;
import co.quest.xms.valuation.domain.exception.StripeSignatureException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeWebhookService implements WebhookService {

    @Value("${stripe.webhook-secret}")
    private String stripeWebhookSecret;

    private final Map<String, StripeEventHandler> eventHandlers;

    @Override
    public void processEvent(String payload, String signature) throws StripeSignatureException, StripeProcessingException {
        try {
            Event event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);

            StripeEventHandler handler = eventHandlers.get(event.getType());
            if (handler != null) {
                handler.handle(event);
            } else {
                log.info("Unhandled event type: {}", event.getType());
            }
        } catch (SignatureVerificationException e) {
            throw new StripeSignatureException("Invalid webhook signature", e);
        } catch (Exception e) {
            throw new StripeProcessingException("Failed to process Stripe event", e);
        }
    }
}
