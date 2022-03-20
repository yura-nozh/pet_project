package com.example.yuriy_ivanov.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

}
