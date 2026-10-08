-- ========================================================
-- JAVAC HEALTHCARE MANAGEMENT SYSTEM - DATABASE SCHEMA
-- Compatible with MySQL 8.0+ / JDBC Connector/J
-- ========================================================

CREATE DATABASE IF NOT EXISTS healthcare_db;
USE healthcare_db;

-- 1. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100),
    password_hash VARCHAR(64) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. PATIENTS TABLE
CREATE TABLE IF NOT EXISTS patients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    blood_group VARCHAR(10),
    emergency_contact VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. DOCTORS TABLE
CREATE TABLE IF NOT EXISTS doctors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    fee DECIMAL(10,2) DEFAULT 0,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. APPOINTMENTS TABLE
CREATE TABLE IF NOT EXISTS appointments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(30) DEFAULT 'Scheduled',
    notes VARCHAR(255),
    FOREIGN KEY(patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY(doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- 5. MEDICAL RECORDS (EHR)
CREATE TABLE IF NOT EXISTS medical_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT,
    visit_date DATE NOT NULL,
    symptoms TEXT,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    FOREIGN KEY(patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY(doctor_id) REFERENCES doctors(id) ON DELETE SET NULL
);

-- 6. MEDICINES / INVENTORY TABLE
CREATE TABLE IF NOT EXISTS medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    quantity INT DEFAULT 0,
    price DECIMAL(10,2) DEFAULT 0,
    expiry_date DATE
);

-- 7. BILLS & PAYMENTS TABLE
CREATE TABLE IF NOT EXISTS bills (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(30) DEFAULT 'Pending',
    bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- ========================================================
-- SEED DATA FOR DEMO & TESTING
-- ========================================================

-- Users
INSERT IGNORE INTO users (id, username, email, password_hash, role) VALUES
(1, 'admin', 'admin@hospital.com', SHA2('admin123', 256), 'ADMIN'),
(2, 'dr_sarah', 'sarah.smith@hospital.com', SHA2('doctor123', 256), 'DOCTOR'),
(3, 'dr_james', 'james.wilson@hospital.com', SHA2('doctor123', 256), 'DOCTOR'),
(4, 'eleanor', 'eleanor@example.com', SHA2('patient123', 256), 'PATIENT');

-- Doctors
INSERT IGNORE INTO doctors (id, name, specialization, phone, email, fee, available) VALUES
(1, 'Dr. Sarah Smith', 'Cardiology', '+1-555-0192', 'sarah.smith@hospital.com', 150.00, TRUE),
(2, 'Dr. James Wilson', 'Neurology', '+1-555-0193', 'james.wilson@hospital.com', 200.00, TRUE),
(3, 'Dr. Elena Rostova', 'Pediatrics', '+1-555-0194', 'elena.rostova@hospital.com', 120.00, TRUE),
(4, 'Dr. Marcus Chen', 'Orthopedics', '+1-555-0195', 'marcus.chen@hospital.com', 175.00, FALSE);

-- Patients
INSERT IGNORE INTO patients (id, name, age, gender, phone, email, address, blood_group, emergency_contact) VALUES
(1, 'Eleanor Vance', 42, 'Female', '+1-555-234-8901', 'eleanor@example.com', '742 Evergreen Terr, Springfield', 'O+', 'Robert Vance - (555) 987-6543'),
(2, 'Arthur Pendelton', 67, 'Male', '+1-555-345-6789', 'arthur@example.com', '12 Baker Street, Apt 4B', 'A-', 'Grace Pendelton - (555) 876-5432'),
(3, 'Rajesh Kumar', 35, 'Male', '+1-555-456-7890', 'rajesh@example.com', '88 Orchid Highway', 'B+', 'Priya Kumar - (555) 765-4321');

-- Appointments
INSERT IGNORE INTO appointments (id, patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES
(1, 1, 1, '2026-10-15', '09:30:00', 'Confirmed', 'Cardiac follow-up checkup'),
(2, 2, 2, '2026-10-16', '11:00:00', 'Scheduled', 'Neurology consultation'),
(3, 3, 1, '2026-10-17', '14:15:00', 'Scheduled', 'Routine ECG assessment');

-- Medicines
INSERT IGNORE INTO medicines (id, name, category, quantity, price, expiry_date) VALUES
(1, 'Amoxicillin 500mg', 'Antibiotics', 120, 14.50, '2027-05-30'),
(2, 'Atorvastatin 20mg', 'Cardiovascular', 14, 28.00, '2026-12-15'),
(3, 'Metformin 850mg', 'Antidiabetic', 250, 11.20, '2027-09-01'),
(4, 'Epinephrine Auto-Inject', 'Emergency', 8, 85.00, '2026-11-20');

-- Bills
INSERT IGNORE INTO bills (id, patient_id, description, amount, payment_status) VALUES
(1, 1, 'Cardiology Consultation Fee', 150.00, 'Paid'),
(2, 2, 'Neurology Diagnostic Assessment', 620.00, 'Pending'),
(3, 3, 'Comprehensive Blood Analysis', 95.00, 'Paid');
