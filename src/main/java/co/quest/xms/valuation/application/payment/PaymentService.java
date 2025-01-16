package co.quest.xms.valuation.application.payment;

import co.quest.xms.valuation.domain.exception.PaymentProcessingException;
import co.quest.xms.valuation.domain.model.PaymentIntentDetails;
import co.quest.xms.valuation.domain.model.SubscriptionDetails;

/**
 * Port interface for managing payments and subscriptions.
 */
public interface PaymentService {

    /**
     * Create a subscription for a customer.
     *
     * @param customerId The ID of the customer.
     * @param planId     The ID of the subscription plan.
     * @return PaymentIntentDetails containing the payment status and related information.
     * @throws PaymentProcessingException if any error occurs during subscription creation.
     */
    PaymentIntentDetails createSubscription(String customerId, String planId);

    /**
     * Cancel a subscription for a customer.
     *
     * @param subscriptionId The ID of the subscription.
     * @return SubscriptionDetails containing the updated subscription status.
     * @throws PaymentProcessingException if any error occurs during subscription cancellation.
     */
    SubscriptionDetails cancelSubscription(String subscriptionId);
}
