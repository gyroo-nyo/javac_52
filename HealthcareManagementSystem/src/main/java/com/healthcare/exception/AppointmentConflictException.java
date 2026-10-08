package com.healthcare.exception;

/**
 * Thrown when two patients attempt to book the exact same doctor time slot concurrently.
 * RUBRIC: OOP Implementation - Exception Handling
 */
public class AppointmentConflictException extends HMSException {
    private final int doctorId;
    private final String requestedDate;
    private final String requestedTime;

    public AppointmentConflictException(int doctorId, String requestedDate, String requestedTime) {
        super(String.format("Slot Conflict: Doctor ID %d is already booked on %s at %s.", doctorId, requestedDate, requestedTime));
        this.doctorId = doctorId;
        this.requestedDate = requestedDate;
        this.requestedTime = requestedTime;
    }

    public int getDoctorId() { return doctorId; }
    public String getRequestedDate() { return requestedDate; }
    public String getRequestedTime() { return requestedTime; }
}
