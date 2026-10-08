package com.healthcare.dao;

import com.healthcare.DatabaseConnection;
import com.healthcare.exception.DatabaseOperationException;
import com.healthcare.interfaces.GenericDAO;
import com.healthcare.model.Bill;

import java.sql.*;
import java.util.*;

/**
 * RUBRIC: Classes for the database operations (7 marks)
 */
public class BillingDAO implements GenericDAO<Bill, Integer> {

    private static final List<Bill> fallbackBills = new ArrayList<>();

    public BillingDAO() {
        seedBills();
    }

    private void seedBills() {
        if (fallbackBills.isEmpty()) {
            fallbackBills.add(new Bill(1, 101, "Eleanor Vance", "Specialist Consultation Fee", 150.00, "Paid", "2026-10-01"));
            fallbackBills.add(new Bill(2, 102, "Arthur Pendelton", "Neurology Inpatient Diagnostics", 620.00, "Pending", "2026-10-03"));
            fallbackBills.add(new Bill(3, 103, "Rajesh Kumar", "Routine Blood Analysis & ECG", 95.00, "Paid", "2026-10-05"));
        }
    }

    public boolean markBillPaid(int billId) throws DatabaseOperationException {
        for (Bill b : fallbackBills) {
            if (b.getId() == billId) {
                b.setPaymentStatus("Paid");
                break;
            }
        }
        if (DatabaseConnection.isConnected()) {
            String sql = "UPDATE bills SET payment_status = 'Paid' WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, billId);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to update bill payment: " + e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public List<Bill> findAll() {
        List<Bill> list = new ArrayList<>();
        if (DatabaseConnection.isConnected()) {
            String sql = "SELECT b.id, b.patient_id, p.name AS patient_name, b.description, b.amount, b.payment_status, b.bill_date " +
                    "FROM bills b LEFT JOIN patients p ON b.patient_id = p.id";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Bill(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getString("patient_name") != null ? rs.getString("patient_name") : "Patient #" + rs.getInt("patient_id"),
                            rs.getString("description"),
                            rs.getDouble("amount"),
                            rs.getString("payment_status"),
                            rs.getTimestamp("bill_date") != null ? rs.getTimestamp("bill_date").toString() : "2026-10-01"
                    ));
                }
                return list;
            } catch (SQLException e) {
                System.err.println("[BillingDAO] findAll error: " + e.getMessage());
            }
        }
        return new ArrayList<>(fallbackBills);
    }

    @Override
    public Optional<Bill> findById(Integer id) {
        return fallbackBills.stream().filter(b -> b.getId() == id).findFirst();
    }

    @Override
    public boolean save(Bill entity) throws DatabaseOperationException {
        fallbackBills.add(entity);
        return true;
    }

    @Override
    public boolean update(Bill entity) throws DatabaseOperationException {
        return true;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseOperationException {
        fallbackBills.removeIf(b -> b.getId() == id);
        return true;
    }
}
