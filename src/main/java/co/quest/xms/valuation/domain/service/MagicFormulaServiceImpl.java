package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.api.dto.MagicFormulaResult;
import co.quest.xms.valuation.application.service.MagicFormulaService;
import co.quest.xms.valuation.application.service.MetricsService;
import co.quest.xms.valuation.application.service.FinancialDataFetcherService;
import co.quest.xms.valuation.domain.model.FinancialMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MagicFormulaServiceImpl implements MagicFormulaService {

    private final FinancialDataFetcherService financialDataFetcherService;
    private final MetricsService metricsService;

    @Override
    public MagicFormulaResult calculateMagicFormula(String symbol) {
        FinancialMetrics metrics = financialDataFetcherService.fetchFinancialMetrics(symbol);

        double returnOnCapital = metricsService.calculateReturnOnCapital(metrics);
        double earningsYield = metricsService.calculateEarningsYield(metrics);

        return new MagicFormulaResult(symbol, returnOnCapital, earningsYield);
    }
}
