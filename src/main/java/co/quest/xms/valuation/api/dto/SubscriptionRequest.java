package co.quest.xms.valuation.api.dto;

import lombok.Data;

/**
 * DTO for subscription requests.
 */
@Data
public class SubscriptionRequest {
    private String customerId;
    private String planId;
}
