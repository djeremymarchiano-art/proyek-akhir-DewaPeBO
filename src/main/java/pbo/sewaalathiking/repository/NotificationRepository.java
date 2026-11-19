/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.repository;

import pbo.sewaalathiking.entity.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class NotificationRepository {
    public List<Notification> getActiveNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id = ? AND scheduled_time <= NOW() ORDER BY scheduled_time DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notif = new Notification();
                    notif.setId(rs.getInt("id"));
                    notif.setUserId(rs.getInt("user_id"));
                    notif.setMessage(rs.getString("message"));
                    notif.setRead(rs.getBoolean("is_read"));
                    notif.setScheduledTime(rs.getTimestamp("scheduled_time").toLocalDateTime());
                    list.add(notif);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil notifikasi: " + e.getMessage());
        }
        return list;
    }
    public boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update status notifikasi: " + e.getMessage());
            return false;
        }
    }
    public boolean markAllAsRead(int userId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND scheduled_time <= NOW()";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update semua notifikasi: " + e.getMessage());
            return false;
        }
    }
}
