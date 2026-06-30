package com.hospital.database.dao.sqlite;

import com.hospital.database.DatabaseManager;
import com.hospital.database.dao.PatientDao;
import com.hospital.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of PatientDao targeting an SQLite database.
 * Demonstrates Polymorphism since it can be bound to a PatientDao reference.
 */
public class PatientDaoImpl implements PatientDao {

    @Override
    public boolean save(Patient patient) {
        String sql = "INSERT INTO patients(name, age, gender, phone, email) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getGender());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getEmail());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        patient.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQLException in PatientDaoImpl.save: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY name ASC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQLException in PatientDaoImpl.getAll: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public List<Patient> search(String keyword) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE name LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY name ASC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(new Patient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("phone"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException in PatientDaoImpl.search: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }
}
