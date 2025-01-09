package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialMetrics {
    private Double ebit; // Earnings Before Interest and Taxes
    private Double netFixedAssets;
    private Double netWorkingCapital;

    private Double marketCapitalization;
    private Double totalDebt;
    private Double cash;
}
