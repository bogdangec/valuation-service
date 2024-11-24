package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class MongoApiKeyRepository implements ApiKeyRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<ApiKey> findByKey(String key) {
        Query query = new Query();
        query.addCriteria(where("api_key").is(key));
        return ofNullable(mongoTemplate.findOne(query, ApiKey.class));
    }

    @Override
    public ApiKey save(ApiKey apiKey) {
        return mongoTemplate.save(apiKey);
    }
}
