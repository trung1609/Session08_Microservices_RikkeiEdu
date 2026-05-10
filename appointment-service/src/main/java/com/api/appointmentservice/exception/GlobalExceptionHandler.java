package com.api.appointmentservice.exception;

import com.api.appointmentservice.dto.ApiResponseError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseError(LocalDateTime.now().toString(), 404, ex.getMessage(), "Not Found"));
    }

    @ExceptionHandler(RequestSpamException.class)
    public ResponseEntity<ApiResponseError> handleRateLimit(RequestSpamException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ApiResponseError(
                LocalDateTime.now().toString(), 429, ex.getMessage(), "Too Many Requests"));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiResponseError> handleCircuitBreaker(ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponseError(
                LocalDateTime.now().toString(), 503, ex.getMessage(), "Service Unavailable"));
    }
}
