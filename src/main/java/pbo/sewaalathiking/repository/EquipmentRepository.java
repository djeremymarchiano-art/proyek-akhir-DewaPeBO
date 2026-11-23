/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.repository;

import pbo.sewaalathiking.entity.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class EquipmentRepository {
    public List<Equipment> getAllEquipment() {
        List<Equipment> list = new ArrayList<>();
        String query = "SELECT * FROM equipment ORDER BY id ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Equipment(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("total_stock"),
                    rs.getInt("available_stock")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data alat: " + e.getMessage());
        }
        return list;
    }
    public List<Equipment> getAvailableEquipment() {
        List<Equipment> list = new ArrayList<>();
        String query = "SELECT * FROM equipment WHERE available_stock > 0 ORDER BY category, name";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Equipment(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price_per_day"),
                    rs.getInt("total_stock"),
                    rs.getInt("available_stock")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil alat tersedia: " + e.getMessage());
        }
        return list;
    }
    // 1. Tambah Alat Baru (Stok tersedia awal sama dengan total stok)
    public boolean addEquipment(Equipment eq) {
        String query = "INSERT INTO equipment (name, category, price_per_day, total_stock, available_stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, eq.getName());
            pstmt.setString(2, eq.getCategory());
            pstmt.setDouble(3, eq.getPricePerDay());
            pstmt.setInt(4, eq.getTotalStock());
            pstmt.setInt(5, eq.getAvailableStock());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menambah alat: " + e.getMessage());
            return false;
        }
    }

    // 2. Update Data Alat (Nama, Kategori, Harga, Total Stok, & Stok Tersedia)
    public boolean updateEquipment(Equipment eq) {
        String query = "UPDATE equipment SET name = ?, category = ?, price_per_day = ?, total_stock = ?, available_stock = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, eq.getName());
            pstmt.setString(2, eq.getCategory());
            pstmt.setDouble(3, eq.getPricePerDay());
            pstmt.setInt(4, eq.getTotalStock());
            pstmt.setInt(5, eq.getAvailableStock());
            pstmt.setInt(6, eq.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal mengubah data alat: " + e.getMessage());
            return false;
        }
    }

    // 3. Hapus Alat berdasarkan ID
    public boolean deleteEquipment(int id) {
        String query = "DELETE FROM equipment WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus alat: " + e.getMessage());
            return false;
        }
    }
}
