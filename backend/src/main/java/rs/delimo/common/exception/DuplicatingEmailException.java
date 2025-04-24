package rs.delimo.common.exception;

public class DuplicatingEmailException extends RuntimeException {
    public DuplicatingEmailException(String message) {
        super(message);
    }
}
