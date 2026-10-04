# Healthcare Management System

Java 17 + Swing + JDBC + MySQL desktop application.

## Modules
- Authentication/Login
- Dashboard
- Patient Management
- Doctor Management
- Appointment Management
- Medical Records
- Medicine/Inventory Management
- Billing & Payments

## Setup
1. Install JDK 17+ and MySQL 8+.
2. Create the database by running `database.sql` in MySQL Workbench.
3. Open `src/main/java/com/healthcare/Main.java` and change DB_USER/DB_PASS if needed.
4. Open the project as a Maven project.
5. Run `mvn clean compile exec:java` or run `com.healthcare.Main` from your IDE.
6. Login: username `admin`, password `admin123`.

For a production healthcare system, add encryption, audit logging, role permissions, backups, validation, secure secrets, and compliance controls before using real patient data.
