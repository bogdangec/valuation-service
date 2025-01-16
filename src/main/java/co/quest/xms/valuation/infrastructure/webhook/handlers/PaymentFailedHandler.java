package co.quest.xms.valuation.infrastructure.webhook.handlers;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles the "invoice.payment_failed" event.
 */
@Component
@Slf4j
public class PaymentFailedHandler implements StripeEventHandler {

    @Override
    public void handle(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);
        if (invoice != null) {
            log.warn("Payment failed for invoice: {}", invoice.getId());
            // TODO Add business logic, e.g., notify user or mark subscription delinquent
        }
    }
}
