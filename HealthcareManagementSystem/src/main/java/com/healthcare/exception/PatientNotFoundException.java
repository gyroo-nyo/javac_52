package com.healthcare.exception;

/**
 * RUBRIC: OOP Implementation - Exception Handling
 */
public class PatientNotFoundException extends HMSException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
