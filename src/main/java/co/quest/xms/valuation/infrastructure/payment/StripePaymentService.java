package co.quest.xms.valuation.infrastructure.payment;

import co.quest.xms.valuation.application.payment.PaymentService;
import co.quest.xms.valuation.domain.exception.PaymentProcessingException;
import co.quest.xms.valuation.domain.model.PaymentIntentDetails;
import co.quest.xms.valuation.domain.model.SubscriptionDetails;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripePaymentService implements PaymentService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    public StripePaymentService() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentIntentDetails createSubscription(String customerId, String planId) {
        try {
            SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                    .setCustomer(customerId)
                    .addItem(SubscriptionCreateParams.Item.builder().setPrice(planId).build())
                    .addExpand("latest_invoice.payment_intent")
                    .build();

            Subscription subscription = Subscription.create(params);

            PaymentIntent paymentIntent = subscription.getLatestInvoiceObject().getPaymentIntentObject();

            return PaymentIntentDetails.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .status(paymentIntent.getStatus())
                    .clientSecret(paymentIntent.getClientSecret())
                    .build();
        } catch (StripeException e) {
            log.error("Failed to create subscription for customer {} and plan {}", customerId, planId, e);
            throw new PaymentProcessingException("Failed to create subscription", e);
        }
    }

    @Override
    public SubscriptionDetails cancelSubscription(String subscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            Subscription canceledSubscription = subscription.cancel();

            return SubscriptionDetails.builder()
                    .subscriptionId(canceledSubscription.getId())
                    .status(canceledSubscription.getStatus())
                    .planId(canceledSubscription.getItems().getData().getFirst().getPrice().getId())
                    .build();
        } catch (StripeException e) {
            log.error("Failed to cancel subscription with ID {}", subscriptionId, e);
            throw new PaymentProcessingException("Failed to cancel subscription", e);
        }
    }
}
