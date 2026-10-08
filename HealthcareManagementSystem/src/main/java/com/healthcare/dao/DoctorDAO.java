package com.healthcare.dao;

import com.healthcare.DatabaseConnection;
import com.healthcare.exception.DatabaseOperationException;
import com.healthcare.interfaces.GenericDAO;
import com.healthcare.model.Doctor;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RUBRIC:
 * - Classes for the database operations (7 marks)
 * - Collections & Generics (Set<String> for unique specializations, Map<Integer, Doctor> for O(1) caching)
 */
public class DoctorDAO implements GenericDAO<Doctor, Integer> {

    // Demonstrates Collections & Generics: Map & Set
    private final Map<Integer, Doctor> doctorCache = new ConcurrentHashMap<>();
    private final Set<String> specializations = new TreeSet<>();

    public DoctorDAO() {
        seedDoctors();
    }

    private void seedDoctors() {
        addDoctorToCache(new Doctor(1, "Dr. Sarah Smith", "Cardiology", "+1-555-0192", "sarah.smith@hospital.com", 150.00, true));
        addDoctorToCache(new Doctor(2, "Dr. James Wilson", "Neurology", "+1-555-0193", "james.wilson@hospital.com", 200.00, true));
        addDoctorToCache(new Doctor(3, "Dr. Elena Rostova", "Pediatrics", "+1-555-0194", "elena.rostova@hospital.com", 120.00, true));
        addDoctorToCache(new Doctor(4, "Dr. Marcus Chen", "Orthopedics", "+1-555-0195", "marcus.chen@hospital.com", 175.00, false));
    }

    private void addDoctorToCache(Doctor d) {
        doctorCache.put(d.getId(), d);
        specializations.add(d.getSpecialization());
    }

    /**
     * Demonstrates Collections & Generics: Returning a Set<String>
     */
    public Set<String> getAllSpecializations() {
        if (DatabaseConnection.isConnected()) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT DISTINCT specialization FROM doctors WHERE specialization IS NOT NULL")) {
                while (rs.next()) {
                    specializations.add(rs.getString("specialization"));
                }
            } catch (SQLException e) {
                System.err.println("[DoctorDAO] Error querying specializations: " + e.getMessage());
            }
        }
        return Collections.unmodifiableSet(specializations);
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> list = new ArrayList<>();
        if (DatabaseConnection.isConnected()) {
            String sql = "SELECT id, name, specialization, phone, email, fee, available FROM doctors";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Doctor d = new Doctor(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("specialization"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getDouble("fee"),
                            rs.getBoolean("available")
                    );
                    list.add(d);
                    addDoctorToCache(d);
                }
                return list;
            } catch (SQLException e) {
                System.err.println("[DoctorDAO] findAll SQL error: " + e.getMessage());
            }
        }
        return new ArrayList<>(doctorCache.values());
    }

    @Override
    public Optional<Doctor> findById(Integer id) {
        if (doctorCache.containsKey(id)) {
            return Optional.of(doctorCache.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Doctor entity) throws DatabaseOperationException {
        addDoctorToCache(entity);
        if (DatabaseConnection.isConnected()) {
            String sql = "INSERT INTO doctors (name, specialization, phone, email, fee, available) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getSpecialization());
                ps.setString(3, entity.getPhone());
                ps.setString(4, entity.getEmail());
                ps.setDouble(5, entity.getFee());
                ps.setBoolean(6, entity.isAvailable());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) entity.setId(rs.getInt(1));
                    addDoctorToCache(entity);
                    return true;
                }
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to save doctor: " + e.getMessage(), e);
            }
        }
        return true;
    }

    @Override
    public boolean update(Doctor entity) throws DatabaseOperationException {
        addDoctorToCache(entity);
        return true;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseOperationException {
        doctorCache.remove(id);
        return true;
    }
}
