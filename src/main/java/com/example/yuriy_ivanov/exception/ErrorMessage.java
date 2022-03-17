package com.example.yuriy_ivanov.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    String name;
    String message;
}
