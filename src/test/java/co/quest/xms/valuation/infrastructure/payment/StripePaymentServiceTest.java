package co.quest.xms.valuation.infrastructure.payment;

import co.quest.xms.valuation.domain.exception.PaymentProcessingException;
import co.quest.xms.valuation.domain.model.PaymentIntentDetails;
import co.quest.xms.valuation.domain.model.SubscriptionDetails;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.SubscriptionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StripePaymentServiceTest {

    private StripePaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new StripePaymentService();
    }

    @Test
    void testCreateSubscription_Success() throws StripeException {
        // Create mock objects
        Subscription mockSubscription = mock(Subscription.class);
        Invoice mockInvoice = mock(Invoice.class);
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);

        // Mock nested method calls
        when(mockSubscription.getLatestInvoiceObject()).thenReturn(mockInvoice);
        when(mockInvoice.getPaymentIntentObject()).thenReturn(mockPaymentIntent);
        when(mockPaymentIntent.getId()).thenReturn("pi_test_123");
        when(mockPaymentIntent.getStatus()).thenReturn("succeeded");
        when(mockPaymentIntent.getClientSecret()).thenReturn("test_client_secret");

        // Mock Subscription.create
        try (MockedStatic<Subscription> mockedStatic = Mockito.mockStatic(Subscription.class)) {
            mockedStatic.when(() -> Subscription.create(any(SubscriptionCreateParams.class)))
                    .thenReturn(mockSubscription);

            // Execute test method
            PaymentIntentDetails result = paymentService.createSubscription("test_customer", "test_plan");

            // Assertions
            assertNotNull(result);
            assertEquals("pi_test_123", result.getPaymentIntentId());
            assertEquals("succeeded", result.getStatus());
            assertEquals("test_client_secret", result.getClientSecret());
        }
    }

    @Test
    void testCancelSubscription_Success() throws StripeException {
        // Create mock objects
        Subscription mockSubscription = mock(Subscription.class);
        SubscriptionItem mockSubscriptionItem = mock(SubscriptionItem.class);
        Price mockPrice = mock(Price.class);
        SubscriptionItemCollection mockSubscriptionItemCollection = mock(SubscriptionItemCollection.class);

        // Mock nested method calls
        when(mockSubscription.getId()).thenReturn("sub_test_123");
        when(mockSubscription.getStatus()).thenReturn("canceled");
        when(mockSubscription.getItems()).thenReturn(mockSubscriptionItemCollection);
        when(mockSubscription.getItems().getData()).thenReturn(List.of(mockSubscriptionItem));
        when(mockSubscription.cancel()).thenReturn(mockSubscription);
        when(mockSubscriptionItem.getPrice()).thenReturn(mockPrice);
        when(mockPrice.getId()).thenReturn("test_plan");


        // Mock Subscription.retrieve
        try (MockedStatic<Subscription> mockedStatic = Mockito.mockStatic(Subscription.class)) {
            mockedStatic.when(() -> Subscription.retrieve("sub_test_123")).thenReturn(mockSubscription);

            // Execute test method
            SubscriptionDetails result = paymentService.cancelSubscription("sub_test_123");

            // Assertions
            assertNotNull(result);
            assertEquals("sub_test_123", result.getSubscriptionId());
            assertEquals("canceled", result.getStatus());
            assertEquals("test_plan", result.getPlanId());
        }
    }

    @Test
    void testCreateSubscription_Failure() throws StripeException {
        // Mock Subscription.create to throw a StripeException
        try (MockedStatic<Subscription> mockedStatic = Mockito.mockStatic(Subscription.class)) {
            mockedStatic.when(() -> Subscription.create(any(SubscriptionCreateParams.class)))
                    .thenThrow(new ApiException("API error", null, null, 500, new Throwable()));

            // Execute test method and assert exception
            assertThrows(PaymentProcessingException.class, () -> paymentService.createSubscription("test_customer", "test_plan"));
        }
    }

    @Test
    void testCancelSubscription_Failure() throws StripeException {
        // Mock Subscription.retrieve to throw a StripeException
        try (MockedStatic<Subscription> mockedStatic = Mockito.mockStatic(Subscription.class)) {
            mockedStatic.when(() -> Subscription.retrieve("invalid_subscription_id"))
                    .thenThrow(new ApiException("Subscription not found", null, null, 404, new Throwable()));

            // Execute test method and assert exception
            assertThrows(PaymentProcessingException.class, () -> paymentService.cancelSubscription("invalid_subscription_id"));
        }
    }
}
