package co.quest.xms.valuation.domain.exception;

public class StripeProcessingException extends RuntimeException {
    public StripeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
