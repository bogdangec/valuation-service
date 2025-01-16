package co.quest.xms.valuation.domain.exception;

public class StripeSignatureException extends RuntimeException {
    public StripeSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}

