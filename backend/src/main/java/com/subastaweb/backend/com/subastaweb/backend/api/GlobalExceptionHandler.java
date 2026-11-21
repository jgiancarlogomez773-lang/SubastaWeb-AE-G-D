package com.subastaweb.backend.api;

import com.subastaweb.backend.application.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorBody(
            String code,
            String message,
            String requestId,
            List details
    ) {}

    @ExceptionHandler(AuthService.EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorBody> handleEmailConflict(AuthService.EmailAlreadyExistsException ex) {
        ErrorBody body = new ErrorBody(
                "EMAIL_ALREADY_EXISTS",
                ex.getMessage(),
                UUID.randomUUID().toString(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> handleValidation(MethodArgumentNotValidException ex) {
        List details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> {
                    Map item = new LinkedHashMap();
                    String field = (err instanceof FieldError fe) ? fe.getField() : err.getObjectName();
                    item.put("field", field);
                    item.put("issue", err.getDefaultMessage());
                    return item;
                })
                .toList();

        ErrorBody body = new ErrorBody(
                "VALIDATION_ERROR",
                "Los datos enviados no son validos",
                UUID.randomUUID().toString(),
                details
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }
}
