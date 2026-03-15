package dev.garcias.backend.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        boolean success,
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
    public static ApiError of(HttpStatus httpStatus, String message) {
        return new ApiError(
                false,
                httpStatus.value(),
                httpStatus.name(),
                message,
                LocalDateTime.now()
        );
    }
}