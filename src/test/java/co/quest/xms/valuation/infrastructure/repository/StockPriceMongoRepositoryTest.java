package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.domain.model.StockPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static co.quest.xms.valuation.util.TestConstants.*;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@ActiveProfiles("test")
@Import(StockPriceMongoRepository.class)
class StockPriceMongoRepositoryTest {

    @Autowired
    private StockPriceMongoRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("stock_prices");
        repository.saveAll(MOCK_AAPL_PRICES);
    }

    @Test
    void testFindBySymbol() {
        // Act
        List<StockPrice> results = repository.findBySymbol(AAPL_SYMBOL);

        // Assert
        assertEquals(2, results.size());
        assertEquals(AAPL_SYMBOL, results.getFirst().getSymbol());
    }

    @Test
    void testFindBySymbolAndTimestamp() {
        StockPrice firstEntry = MOCK_AAPL_PRICES.getFirst();
        // Act
        Optional<StockPrice> result = repository.findBySymbolAndTimestamp(AAPL_SYMBOL, firstEntry.getTimestamp().toString());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(firstEntry.getSymbol(), result.get().getSymbol());
        assertEquals(firstEntry.getTimestamp().truncatedTo(MILLIS), result.get().getTimestamp().truncatedTo(MILLIS));
    }

    @Test
    void testSaveAndRetrieve() {
        // Act
        repository.save(MOCK_GOOGLE_PRICE);
        List<StockPrice> results = repository.findBySymbol(GOOGL_SYMBOL);

        // Assert
        assertEquals(1, results.size());
        assertEquals(GOOGL_SYMBOL, results.getFirst().getSymbol());
    }
}
