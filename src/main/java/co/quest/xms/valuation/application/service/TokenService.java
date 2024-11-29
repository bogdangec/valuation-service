package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.model.User;
import io.jsonwebtoken.Claims;

import java.util.Map;


/**
 * TokenService defines the contract for generating, validating,
 * and managing JSON Web Tokens (JWT) within the application.
 */
public interface TokenService {

    String generateToken(User user);

    /**
     * Generates a JWT for a user based on their details.
     *
     * @param userDetails A map of user details (e.g., ID, email, role).
     * @return The generated JWT as a string.
     */
    String generateTokenForUser(Map<String, Object> userDetails);

    /**
     * Validates a JWT and extracts its claims.
     *
     * @param token The JWT to validate.
     * @return The extracted claims from the token.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired.
     */
    Claims validateToken(String token);

    /**
     * Extracts the subject from a JWT.
     *
     * @param token The JWT from which to extract the subject.
     * @return The subject of the token.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired.
     */
    String extractSubject(String token);
}

