package com.classroom.ui;

import com.classroom.dao.CourseDAO;
import com.classroom.dao.ScheduleDAO;
import com.classroom.dao.UserDAO;
import com.classroom.dao.ResourceDAO;
import com.classroom.model.Course;
import com.classroom.model.Schedule;
import com.classroom.model.User;
import com.classroom.model.Resource;
import com.classroom.model.ScheduleResource;
import com.classroom.util.ColorScheme;
import com.classroom.util.UIUtils;
import com.classroom.util.TimeSlotUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Dialog for adding or editing a schedule.
 */
public class ScheduleDialog extends JDialog implements ActionListener {
    private Schedule schedule;
    private JComboBox<Course> courseCombo;
    private JComboBox<User> instructorCombo;
    private JComboBox<String> dayCombo;
    private JComboBox<String> startTimeCombo;
    private JComboBox<String> endTimeCombo;
    private JTextField roomField;
    private JComboBox<String> programTypeCombo;
    private JCheckBox resourcesCheckBox;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private List<Resource> availableResources;

    private static final String[] DAYS = {
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    private static final String[] TIMES = {
            "08:00", "08:50", "09:40", "10:30", "11:20", "12:10", "13:00",
            "13:50", "14:40", "15:30", "16:20", "17:10", "18:00", "18:50",
            "19:40"
    };

    public ScheduleDialog(JFrame parent, Schedule schedule) {
        super(parent, schedule == null ? "Add Schedule" : "Edit Schedule", true);
        this.schedule = schedule;

        setSize(500, 450); // Increased height for resource controls
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Course
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel courseLabel = new JLabel("Course:");
        formPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        courseCombo = new JComboBox<>();
        List<Course> courses = CourseDAO.getAllCourses();
        for (Course course : courses) {
            courseCombo.addItem(course);
        }
        UIUtils.styleComboBox(courseCombo);
        formPanel.add(courseCombo, gbc);

        // Instructor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel instructorLabel = new JLabel("Instructor:");
        formPanel.add(instructorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        instructorCombo = new JComboBox<>();
        List<User> instructors = UserDAO.getAllInstructors();
        for (User instructor : instructors) {
            instructorCombo.addItem(instructor);
        }
        UIUtils.styleComboBox(instructorCombo);
        formPanel.add(instructorCombo, gbc);

        // Day
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel dayLabel = new JLabel("Day:");
        formPanel.add(dayLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        dayCombo = new JComboBox<>(DAYS);
        UIUtils.styleComboBox(dayCombo);
        formPanel.add(dayCombo, gbc);

        // Start Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel startTimeLabel = new JLabel("Start Time:");
        formPanel.add(startTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        startTimeCombo = new JComboBox<>(TIMES);
        UIUtils.styleComboBox(startTimeCombo);
        formPanel.add(startTimeCombo, gbc);

        // End Time
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        JLabel endTimeLabel = new JLabel("End Time:");
        formPanel.add(endTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        endTimeCombo = new JComboBox<>(TIMES);
        UIUtils.styleComboBox(endTimeCombo);
        endTimeCombo.setSelectedIndex(1); // Default to 1 hour later
        formPanel.add(endTimeCombo, gbc);

        // Room
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        JLabel roomLabel = new JLabel("Room:");
        formPanel.add(roomLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        roomField = new JTextField(15);
        UIUtils.styleTextField(roomField);
        formPanel.add(roomField, gbc);

        // Program Type
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        JLabel programTypeLabel = new JLabel("Program Type:");
        formPanel.add(programTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        programTypeCombo = new JComboBox<>(new String[] { "Regular", "Evening" });
        UIUtils.styleComboBox(programTypeCombo);
        formPanel.add(programTypeCombo, gbc);

        // Resources Panel
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        JLabel resourcesLabel = new JLabel("Required Resources:");
        formPanel.add(resourcesLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        resourcesCheckBox = new JCheckBox("Projector and Connector needed");
        resourcesCheckBox.setBackground(Color.WHITE);
        formPanel.add(resourcesCheckBox, gbc);

        JPanel resourcesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        resourcesPanel.setBackground(Color.WHITE);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        formPanel.add(resourcesPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        saveButton = new JButton(schedule == null ? "Add" : "Save");
        cancelButton = new JButton("Cancel");

        UIUtils.styleButton(saveButton, ColorScheme.PRIMARY);
        UIUtils.styleButton(cancelButton, ColorScheme.SECONDARY);

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainPanel);

        // Add room field listener
        roomField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateAvailableResources();
            }
        });

        // If editing, populate fields
        if (schedule != null) {
            populateFields();
        }
    }

    private void populateFields() {
        // Set course
        for (int i = 0; i < courseCombo.getItemCount(); i++) {
            Course course = courseCombo.getItemAt(i);
            if (course.getCourseId() == schedule.getCourseId()) {
                courseCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set instructor
        for (int i = 0; i < instructorCombo.getItemCount(); i++) {
            User instructor = instructorCombo.getItemAt(i);
            if (instructor.getUserId() == schedule.getInstructorId()) {
                instructorCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set day
        for (int i = 0; i < dayCombo.getItemCount(); i++) {
            if (dayCombo.getItemAt(i).equals(schedule.getDayOfWeek())) {
                dayCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set start time
        for (int i = 0; i < startTimeCombo.getItemCount(); i++) {
            if (startTimeCombo.getItemAt(i).equals(schedule.getStartTime())) {
                startTimeCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set end time
        for (int i = 0; i < endTimeCombo.getItemCount(); i++) {
            if (endTimeCombo.getItemAt(i).equals(schedule.getEndTime())) {
                endTimeCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set room
        roomField.setText(schedule.getRoom());

        // Set program type
        if ("evening".equalsIgnoreCase(schedule.getProgramType())) {
            programTypeCombo.setSelectedIndex(1);
        } else {
            programTypeCombo.setSelectedIndex(0);
        }

        // Set resources
        if (schedule.getRequiredResources() != null) {
            for (ScheduleResource resource : schedule.getRequiredResources()) {
                Resource r = ResourceDAO.getResourceById(resource.getResourceId());
                if (r != null) {
                    // If either a projector or connector is needed, check the box
                    if (r.getResourceType().equals("Projector") || r.getResourceType().equals("Connector")) {
                        resourcesCheckBox.setSelected(true);
                        break;
                    }
                }
            }
        }

        // Update available resources
        updateAvailableResources();
    }

    private void updateAvailableResources() {
        String room = roomField.getText().trim();
        if (!room.isEmpty()) {
            availableResources = ResourceDAO.getAvailableResources(room);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            saveSchedule();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    private boolean checkResourceAvailability(String day, String startTime, String endTime) {
        // Get total resources available (10 sets of projector+connector)
        int totalResources = 10;

        // Get all schedules for the given day and time range
        List<Schedule> conflictingSchedules = ScheduleDAO.getSchedulesInTimeRange(day, startTime, endTime);
        int resourcesInUse = 0;

        for (Schedule s : conflictingSchedules) {
            // Skip current schedule if editing
            if (schedule != null && s.getScheduleId() == schedule.getScheduleId()) {
                continue;
            }
            // Check if this schedule uses resources
            List<ScheduleResource> scheduleResources = ScheduleDAO.getScheduleResources(s.getScheduleId());
            if (!scheduleResources.isEmpty()) {
                resourcesInUse++;
            }
        }

        return resourcesInUse < totalResources;
    }

    private void saveSchedule() {
        // Validate input fields
        if (!validateFields()) {
            return;
        }

        String day = (String) dayCombo.getSelectedItem();
        String startTime = (String) startTimeCombo.getSelectedItem();
        String endTime = (String) endTimeCombo.getSelectedItem();

        if (resourcesCheckBox.isSelected()) {
            if (!checkResourceAvailability(day, startTime, endTime)) {
                JOptionPane.showMessageDialog(this,
                        "No resources available for the selected time slot.",
                        "Resource Not Available",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Create schedule object
        Schedule newSchedule = new Schedule();
        if (schedule != null) {
            newSchedule.setScheduleId(schedule.getScheduleId());
        }

        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        User selectedInstructor = (User) instructorCombo.getSelectedItem();

        newSchedule.setCourseId(selectedCourse.getCourseId());
        newSchedule.setInstructorId(selectedInstructor.getUserId());
        newSchedule.setDayOfWeek((String) dayCombo.getSelectedItem());
        newSchedule.setStartTime((String) startTimeCombo.getSelectedItem());
        newSchedule.setEndTime((String) endTimeCombo.getSelectedItem());
        newSchedule.setRoom(roomField.getText().trim());
        newSchedule.setProgramType((String) programTypeCombo.getSelectedItem());

        // Validate time range
        if (!TimeSlotUtil.isValidTimeRange(newSchedule.getStartTime(), newSchedule.getEndTime())) {
            JOptionPane.showMessageDialog(this,
                    "End time must be after start time",
                    "Invalid Time Range",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check program time validity
        boolean isRegularProgram = newSchedule.getProgramType().equalsIgnoreCase("Regular");
        if (isRegularProgram && !TimeSlotUtil.isValidRegularProgramTime(
                newSchedule.getDayOfWeek(), newSchedule.getStartTime(), newSchedule.getEndTime())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid time slot for regular program\n" +
                            "Regular program runs Monday-Friday, 8:00-17:00 (excluding 12:00-13:00)",
                    "Invalid Time Slot",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (!isRegularProgram && !TimeSlotUtil.isValidEveningProgramTime(
                newSchedule.getDayOfWeek(), newSchedule.getStartTime(), newSchedule.getEndTime())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid time slot for evening program\n" +
                            "Evening program runs:\n" +
                            "Mon-Fri: 18:00-20:00\n" +
                            "Saturday: 13:00-17:00\n" +
                            "Sunday: 8:00-12:00",
                    "Invalid Time Slot",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for schedule conflicts
        String conflictDetails = ScheduleDAO.getConflictDetails(newSchedule);
        if (conflictDetails != null) {
            JOptionPane.showMessageDialog(this,
                    conflictDetails,
                    "Schedule Conflict",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create resource requirements
        List<ScheduleResource> resources = new ArrayList<>();
        if (resourcesCheckBox.isSelected()) {
            // Add both projector and connector as a set
            Resource projectorResource = findResource("Projector");
            Resource connectorResource = findResource("Connector");
            if (projectorResource != null && connectorResource != null) {
                resources.add(new ScheduleResource(0, projectorResource.getResourceId(), 1));
                resources.add(new ScheduleResource(0, connectorResource.getResourceId(), 1));
            }
        }

        // Save schedule with resources
        boolean success = schedule == null ? ScheduleDAO.addScheduleWithResources(newSchedule, resources)
                : ScheduleDAO.updateScheduleWithResources(newSchedule, resources);

        if (success) {
            confirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save schedule",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Resource findResource(String type) {
        if (availableResources != null) {
            for (Resource resource : availableResources) {
                if (resource.getResourceType().equals(type)) {
                    return resource;
                }
            }
        }
        return null;
    }

    private boolean validateFields() {
        // Validate course selection
        if (courseCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate instructor selection
        if (instructorCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select an instructor",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate room
        String room = roomField.getText().trim();
        if (room.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a room number",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate time selection
        String startTime = (String) startTimeCombo.getSelectedItem();
        String endTime = (String) endTimeCombo.getSelectedItem();
        if (startTime == null || endTime == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both start and end times",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate day selection
        if (dayCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a day",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate program type
        if (programTypeCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a program type",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}