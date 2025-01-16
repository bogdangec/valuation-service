package co.quest.xms.valuation.infrastructure.webhook.handlers;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles the "payment_intent.succeeded" event.
 */
@Component
@Slf4j
public class PaymentIntentSucceededHandler implements StripeEventHandler {

    @Override
    public void handle(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (paymentIntent != null) {
            log.info("Payment intent succeeded: {}", paymentIntent.getId());
            // TODO Add business logic for successful payment intent
        }
    }
}
