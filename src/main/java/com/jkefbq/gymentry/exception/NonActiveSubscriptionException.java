package com.jkefbq.gymentry.exception;

public class NonActiveSubscriptionException extends RuntimeException {
    public NonActiveSubscriptionException(String message) {
        super(message);
    }
    public NonActiveSubscriptionException() {
        super();
    }
}
