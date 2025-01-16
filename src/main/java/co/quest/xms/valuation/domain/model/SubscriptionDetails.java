package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents subscription details for a customer.
 */
@Data
@Builder
public class SubscriptionDetails {
    private String subscriptionId;
    private String status;
    private String planId;
}
