package com.hospital.database.dao;

import com.hospital.model.Patient;
import java.util.List;

/**
 * Interface representing patient-specific data access operations.
 * Demonstrates inheritance by extending the generic Dao interface.
 */
public interface PatientDao extends Dao<Patient> {
    /**
     * Searches patients based on a keyword match in name, phone, or email.
     * @param keyword The search term.
     * @return List of matching Patient models.
     */
    List<Patient> search(String keyword);
}
