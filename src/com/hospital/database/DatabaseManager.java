package com.hospital.database;

import java.sql.*;

/**
 * Manages the SQLite database connection and schema initialization.
 * All CRUD operations are delegated to DAO implementations (see dao package),
 * following the modular DAO design pattern.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hospital.db";

    static {
        // Load SQLite JDBC Driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initializeDatabase() {
        String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "age INTEGER, " +
                "gender TEXT, " +
                "phone TEXT, " +
                "email TEXT" +
                ");";

        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "patient_id INTEGER, " +
                "doctor_name TEXT NOT NULL, " +
                "appointment_date TEXT NOT NULL, " +
                "appointment_time TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createPatientsTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
