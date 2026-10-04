CREATE DATABASE IF NOT EXISTS healthcare_db;
USE healthcare_db;

CREATE TABLE IF NOT EXISTS users (
 id INT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) UNIQUE NOT NULL,
 password_hash VARCHAR(64) NOT NULL,
 role VARCHAR(20) NOT NULL DEFAULT 'ADMIN'
);
CREATE TABLE IF NOT EXISTS Patients (
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
CREATE TABLE IF NOT EXISTS doctors (
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100) NOT NULL,
 specialization VARCHAR(100),
 phone VARCHAR(20),
 email VARCHAR(100),
 fee DECIMAL(10,2) DEFAULT 0,
 available BOOLEAN DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS appointments (
 id INT PRIMARY KEY AUTO_INCREMENT,
 patient_id INT NOT NULL,
 doctor_id INT NOT NULL,
 appointment_date DATE NOT NULL,
 appointment_time TIME NOT NULL,
 status VARCHAR(30) DEFAULT 'Scheduled',
 notes VARCHAR(255),
 FOREIGN KEY(patient_id) REFERENCES Patients(id) ON DELETE CASCADE,
 FOREIGN KEY(doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS medical_records (
 id INT PRIMARY KEY AUTO_INCREMENT,
 patient_id INT NOT NULL,
 doctor_id INT,
 visit_date DATE NOT NULL,
 symptoms TEXT,
 diagnosis TEXT,
 treatment TEXT,
 notes TEXT,
 FOREIGN KEY(patient_id) REFERENCES Patients(id) ON DELETE CASCADE,
 FOREIGN KEY(doctor_id) REFERENCES doctors(id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS medicines (
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100) NOT NULL,
 category VARCHAR(50),
 quantity INT DEFAULT 0,
 price DECIMAL(10,2) DEFAULT 0,
 expiry_date DATE
);
CREATE TABLE IF NOT EXISTS bills (
 id INT PRIMARY KEY AUTO_INCREMENT,
 patient_id INT NOT NULL,
 description VARCHAR(255),
 amount DECIMAL(10,2) NOT NULL,
 payment_status VARCHAR(30) DEFAULT 'Pending',
 bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(patient_id) REFERENCES Patients(id) ON DELETE CASCADE
);

INSERT INTO users(username,password_hash,role)
SELECT 'admin', SHA2('admin123',256), 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');
