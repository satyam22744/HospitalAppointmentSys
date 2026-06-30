package com.hospital.database.dao.sqlite;

import com.hospital.database.DatabaseManager;
import com.hospital.database.dao.AppointmentDao;
import com.hospital.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of AppointmentDao targeting an SQLite database.
 * Demonstrates Polymorphism since it can be bound to an AppointmentDao reference.
 */
public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public boolean save(Appointment appointment) {
        String sql = "INSERT INTO appointments(patient_id, doctor_name, appointment_date, appointment_time, status) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setString(2, appointment.getDoctorName());
            pstmt.setString(3, appointment.getAppointmentDate());
            pstmt.setString(4, appointment.getAppointmentTime());
            pstmt.setString(5, appointment.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQLException in AppointmentDaoImpl.save: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.id, a.patient_id, p.name AS patient_name, a.doctor_name, a.appointment_date, a.appointment_time, a.status " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.id " +
                     "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("SQLException in AppointmentDaoImpl.getAll: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQLException in AppointmentDaoImpl.updateStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Appointment> getTodayAppointments(String date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.id, a.patient_id, p.name AS patient_name, a.doctor_name, a.appointment_date, a.appointment_time, a.status " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.id " +
                     "WHERE a.appointment_date = ? " +
                     "ORDER BY a.appointment_time ASC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getString("patient_name"),
                            rs.getString("doctor_name"),
                            rs.getString("appointment_date"),
                            rs.getString("appointment_time"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException in AppointmentDaoImpl.getTodayAppointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalPatients", 0);
        stats.put("activeAppointments", 0);
        stats.put("cancelledAppointments", 0);

        String sqlPatients = "SELECT COUNT(*) FROM patients";
        String sqlActive = "SELECT COUNT(*) FROM appointments WHERE status = 'Scheduled'";
        String sqlCancelled = "SELECT COUNT(*) FROM appointments WHERE status = 'Cancelled'";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlPatients)) {
                if (rs.next()) stats.put("totalPatients", rs.getInt(1));
            }
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlActive)) {
                if (rs.next()) stats.put("activeAppointments", rs.getInt(1));
            }
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlCancelled)) {
                if (rs.next()) stats.put("cancelledAppointments", rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("SQLException in AppointmentDaoImpl.getDashboardStats: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
}
