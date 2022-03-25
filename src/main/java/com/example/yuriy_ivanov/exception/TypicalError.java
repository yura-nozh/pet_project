package com.example.yuriy_ivanov.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TypicalError {

    NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_WITH_THIS_EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST),
    NOT_SO_MANY_PRODUCTS(HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;

    TypicalError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
