package com.healthcare.interfaces;

/**
 * RUBRIC: OOP Implementation - Interfaces
 */
public interface Schedulable {
    boolean isSlotAvailable(String date, String time);
    String getScheduleDetails();
}
