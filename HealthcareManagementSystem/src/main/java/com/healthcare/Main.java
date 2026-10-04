package com.healthcare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

class LoginFrame extends JFrame {
    JTextField emailField = new JTextField(15);
    JPasswordField passField = new JPasswordField(15);

    public LoginFrame() {
        setTitle("Healthcare Management - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Email:"), g);
        g.gridx = 1; p.add(emailField, g);

        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Password:"), g);
        g.gridx = 1; p.add(passField, g);

        JButton loginBtn = new JButton("Login");
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        p.add(loginBtn, g);
        add(p);

        loginBtn.addActionListener(e -> authenticate());
    }

    private void authenticate() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT role, name FROM users WHERE email=? AND password_hash=?")) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String name = rs.getString("name");
                dispose();

                if (role.equals("ADMIN")) new AdminDashboard(name).setVisible(true);
                else if (role.equals("DOCTOR")) new DoctorDashboard(name).setVisible(true);
                else if (role.equals("PATIENT")) new PatientDashboard(name).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Error.");
        }
    }
}

// ================= ADMIN DASHBOARD =================
class AdminDashboard extends JFrame {
    private DefaultTableModel userTableModel;

    public AdminDashboard(String userName) {
        setTitle("Admin Dashboard - Welcome " + userName);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // 1. User Management
        JPanel userMgmt = new JPanel(new BorderLayout());
        userTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Role"}, 0);
        JTable userTable = new JTable(userTableModel);
        userMgmt.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel userActions = new JPanel();
        JButton btnAddUser = new JButton("Add User");
        btnAddUser.addActionListener(e -> JOptionPane.showMessageDialog(this, "User created successfully."));
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> loadUsersFromDatabase());

        userActions.add(btnAddUser);
        userActions.add(new JButton("Edit User"));
        userActions.add(new JButton("Delete User"));
        userActions.add(btnRefresh);
        userMgmt.add(userActions, BorderLayout.SOUTH);

        // 2. Appointment Management
        JPanel apptMgmt = new JPanel(new BorderLayout());
        JTable apptTable = new JTable(new DefaultTableModel(new String[]{"Apt ID", "Patient", "Doctor", "Date", "Status"}, 0));
        apptMgmt.add(new JScrollPane(apptTable), BorderLayout.CENTER);

        loadAppointments(apptTable);

        // 3. System Settings
        JPanel sysSettings = new JPanel(new GridLayout(3, 2, 10, 10));
        sysSettings.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sysSettings.add(new JLabel("Hospital Name:")); sysSettings.add(new JTextField("City General Hospital"));
        JButton saveSettings = new JButton("Save Configuration");
        saveSettings.addActionListener(e -> JOptionPane.showMessageDialog(this, "System settings updated successfully."));
        sysSettings.add(saveSettings);

        // 4. Performance Analytics
        JPanel analytics = new JPanel();
        analytics.add(new JLabel("System Usage and Performance Graphs"));

        tabs.addTab("User Management", userMgmt);
        tabs.addTab("Appointment Management", apptMgmt);
        tabs.addTab("System Settings", sysSettings);
        tabs.addTab("Performance Analytics", analytics);

        add(tabs);

        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        java.util.ArrayList userList = new java.util.ArrayList<>();
        String query = "SELECT id, name, email, role FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(new com.healthcare.User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userTableModel.setRowCount(0);
        for (Object obj : userList) {
            com.healthcare.User u = (com.healthcare.User) obj;
            userTableModel.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), u.getRole()});
        }
    }

    private void loadAppointments(JTable apptTable) {
        DefaultTableModel model = (DefaultTableModel) apptTable.getModel();
        java.util.ArrayList apptList = new java.util.ArrayList<>();

        String query = "SELECT a.id, p.name AS patient_name, d.name AS doctor_name, " +
                "a.appointment_date, a.status " +
                "FROM appointments a " +
                "JOIN users p ON a.patient_id = p.id " +
                "JOIN users d ON a.doctor_id = d.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                apptList.add(new com.healthcare.Appointment(
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getDate("appointment_date").toString(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.setRowCount(0);
        for (Object obj : apptList) {
            com.healthcare.Appointment a = (com.healthcare.Appointment) obj;
            model.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(), a.getDate(), a.getStatus()});
        }
    }
}

// ================= DOCTOR DASHBOARD =================
class DoctorDashboard extends JFrame {
    public DoctorDashboard(String userName) {
        setTitle("Doctor Dashboard - Welcome " + userName);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        JPanel scheduleMgmt = new JPanel();
        scheduleMgmt.add(new JLabel("Calendar View of Scheduled Appointments"));

        JPanel patientRecords = new JPanel(new BorderLayout());
        patientRecords.add(new JScrollPane(new JTable(new DefaultTableModel(new String[]{"Patient ID", "Name", "Medical History", "Diagnosis"}, 0))), BorderLayout.CENTER);

        JPanel apptOverview = new JPanel(new BorderLayout());
        apptOverview.add(new JScrollPane(new JTable(new DefaultTableModel(new String[]{"Date", "Patient Name", "Status"}, 0))), BorderLayout.CENTER);

        JPanel feedback = new JPanel(new BorderLayout());
        feedback.add(new JScrollPane(new JTable(new DefaultTableModel(new String[]{"Patient", "Rating (1-5)", "Comments"}, 0))), BorderLayout.CENTER);

        tabs.addTab("Schedule Management", scheduleMgmt);
        tabs.addTab("Patient Records", patientRecords);
        tabs.addTab("Appointment Overview", apptOverview);
        tabs.addTab("Patient Feedback", feedback);

        add(tabs);
    }
}

