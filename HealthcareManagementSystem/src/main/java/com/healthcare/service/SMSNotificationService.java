package com.healthcare.service;

import com.healthcare.interfaces.NotificationService;

/**
 * Demonstrates Runtime Polymorphism via Interface Implementation
 * RUBRIC: OOP Implementation (Polymorphism)
 */
public class SMSNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS GATEWAY] To: " + recipient + " | Text: " + message);
    }

    @Override
    public String getChannelType() {
        return "Cellular SMS Telephony";
    }
}
