package dev.garcias.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===============================
    // VALIDAÇÃO (MVC)
    // ===============================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                Map.of(
                        "success", false,
                        "status", 400,
                        "errors", errors,
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    // ===============================
    // VALIDAÇÃO (WEBFLUX)
    // ===============================
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> handleWebFluxValidationException(
            WebExchangeBindException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                Map.of(
                        "success", false,
                        "status", 400,
                        "errors", errors,
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    // ===============================
    // ENDPOINT INVÁLIDO
    // ===============================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // ===============================
    // ERROS EXPLÍCITOS HTTP (MVC + WEBFLUX)
    // ===============================
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(
            ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiError.of(HttpStatus.resolve(ex.getStatusCode().value()), ex.getReason()));
    }

    // ===============================
    // ERRO INTERNO CONTROLADO
    // ===============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex) {

        ex.printStackTrace(); // temporário para debug

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."));
    }
}