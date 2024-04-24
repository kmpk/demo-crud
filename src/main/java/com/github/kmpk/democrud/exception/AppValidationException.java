package com.github.kmpk.democrud.exception;

public abstract class AppValidationException extends RuntimeException {
    AppValidationException(String message) {
        super(message);
    }
}
