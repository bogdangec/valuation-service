package co.quest.xms.valuation.infrastructure.client;

import co.quest.xms.valuation.application.client.AlphaVantageClient;
import co.quest.xms.valuation.domain.model.StockPrice;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageBalanceSheetResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageIncomeStatementResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageOverviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.lang.String.format;

/**
 * AlphaVantageClientImpl fetches stock prices using the Alpha Vantage API.
 */
@Slf4j
@Component
public class AlphaVantageClientImpl implements AlphaVantageClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final AlphaVantageResponseParser parser;

    public AlphaVantageClientImpl(RestTemplate restTemplate, @Value("${alpha-vantage.api-key}") String apiKey, AlphaVantageResponseParser parser) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.parser = parser;
    }

    @Override
    public List<StockPrice> fetchStockPrices(String symbol) {
        String url = format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        return parser.parseStockPrices(response, symbol);
    }

    @Override
    public StockPrice fetchRealtimeQuote(String symbol) {
        String url = format("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", symbol, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        return parser.parseRealtimeQuote(response, symbol);
    }

    @Override
    public AlphaVantageIncomeStatementResponse getIncomeStatement(String symbol) {
        String url = format("https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=%s&apikey=%s", symbol, apiKey);
        return restTemplate.getForObject(url, AlphaVantageIncomeStatementResponse.class);
    }

    @Override
    public AlphaVantageBalanceSheetResponse getBalanceSheet(String symbol) {
        String url = format("https://www.alphavantage.co/query?function=BALANCE_SHEET&symbol=%s&apikey=%s", symbol, apiKey);
        return restTemplate.getForObject(url, AlphaVantageBalanceSheetResponse.class);
    }

    @Override
    public AlphaVantageOverviewResponse getOverview(String symbol) {
        String url = format("https://www.alphavantage.co/query?function=OVERVIEW&symbol=%s&apikey=%s", symbol, apiKey);
        return restTemplate.getForObject(url, AlphaVantageOverviewResponse.class);
    }
}
