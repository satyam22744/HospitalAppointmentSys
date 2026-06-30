package com.hospital.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private DashboardPanel dashboardPanel;
    private PatientsPanel patientsPanel;
    private AppointmentsPanel appointmentsPanel;

    private JButton btnDashboard;
    private JButton btnPatients;
    private JButton btnAppointments;
    
    private Color sidebarColor = new Color(30, 31, 37);
    private Color sidebarHoverColor = new Color(45, 47, 56);
    private Color sidebarActiveColor = new Color(59, 130, 246); // Modern Blue Accent
    private Color textColor = new Color(243, 244, 246);

    public MainFrame() {
        setTitle("CareFlow - Hospital Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null); // Center on screen

        // Root layout
        JPanel rootPanel = new JPanel(new BorderLayout());
        setContentPane(rootPanel);

        // Create Sidebar
        JPanel sidebar = createSidebar();
        rootPanel.add(sidebar, BorderLayout.WEST);

        // Create Main Work Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(18, 18, 18));
        rootPanel.add(mainArea, BorderLayout.CENTER);

        // Top Header Bar
        JPanel headerBar = createHeaderBar();
        mainArea.add(headerBar, BorderLayout.NORTH);

        // Client Panel (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        contentPanel.setOpaque(false);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        // Initialize Panels
        dashboardPanel = new DashboardPanel(this);
        patientsPanel = new PatientsPanel();
        appointmentsPanel = new AppointmentsPanel();

        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(patientsPanel, "PATIENTS");
        contentPanel.add(appointmentsPanel, "APPOINTMENTS");

        // Show Dashboard first
        showPanel("DASHBOARD", btnDashboard);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(240, 700));
        panel.setBackground(sidebarColor);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Sidebar Logo/Title Area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 25));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(240, 80));

        JLabel lblLogoIcon = new JLabel("⚕");
        lblLogoIcon.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogoIcon.setForeground(sidebarActiveColor);
        logoPanel.add(lblLogoIcon);

        JLabel lblLogoText = new JLabel("CareFlow");
        lblLogoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogoText.setForeground(textColor);
        logoPanel.add(lblLogoText);

        panel.add(logoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation Buttons
        btnDashboard = createSidebarButton(" Dashboard", "DASHBOARD");
        btnPatients = createSidebarButton(" Patients", "PATIENTS");
        btnAppointments = createSidebarButton(" Appointments", "APPOINTMENTS");

        panel.add(btnDashboard);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnPatients);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnAppointments);

        // Footer or spacing push
        panel.add(Box.createVerticalGlue());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        footerPanel.setOpaque(false);
        footerPanel.setMaximumSize(new Dimension(240, 50));
        JLabel lblFooter = new JLabel("v1.0.0 | Connected");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(156, 163, 175));
        footerPanel.add(lblFooter);
        panel.add(footerPanel);

        return panel;
    }

    private JButton createSidebarButton(String text, String targetCard) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(210, 45));
        button.setPreferredSize(new Dimension(210, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(sidebarColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getBackground() != sidebarActiveColor) {
                    button.setBackground(sidebarHoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getBackground() != sidebarActiveColor) {
                    button.setBackground(sidebarColor);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                showPanel(targetCard, button);
            }
        });

        return button;
    }

    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(860, 65));
        header.setBackground(new Color(25, 25, 28));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 45)));

        JLabel lblTitle = new JLabel("  Hospital Appointment Portal");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(243, 244, 246));
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        header.add(lblTitle, BorderLayout.WEST);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 18));
        rightHeader.setOpaque(false);
        
        JLabel lblUser = new JLabel("Administrator ");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(156, 163, 175));
        rightHeader.add(lblUser);

        JLabel lblStatus = new JLabel("● Online");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(16, 185, 129)); // Emerald Green
        rightHeader.add(lblStatus);

        header.add(rightHeader, BorderLayout.EAST);
        return header;
    }

    public void showPanel(String cardName, JButton activeBtn) {
        cardLayout.show(contentPanel, cardName);

        // Reset all buttons
        btnDashboard.setBackground(sidebarColor);
        btnPatients.setBackground(sidebarColor);
        btnAppointments.setBackground(sidebarColor);

        // Highlight selected
        if (activeBtn != null) {
            activeBtn.setBackground(sidebarActiveColor);
        }

        // Trigger updates if necessary
        if ("DASHBOARD".equals(cardName) && dashboardPanel != null) {
            dashboardPanel.refreshStats();
        } else if ("PATIENTS".equals(cardName) && patientsPanel != null) {
            patientsPanel.refreshTable();
        } else if ("APPOINTMENTS".equals(cardName) && appointmentsPanel != null) {
            appointmentsPanel.refreshTable();
        }
    }
}
