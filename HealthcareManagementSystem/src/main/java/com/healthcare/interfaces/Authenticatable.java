package com.healthcare.interfaces;

/**
 * RUBRIC: OOP Implementation - Interfaces
 */
public interface Authenticatable {
    boolean authenticate(String password);
    String getRole();
    String getEmail();
}
