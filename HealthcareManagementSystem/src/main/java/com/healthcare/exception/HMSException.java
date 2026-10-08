package com.healthcare.exception;

/**
 * RUBRIC: OOP Implementation - Exception Handling (Base Exception)
 */
public class HMSException extends Exception {
    public HMSException(String message) {
        super(message);
    }

    public HMSException(String message, Throwable cause) {
        super(message, cause);
    }
}
