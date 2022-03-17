package com.example.yuriy_ivanov.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final TypicalError typicalError;

    public ServiceException(String message, TypicalError typicalError) {
        super(message);
        this.typicalError = typicalError;
    }
}
