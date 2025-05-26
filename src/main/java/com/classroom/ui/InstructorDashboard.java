package com.classroom.ui;

import javax.swing.*;
import java.awt.*;
import com.classroom.model.User;
import com.classroom.util.ColorScheme;
import com.classroom.util.UIUtils;
import com.classroom.ui.components.CalendarPanel;

import com.classroom.model.MakeupRequest;
import com.classroom.dao.MakeupRequestDAO;
import java.util.List;

public class InstructorDashboard extends JFrame {
    private User currentUser;
    private CalendarPanel calendarPanel;
    private JPanel profilePanel;
    
    public InstructorDashboard(User user) {
        this.currentUser = user;
        
        setTitle("Instructor Dashboard - " + user.getFullName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        // Create calendar panel to show instructor's schedule
        calendarPanel = new CalendarPanel(currentUser);
        profilePanel = createProfilePanel();
        JPanel makeupRequestPanel = createMakeupRequestPanel();
        
        tabbedPane.addTab("My Schedule", new ImageIcon(), new JScrollPane(calendarPanel), "View your teaching schedule");
        tabbedPane.addTab("Profile", new ImageIcon(), profilePanel, "View and edit your profile");
        tabbedPane.addTab("Makeup Requests", new ImageIcon(), new JScrollPane(makeupRequestPanel), "View makeup class requests");
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    private JPanel createMakeupRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Makeup Class Requests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ColorScheme.PRIMARY);

        JButton refreshButton = new JButton("Refresh");
        UIUtils.styleButton(refreshButton, ColorScheme.PRIMARY);
        refreshButton.addActionListener(e -> refreshMakeupRequests(panel));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        refreshMakeupRequests(panel); // Initial load

        return panel;
    }

    private void refreshMakeupRequests(JPanel panel) {
        // Remove existing requests display if any
        if (panel.getComponentCount() > 1) {
            panel.remove(1);
        }

        List<MakeupRequest> requests = MakeupRequestDAO.getRequestsByTeacherId(currentUser.getUserId());
        
        if (requests.isEmpty()) {
            JLabel noRequestsLabel = new JLabel("No makeup requests available");
            noRequestsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noRequestsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noRequestsLabel, BorderLayout.CENTER);
        } else {
            JPanel requestsPanel = new JPanel();
            requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
            requestsPanel.setBackground(Color.WHITE);

            for (MakeupRequest request : requests) {
                JPanel requestCard = createRequestCard(request);
                requestsPanel.add(requestCard);
                requestsPanel.add(Box.createVerticalStrut(10));
            }

            panel.add(new JScrollPane(requestsPanel), BorderLayout.CENTER);
        }

        panel.revalidate();
        panel.repaint();
    }

    private JPanel createRequestCard(MakeupRequest request) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.LIGHT_ACCENT),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Request details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.add(new JLabel("Course: " + request.getCourseCode()));
        detailsPanel.add(new JLabel("Section: " + request.getSection()));
        detailsPanel.add(new JLabel("Status: " + request.getStatus()));

        card.add(detailsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(ColorScheme.PRIMARY);

        JButton logoutButton = new JButton("Logout");
        UIUtils.styleButton(logoutButton, ColorScheme.ACCENT);
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ColorScheme.PRIMARY);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // User information fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel userInfoLabel = new JLabel("User Information");
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(userInfoLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel usernameValue = new JLabel(currentUser.getUsername());
        usernameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameValue, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Full Name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel nameValue = new JLabel(currentUser.getFullName());
        nameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(nameValue, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JLabel emailValue = new JLabel(currentUser.getEmail());
        emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(emailValue, gbc);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }
}