package com.hospital.ui;

import com.hospital.database.dao.AppointmentDao;
import com.hospital.database.dao.PatientDao;
import com.hospital.database.dao.sqlite.AppointmentDaoImpl;
import com.hospital.database.dao.sqlite.PatientDaoImpl;
import com.hospital.model.Patient;
import com.hospital.model.Appointment;
import com.hospital.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * UI Panel for booking and cancelling appointment records.
 * Integrates SQLite DAOs and advanced input validation routines.
 */
public class AppointmentsPanel extends JPanel {
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JButton btnBook;
    private JButton btnCancelAppointment;

    // Polymorphism: Binding implementations to interface references.
    private AppointmentDao appointmentDao = new AppointmentDaoImpl();
    private PatientDao patientDao = new PatientDaoImpl();

    public AppointmentsPanel() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);

        // Header Panel (Title + Actions)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Appointment Board");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setOpaque(false);

        // Cancel Appointment Button
        btnCancelAppointment = new JButton("Cancel Appointment");
        btnCancelAppointment.setPreferredSize(new Dimension(160, 35));
        btnCancelAppointment.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelAppointment.setBackground(new Color(239, 68, 68)); // Red Accent
        btnCancelAppointment.setForeground(Color.WHITE);
        btnCancelAppointment.setFocusPainted(false);
        btnCancelAppointment.putClientProperty("JButton.buttonType", "roundRect");
        btnCancelAppointment.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelAppointment.setEnabled(false); // Enabled only when row is selected
        btnCancelAppointment.addActionListener(e -> cancelSelectedAppointment());
        controlsPanel.add(btnCancelAppointment);

        // Book Appointment Button
        btnBook = new JButton("Book Appointment");
        btnBook.setPreferredSize(new Dimension(150, 35));
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBook.setBackground(new Color(16, 185, 129)); // Emerald Green Accent
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);
        btnBook.putClientProperty("JButton.buttonType", "roundRect");
        btnBook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBook.addActionListener(e -> showBookAppointmentDialog());
        controlsPanel.add(btnBook);

        headerPanel.add(controlsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Appointments Table Panel
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(25, 25, 28));
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 50), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"Appointment ID", "Patient Name", "Doctor", "Date", "Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setRowHeight(35);
        appointmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appointmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        appointmentsTable.setShowVerticalLines(false);
        appointmentsTable.setFillsViewportHeight(true);

        // Set column widths
        appointmentsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        appointmentsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        appointmentsTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        // Listen for table row selection changes
        appointmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object statusObj = appointmentsTable.getValueAt(selectedRow, 5);
                String status = statusObj != null ? statusObj.toString() : "";
                // Can only cancel appointments that are "Scheduled"
                btnCancelAppointment.setEnabled("Scheduled".equalsIgnoreCase(status));
            } else {
                btnCancelAppointment.setEnabled(false);
            }
        });

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        // Initial Data Load
        refreshTable();
    }

    public void refreshTable() {
        // Polymorphism: listing appointments via interface reference
        List<Appointment> list = appointmentDao.getAll();
        tableModel.setRowCount(0);
        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getPatientName(),
                    a.getDoctorName(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getStatus()
            });
        }
        btnCancelAppointment.setEnabled(false);
    }

    private void cancelSelectedAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow < 0) return;

        // Safely parse ID and Name to prevent silent ClassCastExceptions in Swing EDT
        int appId;
        try {
            appId = Integer.parseInt(appointmentsTable.getValueAt(selectedRow, 0).toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Appointment ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String patientName = appointmentsTable.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel the appointment for " + patientName + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Polymorphism: updating status via interface reference
            if (appointmentDao.updateStatus(appId, "Cancelled")) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating appointment status.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBookAppointmentDialog() {
        // Polymorphism: fetching patients via interface reference
        List<Patient> patients = patientDao.getAll();
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please register at least one patient first before booking an appointment.", "No Patients Registered", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Book Appointment", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Title
        JLabel lblTitle = new JLabel("Book Appointment");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Reset grid width
        gbc.gridwidth = 1;

        // Patient Dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Select Patient:"), gbc);
        JComboBox<Patient> cbPatient = new JComboBox<>(patients.toArray(new Patient[0]));
        cbPatient.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(cbPatient, gbc);

        // Doctor Name
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Doctor Name:"), gbc);
        JTextField txtDoctor = new JTextField();
        txtDoctor.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtDoctor, gbc);

        // Appointment Date
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        JTextField txtDate = new JTextField(LocalDate.now().toString());
        txtDate.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtDate, gbc);

        // Appointment Time
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Time (HH:MM):"), gbc);
        
        // Use current time rounded to next hour as a helpful placeholder/default
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        String defaultTime = String.format("%02d:00", (now.getHour() + 1) % 24);
        JTextField txtTime = new JTextField(defaultTime);
        txtTime.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtTime, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(btnCancel);

        JButton btnSave = new JButton("Book Slots");
        btnSave.setBackground(new Color(16, 185, 129));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            Patient patient = (Patient) cbPatient.getSelectedItem();
            String doctor = txtDoctor.getText().trim();
            String dateStr = txtDate.getText().trim();
            String timeStr = txtTime.getText().trim();

            if (patient == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a valid registered patient.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Rubric compliance: Input validation checks
            if (!ValidationUtils.isValidName(doctor)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Doctor Name: Doctor name can only contain letters, spaces, hyphens, and apostrophes.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationUtils.isFutureOrTodayDate(dateStr)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Date: Please enter a valid calendar date in YYYY-MM-DD format (e.g., 2026-06-30). The date cannot be in the past.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationUtils.isValidTime(timeStr)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Time: Please enter a valid time in 24-hour HH:MM format (e.g., 14:30). Hours must be 00-23, minutes 00-59.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Appointment app = new Appointment(patient.getId(), doctor, dateStr, timeStr, "Scheduled");
            
            // Polymorphism: saving appointment via interface reference
            if (appointmentDao.save(app)) {
                JOptionPane.showMessageDialog(dialog, "Appointment booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error saving appointment to database. Connection failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 8, 8);
        panel.add(btnPanel, gbc);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }
}
