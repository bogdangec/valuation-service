package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.exception.UserNotFoundException;
import co.quest.xms.valuation.domain.model.User;


/**
 * Interface defining the operations for user management and authentication.
 * This adheres to the principles of Hexagonal Architecture by serving as a
 * contract for the core business logic related to users.
 */
public interface UserService {

    /**
     * Registers a new user with the specified username, email, and password.
     *
     * @param username    the username of the new user
     * @param email       the email of the new user
     * @param rawPassword the plaintext password of the new user
     * @return the registered User object
     * @throws IllegalArgumentException if the email is already in use
     */
    User register(String username, String email, String rawPassword);

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username    the username of the user
     * @param rawPassword the plaintext password of the user
     * @return the authenticated User object
     * @throws IllegalArgumentException if the username or password is incorrect
     */
    User authenticate(String username, String rawPassword);

    /**
     * Changes the password of an existing user.
     *
     * @param userId          the ID of the user whose password is being changed
     * @param currentPassword the user's current plaintext password
     * @param newPassword     the user's new plaintext password
     * @throws IllegalArgumentException if the current password is incorrect
     * @throws UserNotFoundException    if no user is found with the given ID
     */
    void changePassword(String userId, String currentPassword, String newPassword);

    /**
     * Updates the username for a given user.
     *
     * @param userId      the unique identifier of the user.
     * @param newUsername the new username to be updated.
     * @return the updated User object, or an empty Optional if the update fails.
     * @throws IllegalArgumentException if the new username is already in use.
     * @throws IllegalStateException    if the user cannot be found.
     */
    User updateUsername(String userId, String newUsername);
}