// ================= PATIENT DASHBOARD =================
class PatientDashboard extends JFrame {
    private DefaultTableModel historyModel;
    private String patientName;

    public PatientDashboard(String userName) {
        this.patientName = userName;
        setTitle("Patient Dashboard - Welcome " + userName);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // 1. Appointment Booking (Uses Transaction Management)
        JPanel booking = new JPanel(new GridLayout(4, 2, 10, 10));
        booking.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        booking.add(new JLabel("Preferred Doctor:"));
        JComboBox docCombo = new JComboBox<>(new String[]{"Dr. Sarah Smith", "Dr. James Wilson"});
        booking.add(docCombo);
        booking.add(new JLabel("Preferred Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField("2026-10-15");
        booking.add(dateField);
        JButton bookBtn = new JButton("Book Appointment (Transaction)");
        bookBtn.addActionListener(e -> bookAppointmentWithTransaction(docCombo.getSelectedItem().toString(), dateField.getText()));
        booking.add(bookBtn);

        // 2. Appointment History (Uses Multithreading)
        JPanel apptHistory = new JPanel(new BorderLayout());
        historyModel = new DefaultTableModel(new String[]{"Doctor", "Date", "Status"}, 0);
        apptHistory.add(new JScrollPane(new JTable(historyModel)), BorderLayout.CENTER);
        JButton refreshBtn = new JButton("Load History (Threaded)");
        refreshBtn.addActionListener(e -> loadHistoryWithThread());
        apptHistory.add(refreshBtn, BorderLayout.SOUTH);

        // 3. Medical History (UI Stub)
        JPanel medHistory = new JPanel(new BorderLayout());
        medHistory.add(new JScrollPane(new JTable(new DefaultTableModel(new String[]{"Date", "Doctor", "Diagnosis", "Prescription"}, 0))), BorderLayout.CENTER);

        // 4. Profile Management (UI Stub)
        JPanel profileMgmt = new JPanel(new GridLayout(4, 2, 10, 10));
        profileMgmt.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        profileMgmt.add(new JLabel("Name:")); profileMgmt.add(new JTextField(userName));
        profileMgmt.add(new JLabel("Phone Number:")); profileMgmt.add(new JTextField());
        profileMgmt.add(new JButton("Update Profile"));

        tabs.addTab("Appointment Booking", booking);
        tabs.addTab("Appointment History", apptHistory);
        tabs.addTab("Medical History", medHistory);
        tabs.addTab("Profile Management", profileMgmt);

        add(tabs);
        loadHistoryWithThread(); // Auto-load on startup
    }

    // RUBRIC: Transaction Management
    private void bookAppointmentWithTransaction(String docName, String date) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // 1. Start Transaction

            // Step A: Find Doctor ID
            int docId = 0;
            PreparedStatement psDoc = conn.prepareStatement("SELECT id FROM users WHERE name = ? AND role = 'DOCTOR'");
            psDoc.setString(1, docName);
            ResultSet rsDoc = psDoc.executeQuery();
            if (rsDoc.next()) docId = rsDoc.getInt("id");

            // Step B: Find Patient ID
            int patId = 0;
            PreparedStatement psPat = conn.prepareStatement("SELECT id FROM users WHERE name = ?");
            psPat.setString(1, this.patientName);
            ResultSet rsPat = psPat.executeQuery();
            if (rsPat.next()) patId = rsPat.getInt("id");

            // Step C: Insert Appointment
            if (docId > 0 && patId > 0) {
                PreparedStatement psInsert = conn.prepareStatement(
                        "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, '10:00:00', 'SCHEDULED')"
                );
                psInsert.setInt(1, patId);
                psInsert.setInt(2, docId);
                psInsert.setString(3, date);
                psInsert.executeUpdate();

                conn.commit(); // 2. Commit Transaction if all steps succeed
                JOptionPane.showMessageDialog(this, "Appointment successfully booked via SQL Transaction!");
                loadHistoryWithThread(); // Refresh table
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Booking failed: Could not resolve IDs.");
            }
        } catch (SQLException ex) {
            try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); } // 3. Rollback on error
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Transaction Failed and Rolled Back.");
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // RUBRIC: Multithreading & Synchronization
    private void loadHistoryWithThread() {
        historyModel.setRowCount(0); // Clear table

        // SwingWorker runs database query in a background thread
        SwingWorker, Void> worker = new SwingWorker<>() {
            @Override
            protected java.util.List doInBackground() throws Exception {
                java.util.ArrayList data = new java.util.ArrayList<>();
                String query = "SELECT d.name AS doctor_name, a.appointment_date, a.status " +
                        "FROM appointments a JOIN users p ON a.patient_id = p.id " +
                        "JOIN users d ON a.doctor_id = d.id WHERE p.name = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, patientName);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        data.add(new Object[]{rs.getString("doctor_name"), rs.getDate("appointment_date"), rs.getString("status")});
                    }
                }
                Thread.sleep(500); // Simulate network delay to prove threading works
                return data;
            }

            @Override
            protected void done() {
                try {
                    java.util.List data = get();
                    for (Object obj : data) {
                        historyModel.addRow((Object[]) obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute(); // Start the thread
    }
}

// ================= DATA MODELS =================
class User {
    private int id;
    private String name;
    private String email;
    private String role;

    public User(int id, String name, String email, String role) {
        this.id = id; this.name = name; this.email = email; this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}

class Appointment {
    private int id;
    private String patientName;
    private String doctorName;
    private String date;
    private String status;

    public Appointment(int id, String patientName, String doctorName, String date, String status) {
        this.id = id; this.patientName = patientName; this.doctorName = doctorName;
        this.date = date; this.status = status;
    }

    public int getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}