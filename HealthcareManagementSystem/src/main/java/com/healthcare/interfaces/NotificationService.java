package com.healthcare.interfaces;

/**
 * RUBRIC: OOP Implementation - Interfaces & Polymorphism
 */
public interface NotificationService {
    void sendNotification(String recipient, String message);
    String getChannelType();
}
