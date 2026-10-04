package com.healthcare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Demonstrates Collections & Generics (List<Patient>)
    public List<Patient> getAllPatients() {
        List<Patient> Patients = new ArrayList<>();
        String query = "SELECT id, name, age, gender FROM patients";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Patients;
    }

    // Demonstrates Transaction Management (Commit & Rollback)
    public boolean addPatientTransaction(String name, int age, String gender) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            // 1. Start Transaction
            conn.setAutoCommit(false);

            String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, gender);
                ps.executeUpdate();
            }

            // 2. Commit Transaction if successful
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // 3. Rollback Transaction if an error occurs
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
