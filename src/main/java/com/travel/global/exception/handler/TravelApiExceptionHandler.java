package com.travel.global.exception.handler;

import com.travel.global.exception.MemberException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "com.travel.domain")
@Order(value = 1)
public class TravelApiExceptionHandler {

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> travelExceptionAdvice(HttpStatusCodeException e) {
        writeErrorLog(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            log.error("======= Validation Exception =======");
            log.error("Exception type: {}", e.getClass().getSimpleName());
            log.error("Exception message: {}", fieldError.getDefaultMessage());
        });

        String errorMessage = String.join(", ", errors.values());
        writeErrorLog(e.getMessage());

        return ResponseEntity.status(e.getStatusCode()).body(errorMessage);
    }

    @ExceptionHandler(value = {MemberException.class})
    public ResponseEntity<?> handleValidationExceptions(MemberException e) {
        writeErrorLog(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private void writeErrorLog(String message) {
        log.error("error message : {}", message);
    }
}
