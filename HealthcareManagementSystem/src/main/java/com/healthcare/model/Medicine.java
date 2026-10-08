package com.healthcare.model;

import com.healthcare.interfaces.Billable;

/**
 * Medicine / Pharmacy Model
 * RUBRIC: OOP Implementation: Interfaces (Implements Billable)
 */
public class Medicine implements Billable {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private String expiryDate;

    public Medicine(int id, String name, String category, int quantity, double price, String expiryDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public boolean isLowStock() {
        return quantity <= 15;
    }

    @Override
    public double getBillingAmount() {
        return price;
    }

    @Override
    public String getBillDescription() {
        return "Pharmacy: " + name + " (" + category + ")";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public String getExpiryDate() { return expiryDate; }
}
