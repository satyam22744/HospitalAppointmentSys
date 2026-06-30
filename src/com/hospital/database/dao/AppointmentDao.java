package com.hospital.database.dao;

import com.hospital.model.Appointment;
import java.util.List;
import java.util.Map;

/**
 * Interface representing appointment-specific data access operations.
 * Demonstrates inheritance by extending the generic Dao interface.
 */
public interface AppointmentDao extends Dao<Appointment> {
    /**
     * Updates the status of an appointment (e.g., Scheduled, Cancelled).
     * @param id The appointment ID.
     * @param status The new status value.
     * @return true if update was successful, false otherwise.
     */
    boolean updateStatus(int id, String status);

    /**
     * Retrieves all appointments scheduled for a specific date.
     * @param date The date string (YYYY-MM-DD).
     * @return List of Appointment models.
     */
    List<Appointment> getTodayAppointments(String date);

    /**
     * Aggregates statistics for total patients, active appointments, and cancelled appointments.
     * @return A map containing statistics metrics.
     */
    Map<String, Integer> getDashboardStats();
}
