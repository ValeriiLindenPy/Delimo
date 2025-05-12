package rs.delimo.common.exception;

public class ConfirmationException extends RuntimeException {
    public ConfirmationException(String pleaseConfirmEmail) {
        super(pleaseConfirmEmail);
    }
}
