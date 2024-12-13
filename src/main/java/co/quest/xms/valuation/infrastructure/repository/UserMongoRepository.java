package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.UserRepository;
import co.quest.xms.valuation.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserMongoRepository implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<User> findByUsername(String username) {
        return ofNullable(mongoTemplate.findOne(query(where("email").is(username)), User.class));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return ofNullable(mongoTemplate.findOne(query(where("email").is(email)), User.class));
    }

    @Override
    public void save(User user) {
        mongoTemplate.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return ofNullable(mongoTemplate.findOne(query(where("id").is(id)), User.class));
    }
}

