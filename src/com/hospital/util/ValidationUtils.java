package com.hospital.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class handling all input validation routines.
 * Promotes Code Modularity and Reusability.
 */
public class ValidationUtils {

    // Regex pattern for names: Letters, spaces, hyphens, and apostrophes only. No numbers.
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-']+$");

    // Regex pattern for phone: Optional leading '+', digits, spaces, hyphens, parentheses (7 to 15 digits total).
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\-()]{7,15}$");

    // Regex pattern for email: Standard RFC 5322 structure.
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Regex pattern for 24-hour time: HH:MM
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

    /**
     * Validates a person's name (e.g., patient, doctor).
     * @param name The name string.
     * @return true if name is non-empty and contains only valid name characters, false otherwise.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates an age string.
     * @param ageStr The input string representing age.
     * @return true if age is a valid integer between 0 and 125, false otherwise.
     */
    public static boolean isValidAge(String ageStr) {
        if (ageStr == null || ageStr.trim().isEmpty()) {
            return false;
        }
        try {
            int age = Integer.parseInt(ageStr.trim());
            return age >= 0 && age <= 125;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates a phone number.
     * @param phone The phone string.
     * @return true if phone matches digit constraints, false otherwise.
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates an email address.
     * @param email The email string (can be optional / empty).
     * @return true if email is empty (valid optional) or matches email format, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Optional field
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a date string in YYYY-MM-DD format.
     * @param dateStr The date string.
     * @return true if date is parseable as a valid calendar date, false otherwise.
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates if a date string in YYYY-MM-DD format is today or in the future.
     * @param dateStr The date string.
     * @return true if date is parseable and is on or after the current date, false otherwise.
     */
    public static boolean isFutureOrTodayDate(String dateStr) {
        if (!isValidDate(dateStr)) {
            return false;
        }
        LocalDate date = LocalDate.parse(dateStr.trim());
        LocalDate today = LocalDate.now();
        return !date.isBefore(today);
    }

    /**
     * Validates a time string in 24-hour format (HH:MM).
     * @param timeStr The time string.
     * @return true if time matches valid 24-hour hour and minute boundaries, false otherwise.
     */
    public static boolean isValidTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return false;
        }
        return TIME_PATTERN.matcher(timeStr.trim()).matches();
    }
}
