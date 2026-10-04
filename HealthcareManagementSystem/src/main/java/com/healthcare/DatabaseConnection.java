package com.healthcare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/healthcare_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "qwerty@123456"; // CHANGE THIS

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
