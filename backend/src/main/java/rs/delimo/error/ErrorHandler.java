package rs.delimo.error;

import rs.delimo.error.exception.DublicatingEmailException;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(OwnerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOwnerException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(DublicatingEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDublicatingEmailException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
