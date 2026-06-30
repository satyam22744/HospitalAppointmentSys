package com.hospital.ui;

import com.hospital.database.dao.AppointmentDao;
import com.hospital.database.dao.sqlite.AppointmentDaoImpl;
import com.hospital.model.Appointment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * UI Panel for displaying overview stats metrics and today's scheduling board.
 * References interface types for database queries.
 */
public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel lblTotalPatients;
    private JLabel lblActiveAppointments;
    private JLabel lblCancelledAppointments;
    private JTable todayTable;
    private DefaultTableModel tableModel;
    private JLabel lblTodayHeader;

    // Polymorphism: Binding implementation to interface reference.
    private AppointmentDao appointmentDao = new AppointmentDaoImpl();

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 20));
        setOpaque(false);

        // Top Section: Greeting & Quick Stats
        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);

        // Greeting
        JPanel greetingPanel = new JPanel(new BorderLayout());
        greetingPanel.setOpaque(false);
        
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"));
        
        JLabel lblWelcome = new JLabel("Welcome Back, Admin");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        greetingPanel.add(lblWelcome, BorderLayout.NORTH);

        JLabel lblDate = new JLabel(formattedDate);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(new Color(156, 163, 175));
        greetingPanel.add(lblDate, BorderLayout.SOUTH);

        topSection.add(greetingPanel, BorderLayout.NORTH);

        // Stat Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);

        // Initialize Stats
        lblTotalPatients = new JLabel("0");
        lblActiveAppointments = new JLabel("0");
        lblCancelledAppointments = new JLabel("0");

        cardsPanel.add(createStatCard("Total Patients", lblTotalPatients, new Color(59, 130, 246), "👥")); // Blue
        cardsPanel.add(createStatCard("Active Bookings", lblActiveAppointments, new Color(16, 185, 129), "📅")); // Emerald
        cardsPanel.add(createStatCard("Cancelled Bookings", lblCancelledAppointments, new Color(239, 68, 68), "❌")); // Red

        topSection.add(cardsPanel, BorderLayout.CENTER);
        add(topSection, BorderLayout.NORTH);

        // Center Section: Today's Appointments & Quick Actions
        JPanel bodySection = new JPanel(new BorderLayout(20, 0));
        bodySection.setOpaque(false);

        // Today's Appointments Panel
        JPanel schedulePanel = new JPanel(new BorderLayout(0, 10));
        schedulePanel.setBackground(new Color(25, 25, 28));
        schedulePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 50), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        lblTodayHeader = new JLabel("Today's Schedule");
        lblTodayHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTodayHeader.setForeground(Color.WHITE);
        schedulePanel.add(lblTodayHeader, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Time", "Patient Name", "Doctor", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        todayTable = new JTable(tableModel);
        todayTable.setRowHeight(35);
        todayTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        todayTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        todayTable.setShowVerticalLines(false);
        todayTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(todayTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        schedulePanel.add(scrollPane, BorderLayout.CENTER);

        bodySection.add(schedulePanel, BorderLayout.CENTER);

        // Quick Actions Sidebar
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setPreferredSize(new Dimension(250, 0));
        actionsPanel.setBackground(new Color(25, 25, 28));
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 45, 50), 1),
                new EmptyBorder(15, 15, 15, 15)
                ));

        JLabel lblActions = new JLabel("Quick Actions");
        lblActions.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblActions.setForeground(Color.WHITE);
        lblActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.add(lblActions);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnRegister = createActionButton("Register New Patient", new Color(59, 130, 246));
        btnRegister.addActionListener(e -> mainFrame.showPanel("PATIENTS", null));
        actionsPanel.add(btnRegister);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnBook = createActionButton("Book Appointment", new Color(16, 185, 129));
        btnBook.addActionListener(e -> mainFrame.showPanel("APPOINTMENTS", null));
        actionsPanel.add(btnBook);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnRefresh = createActionButton("Refresh Data", new Color(75, 85, 99));
        btnRefresh.addActionListener(e -> refreshStats());
        actionsPanel.add(btnRefresh);

        bodySection.add(actionsPanel, BorderLayout.EAST);

        add(bodySection, BorderLayout.CENTER);

        // Initial Data Load
        refreshStats();
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(25, 25, 28));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(45, 45, 50), 1),
                        new EmptyBorder(15, 20, 15, 20)
                )
        ));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(156, 163, 175));
        textPanel.add(lblTitle);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        textPanel.add(valueLabel);

        card.add(textPanel, BorderLayout.CENTER);

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        lblEmoji.setForeground(accentColor);
        card.add(lblEmoji, BorderLayout.EAST);

        return card;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(220, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.putClientProperty("JButton.buttonType", "roundRect");
        
        return button;
    }

    public void refreshStats() {
        // Polymorphism: fetching aggregate stats via interface reference
        Map<String, Integer> stats = appointmentDao.getDashboardStats();
        lblTotalPatients.setText(String.valueOf(stats.get("totalPatients")));
        lblActiveAppointments.setText(String.valueOf(stats.get("activeAppointments")));
        lblCancelledAppointments.setText(String.valueOf(stats.get("cancelledAppointments")));

        // Load today's appointments
        LocalDate today = LocalDate.now();
        String todayString = today.toString(); // YYYY-MM-DD
        
        // Polymorphism: fetching lists via interface reference
        List<Appointment> todayList = appointmentDao.getTodayAppointments(todayString);
        tableModel.setRowCount(0);
        for (Appointment app : todayList) {
            tableModel.addRow(new Object[]{
                    app.getAppointmentTime(),
                    app.getPatientName(),
                    app.getDoctorName(),
                    app.getStatus()
            });
        }
        
        lblTodayHeader.setText("Today's Schedule (" + todayList.size() + " appointments)");
    }
}
