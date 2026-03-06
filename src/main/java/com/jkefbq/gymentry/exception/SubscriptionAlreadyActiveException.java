package com.jkefbq.gymentry.exception;

public class SubscriptionAlreadyActiveException extends RuntimeException {
    public SubscriptionAlreadyActiveException(String message) {
        super(message);
    }
    public SubscriptionAlreadyActiveException() {
        super();
    }
}
