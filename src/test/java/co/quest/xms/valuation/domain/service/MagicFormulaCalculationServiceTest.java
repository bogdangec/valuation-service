package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.api.dto.MagicFormulaResult;
import co.quest.xms.valuation.application.service.FinancialDataFetcherService;
import co.quest.xms.valuation.application.service.MagicFormulaService;
import co.quest.xms.valuation.application.service.MetricsService;
import co.quest.xms.valuation.domain.model.FinancialMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MagicFormulaCalculationServiceTest {

    private final FinancialDataFetcherService financialDataFetcherService = mock(FinancialDataFetcherService.class);
    private final MetricsService metricsCalculationService = mock(MetricsService.class);
    private final MagicFormulaService magicFormulaCalculationService =
            new MagicFormulaServiceImpl(financialDataFetcherService, metricsCalculationService);

    @Test
    void calculateMagicFormula_shouldReturnCorrectResult() {
        // Arrange
        String symbol = "AAPL";
        FinancialMetrics metrics = FinancialMetrics.builder().build();
        when(financialDataFetcherService.fetchFinancialMetrics(symbol)).thenReturn(metrics);
        when(metricsCalculationService.calculateEarningsYield(metrics)).thenReturn(0.1);
        when(metricsCalculationService.calculateReturnOnCapital(metrics)).thenReturn(0.2);

        // Act
        MagicFormulaResult result = magicFormulaCalculationService.calculateMagicFormula(symbol);

        // Assert
        assertNotNull(result);
        assertEquals(symbol, result.symbol());
        assertEquals(0.1, result.earningsYield());
        assertEquals(0.2, result.returnOnCapital());
    }
}
