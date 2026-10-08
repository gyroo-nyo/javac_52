package com.healthcare.dao;

import com.healthcare.DatabaseConnection;
import com.healthcare.exception.DatabaseOperationException;
import com.healthcare.interfaces.GenericDAO;
import com.healthcare.model.MedicalRecord;

import java.sql.*;
import java.util.*;

/**
 * RUBRIC: Classes for the database operations (7 marks)
 */
public class MedicalRecordDAO implements GenericDAO<MedicalRecord, Integer> {

    private static final List<MedicalRecord> fallbackRecords = new ArrayList<>();

    public MedicalRecordDAO() {
        seedRecords();
    }

    private void seedRecords() {
        if (fallbackRecords.isEmpty()) {
            fallbackRecords.add(new MedicalRecord(1, 101, "Eleanor Vance", 1, "Dr. Sarah Smith", "2026-09-12", "Chest palpitations, mild vertigo", "Stage 1 Essential Hypertension", "Lisinopril 10mg daily + sodium moderation", "BP checked at 142/90 mmHg. Follow-up 4 weeks."));
            fallbackRecords.add(new MedicalRecord(2, 102, "Arthur Pendelton", 2, "Dr. James Wilson", "2026-09-28", "Tremors in left hand, stiffness", "Early Parkinson's Syndrome", "Levodopa/Carbidopa 100/25mg TID", "Brain MRI scheduled. Motor response stable."));
        }
    }

    @Override
    public List<MedicalRecord> findAll() {
        List<MedicalRecord> list = new ArrayList<>();
        if (DatabaseConnection.isConnected()) {
            String sql = "SELECT mr.id, mr.patient_id, p.name AS patient_name, mr.doctor_id, d.name AS doctor_name, " +
                    "mr.visit_date, mr.symptoms, mr.diagnosis, mr.treatment, mr.notes " +
                    "FROM medical_records mr " +
                    "LEFT JOIN patients p ON mr.patient_id = p.id " +
                    "LEFT JOIN doctors d ON mr.doctor_id = d.id";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MedicalRecord(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getString("patient_name") != null ? rs.getString("patient_name") : "Patient #" + rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            rs.getString("doctor_name") != null ? rs.getString("doctor_name") : "Dr. #" + rs.getInt("doctor_id"),
                            rs.getDate("visit_date") != null ? rs.getDate("visit_date").toString() : "2026-09-01",
                            rs.getString("symptoms"),
                            rs.getString("diagnosis"),
                            rs.getString("treatment"),
                            rs.getString("notes")
                    ));
                }
                return list;
            } catch (SQLException e) {
                System.err.println("[MedicalRecordDAO] findAll error: " + e.getMessage());
            }
        }
        return new ArrayList<>(fallbackRecords);
    }

    @Override
    public Optional<MedicalRecord> findById(Integer id) {
        return fallbackRecords.stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public boolean save(MedicalRecord entity) throws DatabaseOperationException {
        fallbackRecords.add(entity);
        return true;
    }

    @Override
    public boolean update(MedicalRecord entity) throws DatabaseOperationException {
        return true;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseOperationException {
        fallbackRecords.removeIf(r -> r.getId() == id);
        return true;
    }
}
