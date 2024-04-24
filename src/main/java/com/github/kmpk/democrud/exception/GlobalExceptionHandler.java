package com.github.kmpk.democrud.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unexpectedException(Exception ex, WebRequest request) {
        log.error("Unexpected exception", ex);
        return createProblemDetailExceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error("ConstraintViolationException: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, UNPROCESSABLE_ENTITY, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        ProblemDetail body = ex.getBody();
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), error.getDefaultMessage());
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidParams.put(error.getField(), error.getDefaultMessage());
        }
        body.setProperty("invalid_params", invalidParams);
        body.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        return handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        if (ex.getMessage().contains("book_isbn_key")) {
            return createCustomDataIntegrityViolationResponse(ex, request, "Book with this isbn already exists");
        }
        return createProblemDetailExceptionResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(AppValidationException.class)
    public ResponseEntity<Object> appValidationException(AppValidationException ex, WebRequest request) {
        log.error("AppValidationException: {}", ex.getMessage());
        return createProblemDetailExceptionResponse(ex, BAD_REQUEST, request);
    }

    private ResponseEntity<Object> createCustomDataIntegrityViolationResponse(DataIntegrityViolationException ex, WebRequest request, String exceptionMessage) {
        ProblemDetail body = createProblemDetail(ex, BAD_REQUEST, exceptionMessage, null, null, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    private ResponseEntity<Object> createProblemDetailExceptionResponse(Exception ex, HttpStatusCode statusCode, WebRequest request) {
        ProblemDetail body = createProblemDetail(ex, statusCode, ex.getMessage(), null, null, request);
        return handleExceptionInternal(ex, body, new HttpHeaders(), statusCode, request);
    }
}
