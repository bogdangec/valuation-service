package co.quest.xms.valuation.application.client;

import co.quest.xms.valuation.domain.model.StockPrice;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageBalanceSheetResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageIncomeStatementResponse;
import co.quest.xms.valuation.infrastructure.client.dto.AlphaVantageOverviewResponse;

import java.util.List;

/**
 * AlphaVantageClient is a port interface for fetching stock price data from the Alpha Vantage API.
 * <p>
 * This interface abstracts the external API interactions and serves as a contract within the hexagonal architecture,
 * allowing the application to request historical stock price data without being directly tied to the Alpha Vantage API implementation.
 * Implementations of this interface handle the necessary HTTP communication, response parsing,
 * and mapping of API data to domain-specific {@link StockPrice} objects.
 */
public interface AlphaVantageClient {

    /**
     * Fetches historical stock price data for the given stock symbol.
     * <p>
     *
     * @param symbol (e.g., "AAPL" for Apple Inc., "MSFT" for Microsoft Corporation).
     * @return A list of {@link StockPrice} objects with historical data, ordered by date.
     */
    List<StockPrice> fetchStockPrices(String symbol);

    /**
     * Fetches the real-time stock price for a given symbol.
     *
     * @param symbol The stock symbol.
     * @return A StockPrice object with the current real-time quote.
     */
    StockPrice fetchRealtimeQuote(String symbol);

    /**
     * Fetch the income statement data for a given stock symbol.
     *
     * @param symbol The stock symbol.
     * @return Income statement response.
     */
    AlphaVantageIncomeStatementResponse getIncomeStatement(String symbol);

    /**
     * Fetch the balance sheet data for a given stock symbol.
     *
     * @param symbol The stock symbol.
     * @return Balance sheet response.
     */
    AlphaVantageBalanceSheetResponse getBalanceSheet(String symbol);

    /**
     * Fetch the overview data for a given stock symbol.
     *
     * @param symbol The stock symbol.
     * @return Overview response.
     */
    AlphaVantageOverviewResponse getOverview(String symbol);
}
