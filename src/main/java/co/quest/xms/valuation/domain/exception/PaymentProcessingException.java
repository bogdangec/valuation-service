package co.quest.xms.valuation.domain.exception;

/**
 * Exception for errors during payment processing.
 */
public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
