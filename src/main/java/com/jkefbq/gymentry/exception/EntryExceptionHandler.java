package com.jkefbq.gymentry.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class EntryExceptionHandler {

    @ExceptionHandler(NonActiveSubscriptionException.class)
    public ResponseEntity<@NonNull String> handleSubscriptionExpiredException(NonActiveSubscriptionException e) {
        log.warn("NonActiveSubscriptionException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(VisitsAreOverException.class)
    public ResponseEntity<@NonNull String> handleVisitsAreOverException(VisitsAreOverException e) {
        log.warn("VisitsAreOverException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
