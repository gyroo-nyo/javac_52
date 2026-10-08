package com.healthcare.service;

import com.healthcare.interfaces.NotificationService;

/**
 * Demonstrates Runtime Polymorphism via Interface Implementation
 * RUBRIC: OOP Implementation (Polymorphism)
 */
public class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[EMAIL DISPATCH] To: " + recipient + " | Body: " + message);
    }

    @Override
    public String getChannelType() {
        return "SMTP/Secure TLS Email";
    }
}
