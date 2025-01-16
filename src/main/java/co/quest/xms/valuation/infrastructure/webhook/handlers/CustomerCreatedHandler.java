package co.quest.xms.valuation.infrastructure.webhook.handlers;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles the "customer.created" event.
 */
@Component
@Slf4j
public class CustomerCreatedHandler implements StripeEventHandler {

    @Override
    public void handle(Event event) {
        Customer customer = (Customer) event.getDataObjectDeserializer().getObject().orElse(null);
        if (customer != null) {
            log.info("New customer created: {}", customer.getId());
            // TODO Add business logic for new customer creation
        }
    }
}
