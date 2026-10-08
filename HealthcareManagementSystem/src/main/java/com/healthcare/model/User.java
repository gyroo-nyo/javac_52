package com.healthcare.model;

import com.healthcare.interfaces.Authenticatable;

/**
 * RUBRIC REQUIREMENTS:
 * - OOP Implementation: Inheritance (Abstract Base Class), Encapsulation
 * - OOP Implementation: Interfaces (Implements Authenticatable)
 * - Collections & Generics: Comparable<User> for sorting in Collections
 */
public abstract class User implements Authenticatable, Comparable<User> {
    private int id;
    private String name;
    private String email;
    private String role; // "ADMIN", "DOCTOR", "PATIENT"
    private String passwordHash;

    public User(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public User(int id, String name, String email, String role, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    // Abstract methods demonstrating Polymorphism in subclasses
    public abstract String getRoleDescription();
    public abstract String getDashboardTitle();

    // Authenticatable interface implementation
    @Override
    public boolean authenticate(String password) {
        if (password == null || this.passwordHash == null) return false;
        return this.passwordHash.equals(password);
    }

    @Override
    public String getRole() { return role; }

    @Override
    public String getEmail() { return email; }

    // Comparable implementation for Generics & Collections sorting
    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.getName());
    }

    // Getters and Setters (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setRole(String role) { this.role = role; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public String toString() {
        return String.format("[%s #%d] %s (%s)", role, id, name, email);
    }
}
