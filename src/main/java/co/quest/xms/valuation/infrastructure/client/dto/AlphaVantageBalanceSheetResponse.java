package co.quest.xms.valuation.infrastructure.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlphaVantageBalanceSheetResponse {
    private String symbol;
    private Double totalCurrentAssets;
    private Double totalCurrentLiabilities;
    private Double propertyPlantEquipment;

    private Double shortTermDebt;
    private Double longTermDebt;
    private Double cashAndCashEquivalentsAtCarryingValue;
}
