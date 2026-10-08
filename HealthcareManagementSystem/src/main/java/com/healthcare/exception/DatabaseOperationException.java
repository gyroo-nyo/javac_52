package com.healthcare.exception;

/**
 * RUBRIC: OOP Implementation - Exception Handling
 */
public class DatabaseOperationException extends HMSException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseOperationException(String message) {
        super(message);
    }
}
