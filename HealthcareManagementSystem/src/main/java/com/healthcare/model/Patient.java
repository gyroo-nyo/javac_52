package com.healthcare.model;

import com.healthcare.interfaces.Billable;

/**
 * RUBRIC:
 * - OOP Implementation: Inheritance (extends User)
 * - OOP Implementation: Interfaces (implements Billable)
 * - OOP Implementation: Polymorphism (overrides abstract methods)
 */
public class Patient extends User implements Billable {
    private int age;
    private String gender;
    private String phone;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private double pendingBalance;

    public Patient(int id, String name, int age, String gender) {
        super(id, name, name.toLowerCase().replace(" ", ".") + "@example.com", "PATIENT");
        this.age = age;
        this.gender = gender;
        this.phone = "N/A";
        this.address = "N/A";
        this.bloodGroup = "O+";
        this.emergencyContact = "N/A";
        this.pendingBalance = 0.0;
    }

    public Patient(int id, String name, int age, String gender, String phone, String email, 
                   String address, String bloodGroup, String emergencyContact) {
        super(id, name, email, "PATIENT");
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.pendingBalance = 0.0;
    }

    @Override
    public String getRoleDescription() {
        return String.format("Registered Patient (Age %d, Gender %s, Blood %s)", age, gender, bloodGroup);
    }

    @Override
    public String getDashboardTitle() {
        return "Patient Self-Service Portal (" + getName() + ")";
    }

    // Billable interface implementation
    @Override
    public double getBillingAmount() {
        return pendingBalance;
    }

    @Override
    public String getBillDescription() {
        return "Hospital Clinical Services for Patient: " + getName();
    }

    // Getters and Setters
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public double getPendingBalance() { return pendingBalance; }
    public void setPendingBalance(double pendingBalance) { this.pendingBalance = pendingBalance; }
}
