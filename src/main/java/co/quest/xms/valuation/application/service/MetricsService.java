package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.model.FinancialMetrics;

public interface MetricsService {
    /**
     * Calculate Earnings Yield for a given set of financial metrics.
     *
     * @param metrics FinancialMetrics object.
     * @return Earnings Yield as a double.
     */
    double calculateEarningsYield(FinancialMetrics metrics);

    /**
     * Calculate Return on Capital (ROC) for a given set of financial metrics.
     *
     * @param metrics FinancialMetrics object.
     * @return ROC as a double.
     */
    double calculateReturnOnCapital(FinancialMetrics metrics);
}
