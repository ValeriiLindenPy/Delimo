package rs.delimo.error.exception;

public class DublicatingEmailException extends RuntimeException {
    public DublicatingEmailException(String message) {
        super(message);
    }
}
