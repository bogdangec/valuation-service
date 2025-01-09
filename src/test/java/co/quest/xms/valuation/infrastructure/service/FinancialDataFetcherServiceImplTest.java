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

        AlphaVantageIncomeStatementResponse incomeStatement = new AlphaVantageIncomeStatementResponse();
        incomeStatement.setEbit(500.0);

        AlphaVantageBalanceSheetResponse balanceSheet = new AlphaVantageBalanceSheetResponse();
        balanceSheet.setCurrentAssets(200.0);
        balanceSheet.setCurrentLiabilities(100.0);
        balanceSheet.setNetFixedAssets(150.0);
        balanceSheet.setCash(50.0);
        balanceSheet.setTotalDebt(300.0);

        AlphaVantageOverviewResponse overview = new AlphaVantageOverviewResponse();
        overview.setMarketCapitalization(2000.0);

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
