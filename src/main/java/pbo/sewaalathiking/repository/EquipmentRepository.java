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
}
