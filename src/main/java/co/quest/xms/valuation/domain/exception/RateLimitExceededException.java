package co.quest.xms.valuation.domain.exception;

/**
 * Exception thrown when a rate limit is exceeded.
 */
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
