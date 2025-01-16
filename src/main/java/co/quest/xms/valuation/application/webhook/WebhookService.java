package co.quest.xms.valuation.application.webhook;

import co.quest.xms.valuation.domain.exception.StripeProcessingException;
import co.quest.xms.valuation.domain.exception.StripeSignatureException;

/**
 * Port interface for processing webhook events from Stripe.
 */
public interface WebhookService {

    /**
     * Process a webhook event payload.
     *
     * @param payload   The raw JSON payload received from Stripe.
     * @param signature The Stripe signature header to verify authenticity.
     * @throws StripeSignatureException If the signature verification fails.
     * @throws StripeProcessingException If processing the event fails.
     */
    void processEvent(String payload, String signature) throws StripeSignatureException, StripeProcessingException;
}
