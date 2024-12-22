package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.ContactRepository;
import co.quest.xms.valuation.domain.model.ContactMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContactMongoRepository implements ContactRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ContactMessage save(ContactMessage contactMessage) {
        return mongoTemplate.save(contactMessage);
    }
}
