package com.hospital;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hospital.database.DatabaseManager;
import com.hospital.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize FlatLaf Dark Theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("FlatLaf could not be initialized. Falling back to default L&F.");
            e.printStackTrace();
        }

        // 2. Initialize Database & Tables
        DatabaseManager.initializeDatabase();

        // 3. Launch GUI in Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
