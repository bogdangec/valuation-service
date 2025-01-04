package co.quest.xms.valuation.infrastructure.service;

import co.quest.xms.valuation.application.service.TokenService;
import co.quest.xms.valuation.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtService provides an implementation of the TokenService interface for managing
 * JSON Web Tokens (JWT) using the JJWT library.
 */
@Component
public class JwtService implements TokenService {

    private final SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Constructs a new JwtService instance with the specified secret key.
     *
     * @param secret The secret key used for signing JWTs.
     */
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        return generateToken(claims, user.getUsername());
    }

    /**
     * Generate a JWT for the given user details and claims.
     *
     * @param claims  Additional claims to include in the token.
     * @param subject The subject of the token (typically the user ID or email).
     * @return A signed JWT as a String.
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generate a JWT for the given user.
     *
     * @param userDetails User details, such as user ID and roles.
     * @return A signed JWT as a String.
     */
    public String generateTokenForUser(Map<String, Object> userDetails) {
        return generateToken(userDetails, userDetails.get("id").toString());
    }

    /**
     * Validate the given JWT and return the claims if valid.
     *
     * @param token The JWT to validate.
     * @return The claims extracted from the token.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired.
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract the subject (e.g., user ID or email) from the token.
     *
     * @param token The JWT to parse.
     * @return The subject from the token.
     */
    public String extractSubject(String token) {
        return validateToken(token).getSubject();
    }
}
