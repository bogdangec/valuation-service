package co.quest.xms.valuation.application.repository;

import co.quest.xms.valuation.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    void save(User user);
}

