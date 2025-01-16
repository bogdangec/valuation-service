package co.quest.xms.valuation.application.webhook;

import com.stripe.model.Event;

/**
 * Interface for handling specific Stripe events.
 */
public interface StripeEventHandler {
    /**
     * Handles the given Stripe event.
     *
     * @param event The Stripe event to handle.
     */
    void handle(Event event);
}
