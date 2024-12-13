package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.StockPriceRepository;
import co.quest.xms.valuation.domain.model.StockPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.parse;
import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class StockPriceMongoRepository implements StockPriceRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public StockPrice save(StockPrice stockPrice) {
        return mongoTemplate.save(stockPrice);
    }

    @Override
    public void saveAll(List<StockPrice> stockPrices) {
        mongoTemplate.insertAll(stockPrices);
    }

    @Override
    public List<StockPrice> findBySymbol(String symbol) {
        Query query = new Query();
        query.addCriteria(where("symbol").is(symbol));
        return mongoTemplate.find(query, StockPrice.class);
    }

    @Override
    public Optional<StockPrice> findBySymbolAndTimestamp(String symbol, String timestamp) {
        Query query = new Query();
        query.addCriteria(where("symbol").is(symbol).and("timestamp").is(parse(timestamp)));
        return ofNullable(mongoTemplate.findOne(query, StockPrice.class));
    }
}
