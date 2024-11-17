package co.quest.xms.valuation.application.repository;

import co.quest.xms.valuation.domain.model.StockPrice;

import java.util.List;
import java.util.Optional;

/**
 * Interface representing the Stock Price Repository.
 * <p>
 * This interface defines the contract for storing and retrieving stock price data from the database.
 * It abstracts the implementation details of the persistence mechanism and serves as a port in the hexagonal architecture.
 */
public interface StockPriceRepository {

    /**
     * Saves a stock price entry to the repository.
     *
     * @param stockPrice The {@link StockPrice} object to be saved.
     * @return The saved {@link StockPrice} object.
     */
    StockPrice save(StockPrice stockPrice);

    /**
     * Saves a {@link List} of stock prices entries to the repository.
     *
     * @param stockPrices The {@link List<StockPrice>} object to be saved.
     */
    void saveAll(List<StockPrice> stockPrices);

    /**
     * Retrieves the stock price data for a given symbol.
     *
     * @param symbol The stock symbol to look up.
     * @return A list of {@link StockPrice} objects associated with the given symbol.
     */
    List<StockPrice> findBySymbol(String symbol);

    /**
     * Retrieves a stock price entry by its timestamp and symbol.
     *
     * @param symbol    The stock symbol.
     * @param timestamp The timestamp to search for.
     * @return An {@link java.util.Optional} containing the {@link StockPrice} if found, or empty otherwise.
     */
    Optional<StockPrice> findBySymbolAndTimestamp(String symbol, String timestamp);
}
