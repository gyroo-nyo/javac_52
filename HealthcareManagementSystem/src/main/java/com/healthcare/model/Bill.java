package com.healthcare.model;

/**
 * Bill / Financial Invoice Model
 */
public class Bill {
    private int id;
    private int patientId;
    private String patientName;
    private String description;
    private double amount;
    private String paymentStatus; // "Pending", "Paid", "Cancelled"
    private String billDate;

    public Bill(int id, int patientId, String patientName, String description, double amount, String paymentStatus, String billDate) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.description = description;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.billDate = billDate;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getBillDate() { return billDate; }
}
