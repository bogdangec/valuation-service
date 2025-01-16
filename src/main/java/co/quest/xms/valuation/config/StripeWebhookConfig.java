package co.quest.xms.valuation.config;

import co.quest.xms.valuation.application.webhook.StripeEventHandler;
import co.quest.xms.valuation.infrastructure.webhook.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StripeWebhookConfig {

    @Bean
    public Map<String, StripeEventHandler> eventHandlers(
            PaymentSucceededHandler paymentSucceededHandler,
            PaymentFailedHandler paymentFailedHandler,
            SubscriptionDeletedHandler subscriptionDeletedHandler,
            SubscriptionUpdatedHandler subscriptionUpdatedHandler,
            PaymentIntentSucceededHandler paymentIntentSucceededHandler,
            PaymentIntentFailedHandler paymentIntentFailedHandler,
            CustomerCreatedHandler customerCreatedHandler,
            CustomerUpdatedHandler customerUpdatedHandler
    ) {
        return Map.of(
                "invoice.payment_succeeded", paymentSucceededHandler,
                "invoice.payment_failed", paymentFailedHandler,
                "customer.subscription.deleted", subscriptionDeletedHandler,
                "customer.subscription.updated", subscriptionUpdatedHandler,
                "payment_intent.succeeded", paymentIntentSucceededHandler,
                "payment_intent.payment_failed", paymentIntentFailedHandler,
                "customer.created", customerCreatedHandler,
                "customer.updated", customerUpdatedHandler
        );
    }
}

