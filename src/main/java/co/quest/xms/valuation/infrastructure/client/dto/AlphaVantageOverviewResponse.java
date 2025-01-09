package co.quest.xms.valuation.infrastructure.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlphaVantageOverviewResponse {
    private String symbol;
    private Double marketCapitalization;
}
