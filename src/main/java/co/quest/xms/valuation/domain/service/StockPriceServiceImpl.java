package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.client.AlphaVantageClient;
import co.quest.xms.valuation.application.publisher.StockPricePublisher;
import co.quest.xms.valuation.application.repository.StockPriceRepository;
import co.quest.xms.valuation.domain.model.StockPrice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the StockPriceService interface.
 * This service handles fetching stock prices, saving them to MongoDB, and publishing to Kafka.
 */
@Service
public class StockPriceServiceImpl implements StockPriceService {
    private final AlphaVantageClient alphaVantageClient;
    private final StockPriceRepository stockPriceRepository;
    private final StockPricePublisher stockPricePublisher;

    public StockPriceServiceImpl(AlphaVantageClient client, StockPriceRepository repository, StockPricePublisher publisher) {
        this.alphaVantageClient = client;
        this.stockPriceRepository = repository;
        this.stockPricePublisher = publisher;
    }

    @Override
    @Transactional
    public List<StockPrice> getHistoricalPrices(String symbol) {
        List<StockPrice> historicalPrices = alphaVantageClient.fetchStockPrices(symbol);
        stockPriceRepository.saveAll(historicalPrices);
        historicalPrices.forEach(stockPricePublisher::publish);
        return historicalPrices;
    }

    @Override
    @Transactional
    public StockPrice getRealtimeQuote(String symbol) {
        StockPrice realtimeQuote = alphaVantageClient.fetchRealtimeQuote(symbol);
        stockPriceRepository.save(realtimeQuote);
        stockPricePublisher.publish(realtimeQuote);
        return realtimeQuote;
    }
}
