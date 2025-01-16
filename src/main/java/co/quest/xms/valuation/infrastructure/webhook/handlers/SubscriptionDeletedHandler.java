package co.quest.xms.valuation.infrastructure.webhook.handlers;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles the "customer.subscription.deleted" event.
 */
@Component
@Slf4j
public class SubscriptionDeletedHandler implements StripeEventHandler {

    @Override
    public void handle(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (subscription != null) {
            log.info("Subscription canceled: {}", subscription.getId());
            // TODO Add business logic for subscription cancellation
        }
    }
}
