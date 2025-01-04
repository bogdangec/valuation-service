package co.quest.xms.valuation.infrastructure.service;

import co.quest.xms.valuation.application.client.AlphaVantageClient;
import co.quest.xms.valuation.application.service.FinancialDataFetcherService;
import co.quest.xms.valuation.domain.model.FinancialMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofHours;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialDataFetcherServiceImpl implements FinancialDataFetcherService {

    private static final Duration CACHE_EXPIRY = ofHours(1);

    private final AlphaVantageClient alphaVantageClient;

    // In-memory cache
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public FinancialMetrics fetchFinancialMetrics(String symbol) {
        log.info("Fetching financial metrics for symbol: {}", symbol);

        // Check cache
        CacheEntry cachedEntry = cache.get(symbol);
        if (cachedEntry != null && !cachedEntry.isExpired()) {
            log.info("Cache hit for symbol: {}", symbol);
            return cachedEntry.metrics();
        }

        // Fetch data from Alpha Vantage endpoints
        var incomeStatement = alphaVantageClient.getIncomeStatement(symbol);
        var balanceSheet = alphaVantageClient.getBalanceSheet(symbol);
        var overview = alphaVantageClient.getOverview(symbol);

        // Aggregate data into a FinancialMetrics object
        FinancialMetrics metrics = FinancialMetrics.builder()
                .ebit(incomeStatement.getEbit())
                .marketCapitalization(overview.getMarketCapitalization())
                .totalDebt(balanceSheet.getTotalDebt())
                .netFixedAssets(balanceSheet.getNetFixedAssets())
                .netWorkingCapital(balanceSheet.getCurrentAssets() - balanceSheet.getCurrentLiabilities())
                .cash(balanceSheet.getCash())
                .build();

        // Cache the result from Alpha Vantage
        log.info("Add cache for symbol: {}", symbol);
        cache.put(symbol, new CacheEntry(metrics, currentTimeMillis() + CACHE_EXPIRY.toMillis()));
        return metrics;
    }

    private record CacheEntry(FinancialMetrics metrics, long expiryTime) {

        public boolean isExpired() {
            return currentTimeMillis() > expiryTime;
        }
    }
}
