package com.github.kmpk.democrud.exception;

public class EntityNotFoundException extends AppValidationException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
