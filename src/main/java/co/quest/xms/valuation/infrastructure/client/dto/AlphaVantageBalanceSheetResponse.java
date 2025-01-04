package co.quest.xms.valuation.infrastructure.client.dto;

import lombok.Data;

@Data
public class AlphaVantageBalanceSheetResponse {
    private Double currentAssets;
    private Double currentLiabilities;
    private Double netFixedAssets;
    private Double cash;
    private Double totalDebt;
}
