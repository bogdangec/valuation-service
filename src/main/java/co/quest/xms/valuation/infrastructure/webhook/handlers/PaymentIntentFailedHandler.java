package co.quest.xms.valuation.infrastructure.webhook.handlers;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles the "payment_intent.payment_failed" event.
 */
@Component
@Slf4j
public class PaymentIntentFailedHandler implements StripeEventHandler {

    @Override
    public void handle(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent != null) {
            log.warn("Payment intent failed: {}", paymentIntent.getId());
            // TODO Add business logic for failed payment intent
        }
    }
}
