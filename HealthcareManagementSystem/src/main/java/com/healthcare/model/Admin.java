package com.healthcare.model;

/**
 * RUBRIC: OOP Implementation - Inheritance & Polymorphism
 */
public class Admin extends User {
    private String department;

    public Admin(int id, String name, String email) {
        super(id, name, email, "ADMIN");
        this.department = "System Administration";
    }

    public Admin(int id, String name, String email, String passwordHash) {
        super(id, name, email, "ADMIN", passwordHash);
        this.department = "System Administration";
    }

    @Override
    public String getRoleDescription() {
        return "System Administrator with full access to hospital telemetry, users, and billing.";
    }

    @Override
    public String getDashboardTitle() {
        return "Command Center - Administrative Portal (" + getName() + ")";
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
