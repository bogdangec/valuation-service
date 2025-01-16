package co.quest.xms.valuation.infrastructure.service;

import co.quest.xms.valuation.application.client.AlphaVantageClient;
import co.quest.xms.valuation.domain.model.FinancialMetrics;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageBalanceSheetResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageIncomeStatementResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageOverviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinancialDataFetcherServiceImplTest {
    @Mock
    private AlphaVantageClient alphaVantageClient;
    @InjectMocks
    private FinancialDataFetcherServiceImpl financialDataFetcherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchFinancialMetrics_shouldReturnCachedMetricsIfAvailable() {
        // Arrange
        String symbol = "AAPL";

        AlphaVantageIncomeStatementResponse incomeStatement = AlphaVantageIncomeStatementResponse.builder().ebit(500.0).build();

        AlphaVantageBalanceSheetResponse balanceSheet = AlphaVantageBalanceSheetResponse.builder()
                .totalCurrentAssets(200.0)
                .totalCurrentLiabilities(100.0)
                .propertyPlantEquipment(150.0)
                .cashAndCashEquivalentsAtCarryingValue(50.0)
                .longTermDebt(300.0)
                .build();

        AlphaVantageOverviewResponse overview = AlphaVantageOverviewResponse.builder().marketCapitalization(2000.0).build();

        // First call (cache miss)
        when(alphaVantageClient.getIncomeStatement(symbol)).thenReturn(incomeStatement);
        when(alphaVantageClient.getBalanceSheet(symbol)).thenReturn(balanceSheet);
        when(alphaVantageClient.getOverview(symbol)).thenReturn(overview);

        // Act
        FinancialMetrics metrics1 = financialDataFetcherService.fetchFinancialMetrics(symbol);
        FinancialMetrics cachedMetrics = financialDataFetcherService.fetchFinancialMetrics(symbol); // Cached

        // Assert
        assertNotNull(metrics1);
        assertEquals(metrics1, cachedMetrics); // Metrics should be the same from cache
        verify(alphaVantageClient, times(1)).getIncomeStatement(symbol);
        verify(alphaVantageClient, times(1)).getBalanceSheet(symbol);
        verify(alphaVantageClient, times(1)).getOverview(symbol);
    }
}
