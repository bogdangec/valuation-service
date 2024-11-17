package co.quest.xms.valuation.application.publisher;

import co.quest.xms.valuation.domain.model.StockPrice;

/**
 * Interface representing the Stock Price Publisher.
 * <p>
 * This interface defines the contract for publishing stock price data to Kafka or any other messaging system.
 * It abstracts the implementation details of the messaging mechanism and serves as a port in the hexagonal architecture.
 */
public interface StockPricePublisher {

    /**
     * Publishes a stock price event to the messaging system.
     *
     * @param stockPrice The {@link StockPrice} object containing the stock price data to be published.
     */
    void publish(StockPrice stockPrice);
}
