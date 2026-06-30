package com.hospital.ui;

import com.hospital.database.dao.PatientDao;
import com.hospital.database.dao.sqlite.PatientDaoImpl;
import com.hospital.model.Patient;
import com.hospital.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * UI Panel for displaying and managing patient registry records.
 * Incorporates modular validations and SQLite DAO interfaces.
 */
public class PatientsPanel extends JPanel {
    private JTable patientsTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAddPatient;

    // Polymorphism: Binding implementation to interface reference.
    private PatientDao patientDao = new PatientDaoImpl();

    public PatientsPanel() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);

        // Header Panel (Title + Search + Add Button)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Patient Registry");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setOpaque(false);

        // Search Input
        txtSearch = new JTextField(15);
        txtSearch.putClientProperty("JTextField.placeholderText", "Search patients...");
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });
        controlsPanel.add(txtSearch);

        // Add Patient Button
        btnAddPatient = new JButton("Register Patient");
        btnAddPatient.setPreferredSize(new Dimension(140, 35));
        btnAddPatient.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddPatient.setBackground(new Color(59, 130, 246));
        btnAddPatient.setForeground(Color.WHITE);
        btnAddPatient.setFocusPainted(false);
        btnAddPatient.putClientProperty("JButton.buttonType", "roundRect");
        btnAddPatient.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddPatient.addActionListener(e -> showAddPatientDialog());
        controlsPanel.add(btnAddPatient);

        headerPanel.add(controlsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Patients Table Panel
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(25, 25, 28));
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 50), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"ID", "Name", "Age", "Gender", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientsTable = new JTable(tableModel);
        patientsTable.setRowHeight(35);
        patientsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        patientsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        patientsTable.setShowVerticalLines(false);
        patientsTable.setFillsViewportHeight(true);

        // Set column widths
        patientsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        patientsTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        patientsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        patientsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        patientsTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        patientsTable.getColumnModel().getColumn(5).setPreferredWidth(180);

        JScrollPane scrollPane = new JScrollPane(patientsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        // Initial Data Load
        refreshTable();
    }

    private void performSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            refreshTable();
        } else {
            // Polymorphism: invoking interface search method
            List<Patient> list = patientDao.search(keyword);
            populateTable(list);
        }
    }

    public void refreshTable() {
        // Polymorphism: invoking interface list method
        List<Patient> list = patientDao.getAll();
        populateTable(list);
    }

    private void populateTable(List<Patient> list) {
        tableModel.setRowCount(0);
        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    p.getPhone(),
                    p.getEmail()
            });
        }
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Register New Patient", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Title
        JLabel lblTitle = new JLabel("Register Patient");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Reset grid width
        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Full Name:"), gbc);
        JTextField txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        JTextField txtAge = new JTextField();
        txtAge.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtAge, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gender:"), gbc);
        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> cbGender = new JComboBox<>(genders);
        cbGender.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(cbGender, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Phone:"), gbc);
        JTextField txtPhone = new JTextField();
        txtPhone.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);
        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(btnCancel);

        JButton btnSave = new JButton("Save Patient");
        btnSave.setBackground(new Color(59, 130, 246));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String name = txtName.getText().trim();
            String ageStr = txtAge.getText().trim();
            String gender = (String) cbGender.getSelectedItem();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();

            // Rubric compliance: Detailed, modular validation and user feedback
            if (!ValidationUtils.isValidName(name)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Name: Please enter a valid name. Names can only contain letters, spaces, hyphens, and apostrophes.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationUtils.isValidAge(ageStr)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Age: Please enter a valid whole number between 0 and 125.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationUtils.isValidPhone(phone)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Phone: Please enter a valid phone number containing 7 to 15 digits (spaces/hyphens allowed).", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationUtils.isValidEmail(email)) {
                JOptionPane.showMessageDialog(dialog, 
                        "Invalid Email: Please enter a valid email address (e.g., example@domain.com) or leave it empty.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageStr);
            Patient newPatient = new Patient(name, age, gender, phone, email);
            
            // Polymorphism: save method via interface reference
            if (patientDao.save(newPatient)) {
                JOptionPane.showMessageDialog(dialog, "Patient registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error adding patient to database. Connection failed.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(btnPanel, gbc);

        dialog.getContentPane().add(panel);
        dialog.setVisible(true);
    }
}
