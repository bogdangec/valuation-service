package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents payment intent details returned by the payment provider.
 */
@Data
@Builder
public class PaymentIntentDetails {
    private String paymentIntentId;
    private String status;
    private String clientSecret; // Used for client-side payment confirmation
}
