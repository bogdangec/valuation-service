package co.quest.xms.valuation.infrastructure.repository;

import co.quest.xms.valuation.application.repository.UserRepository;
import co.quest.xms.valuation.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoUserRepository implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query(Criteria.where("email").is(username)),
                User.class));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query(Criteria.where("email").is(email)),
                User.class));
    }

    @Override
    public void save(User user) {
        mongoTemplate.save(user);
    }
}

