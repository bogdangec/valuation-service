package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.model.FinancialMetrics;

public interface FinancialDataFetcherService {

    /**
     * Fetch financial metrics for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch data for.
     * @return FinancialMetrics containing the aggregated data.
     */
    FinancialMetrics fetchFinancialMetrics(String symbol);
}
