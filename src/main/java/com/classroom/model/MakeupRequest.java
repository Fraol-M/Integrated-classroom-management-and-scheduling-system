package com.classroom.model;

public class MakeupRequest {
    private int requestId;
    private int studentId;
    private String courseCode;
    private String section;
    private String status; // PENDING, APPROVED, REJECTED
    private int teacherId;
    private boolean isClassRep;
    
    // Constructor
    public MakeupRequest(int studentId, String courseCode, String section, int teacherId, boolean isClassRep) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.section = section;
        this.teacherId = teacherId;
        this.isClassRep = isClassRep;
        this.status = "PENDING";
    }
    
    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public boolean isClassRep() { return isClassRep; }
    public void setClassRep(boolean classRep) { isClassRep = classRep; }
}