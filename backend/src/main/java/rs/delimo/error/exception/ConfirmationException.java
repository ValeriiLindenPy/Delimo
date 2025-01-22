package rs.delimo.error.exception;

public class ConfirmationException extends RuntimeException {
    public ConfirmationException(String pleaseConfirmEmail) {
        super(pleaseConfirmEmail);
    }
}
