package com.classroom.dao;

import com.classroom.model.MakeupRequest;
import com.classroom.util.DatabaseUtil;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MakeupRequestDAO {
    public static boolean createRequest(MakeupRequest request) {
        String sql = "INSERT INTO makeup_requests (student_id, course_code, section, teacher_id, is_class_rep, status) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getStudentId());
            pstmt.setString(2, request.getCourseCode());
            pstmt.setString(3, request.getSection());
            pstmt.setInt(4, request.getTeacherId());
            pstmt.setBoolean(5, request.isClassRep());
            pstmt.setString(6, request.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<MakeupRequest> getRequestsByTeacherId(int teacherId) {
        List<MakeupRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM makeup_requests WHERE teacher_id = ?;";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MakeupRequest request = new MakeupRequest(
                    rs.getInt("student_id"),
                    rs.getString("course_code"),
                    rs.getString("section"),
                    rs.getInt("teacher_id"),
                    rs.getBoolean("is_class_rep")
                );
                request.setRequestId(rs.getInt("request_id"));
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
}