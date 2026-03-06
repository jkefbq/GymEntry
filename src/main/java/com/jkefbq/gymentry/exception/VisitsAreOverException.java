package com.jkefbq.gymentry.exception;

public class VisitsAreOverException extends RuntimeException {
    public VisitsAreOverException(String message) {
        super(message);
    }
    public VisitsAreOverException() {
        super();
    }
}
