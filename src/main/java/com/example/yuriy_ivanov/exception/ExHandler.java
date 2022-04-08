package com.example.yuriy_ivanov.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<ErrorMessage> handleConflict(ServiceException e) {
        log.error(e.getMessage());
        TypicalError typicalError = e.getTypicalError();
        ErrorMessage errorMessage = new ErrorMessage(typicalError.name(), e.getMessage());
        return new ResponseEntity<>(errorMessage, typicalError.getHttpStatus());
    }

    // FIXED
    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Object> handelApiException(Throwable throwable) {
        HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        Api api = new Api(throwable.getMessage(), throwable, serverError);
        return new ResponseEntity<>(api, serverError);
    }
}
