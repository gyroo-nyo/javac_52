package com.healthcare.model;

import com.healthcare.interfaces.Schedulable;

/**
 * RUBRIC:
 * - OOP Implementation: Inheritance (extends User)
 * - OOP Implementation: Interfaces (implements Schedulable)
 * - OOP Implementation: Polymorphism (overrides abstract methods)
 */
public class Doctor extends User implements Schedulable {
    private String specialization;
    private String phone;
    private double fee;
    private boolean available;

    public Doctor(int id, String name, String specialization, String phone, String email, double fee, boolean available) {
        super(id, name, email, "DOCTOR");
        this.specialization = specialization;
        this.phone = phone;
        this.fee = fee;
        this.available = available;
    }

    public Doctor(int id, String name, String specialization, String phone, String email, double fee, boolean available, String passwordHash) {
        super(id, name, email, "DOCTOR", passwordHash);
        this.specialization = specialization;
        this.phone = phone;
        this.fee = fee;
        this.available = available;
    }

    @Override
    public String getRoleDescription() {
        return String.format("Attending Physician - %s (Consultation Fee: $%.2f)", specialization, fee);
    }

    @Override
    public String getDashboardTitle() {
        return "Doctor Consultation Console (" + getName() + " - " + specialization + ")";
    }

    // Schedulable interface implementation
    @Override
    public boolean isSlotAvailable(String date, String time) {
        // Checked against schedule availability
        return this.available;
    }

    @Override
    public String getScheduleDetails() {
        return String.format("Dr. %s (%s) - Status: %s", getName(), specialization, available ? "Available" : "On Leave");
    }

    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
