package co.quest.xms.valuation.application.repository;

import co.quest.xms.valuation.domain.model.ApiKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {
    Optional<ApiKey> findByKey(String key);
}
