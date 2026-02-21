package com.jkefbq.gymentry.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntryExceptionHandler {

    @ExceptionHandler(SubscriptionExpiredException.class)
    public ResponseEntity<@NonNull String> handleSubscriptionExpiredException(SubscriptionExpiredException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(VisitsAreOverException.class)
    public ResponseEntity<@NonNull String> handleVisitsAreOverException(VisitsAreOverException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
