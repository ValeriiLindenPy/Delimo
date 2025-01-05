package com.dajdam.daj_dam.error;

import com.dajdam.daj_dam.error.exception.DublicatingEmailException;
import com.dajdam.daj_dam.error.exception.NotFoundException;
import com.dajdam.daj_dam.error.exception.OwnerException;
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
