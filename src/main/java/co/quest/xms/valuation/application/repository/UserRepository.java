package co.quest.xms.valuation.application.repository;

import co.quest.xms.valuation.domain.model.User;

import java.util.Optional;

/**
 * Port interface for managing user data in the persistence layer.
 * This interface defines the contract for operations related to user storage and retrieval.
 * Implementations of this interface handle the interaction with the database or storage system.
 */
public interface UserRepository {

    /**
     * Retrieves a user by their username.
     *
     * @param username The unique username of the user.
     * @return An {@link Optional} containing the {@link User} if found, or an empty {@link Optional} if no user exists with the given username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email The unique email address of the user.
     * @return An {@link Optional} containing the {@link User} if found, or an empty {@link Optional} if no user exists with the given email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Saves or updates a user in the repository.
     * <p>
     * This method can be used to create a new user or update the details of an existing user.
     *
     * @param user The {@link User} object to be saved or updated.
     */
    void save(User user);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId The unique identifier of the user.
     * @return An {@link Optional} containing the {@link User} if found, or an empty {@link Optional} if no user exists with the given ID.
     */
    Optional<User> findById(String userId);
}


