package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class ApiKeyMongoRepository implements ApiKeyRepository {

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

    @Override
    public List<ApiKey> findByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(where("user_id").is(userId));
        return mongoTemplate.find(query, ApiKey.class);
    }

    @Override
    public Optional<ApiKey> findByKeyAndUserId(String api_key, String userId) {
        Query query = new Query();
        query.addCriteria(where("api_key").is(api_key).and("user_id").is(userId));
        return ofNullable(mongoTemplate.findOne(query, ApiKey.class));
    }
}
