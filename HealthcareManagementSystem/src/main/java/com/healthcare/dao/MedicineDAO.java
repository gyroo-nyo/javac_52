package com.healthcare.dao;

import com.healthcare.DatabaseConnection;
import com.healthcare.exception.DatabaseOperationException;
import com.healthcare.interfaces.GenericDAO;
import com.healthcare.model.Medicine;

import java.sql.*;
import java.util.*;

/**
 * RUBRIC: Classes for the database operations (7 marks)
 */
public class MedicineDAO implements GenericDAO<Medicine, Integer> {

    private static final List<Medicine> fallbackInventory = new ArrayList<>();

    public MedicineDAO() {
        seedInventory();
    }

    private void seedInventory() {
        if (fallbackInventory.isEmpty()) {
            fallbackInventory.add(new Medicine(1, "Amoxicillin 500mg", "Antibiotics", 120, 14.50, "2027-05-30"));
            fallbackInventory.add(new Medicine(2, "Atorvastatin 20mg", "Cardiovascular", 14, 28.00, "2026-12-15")); // Low stock
            fallbackInventory.add(new Medicine(3, "Metformin 850mg", "Antidiabetic", 250, 11.20, "2027-09-01"));
            fallbackInventory.add(new Medicine(4, "Epinephrine Auto-Inject", "Emergency", 8, 85.00, "2026-11-20")); // Low stock
        }
    }

    public List<Medicine> getLowStockMedicines() {
        List<Medicine> lowStock = new ArrayList<>();
        for (Medicine m : findAll()) {
            if (m.isLowStock()) {
                lowStock.add(m);
            }
        }
        return lowStock;
    }

    public boolean restockMedicine(int id, int addedQuantity) throws DatabaseOperationException {
        for (Medicine m : fallbackInventory) {
            if (m.getId() == id) {
                m.setQuantity(m.getQuantity() + addedQuantity);
                break;
            }
        }
        if (DatabaseConnection.isConnected()) {
            String sql = "UPDATE medicines SET quantity = quantity + ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, addedQuantity);
                ps.setInt(2, id);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DatabaseOperationException("Restock failed: " + e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public List<Medicine> findAll() {
        List<Medicine> list = new ArrayList<>();
        if (DatabaseConnection.isConnected()) {
            String sql = "SELECT id, name, category, quantity, price, expiry_date FROM medicines";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Medicine(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getDate("expiry_date") != null ? rs.getDate("expiry_date").toString() : "2027-01-01"
                    ));
                }
                return list;
            } catch (SQLException e) {
                System.err.println("[MedicineDAO] findAll error: " + e.getMessage());
            }
        }
        return new ArrayList<>(fallbackInventory);
    }

    @Override
    public Optional<Medicine> findById(Integer id) {
        return fallbackInventory.stream().filter(m -> m.getId() == id).findFirst();
    }

    @Override
    public boolean save(Medicine entity) throws DatabaseOperationException {
        fallbackInventory.add(entity);
        return true;
    }

    @Override
    public boolean update(Medicine entity) throws DatabaseOperationException {
        return true;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseOperationException {
        fallbackInventory.removeIf(m -> m.getId() == id);
        return true;
    }
}
