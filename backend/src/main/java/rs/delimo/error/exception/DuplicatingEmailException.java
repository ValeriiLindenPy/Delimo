package rs.delimo.error.exception;

public class DuplicatingEmailException extends RuntimeException {
    public DuplicatingEmailException(String message) {
        super(message);
    }
}
