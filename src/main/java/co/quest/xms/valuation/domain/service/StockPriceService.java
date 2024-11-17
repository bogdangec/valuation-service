package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.domain.model.StockPrice;

import java.util.List;

/**
 * Service interface for handling stock price-related business logic.
 */
public interface StockPriceService {

    /**
     * Retrieves historical stock prices for a given symbol.
     *
     * @param symbol The stock symbol.
     * @return A list of historical StockPrice objects.
     */
    List<StockPrice> getHistoricalPrices(String symbol);

    /**
     * Retrieves the real-time stock quote for a given symbol.
     *
     * @param symbol The stock symbol.
     * @return A StockPrice object with real-time data.
     */
    StockPrice getRealtimeQuote(String symbol);
}
