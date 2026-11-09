/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.repository;

import pbo.sewaalathiking.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ASUS
 */
public class UserRepository {
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, email);
            pstmt.setString(2, password); // Pada sistem nyata biasanya di-hash, tapi untuk tugas ini plaintext cukup
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal melakukan login: " + e.getMessage());
        }
        return null; // Kembalikan null jika email/password salah
    }
    
    public boolean register(User user) {
        String query = "INSERT INTO users (role, name, phone, email, password) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, user.getRole());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Gagal mendaftar: " + e.getMessage());
            return false;
        }
    }
}
