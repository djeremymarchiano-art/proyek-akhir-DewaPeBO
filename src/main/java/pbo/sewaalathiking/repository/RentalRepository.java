/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.repository;

import pbo.sewaalathiking.entity.RentalDetail;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import pbo.sewaalathiking.entity.RentalTransaction;
import java.time.Duration;

/**
 *
 * @author ASUS
 */
public class RentalRepository {
    public static final double PENALTY_PER_HOUR = 5000.0;

    public static double calculatePenalty(LocalDateTime expectedReturn, LocalDateTime checkTime) {
        if (checkTime.isAfter(expectedReturn)) {
            long lateMinutes = Duration.between(expectedReturn, checkTime).toMinutes();
            if (lateMinutes > 0) {
                long lateHours = (lateMinutes + 59) / 60; // Pembulatan ke atas per jam
                return lateHours * PENALTY_PER_HOUR;
            }
        }
        return 0.0;
    }
    
    public List<RentalTransaction> getRentalsByUserId(int userId) {
        List<RentalTransaction> list = new ArrayList<>();
        String query = "SELECT * FROM rental_transactions WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RentalTransaction tx = new RentalTransaction();
                    tx.setId(rs.getInt("id"));
                    tx.setUserId(rs.getInt("user_id"));
                    tx.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                    tx.setExpectedReturnDate(rs.getTimestamp("expected_return_date").toLocalDateTime());

                    Timestamp actualTs = rs.getTimestamp("actual_return_date");
                    if (actualTs != null) {
                        tx.setActualReturnDate(actualTs.toLocalDateTime());
                    }

                    tx.setTotalCost(rs.getDouble("total_cost"));
                    tx.setStatus(rs.getString("status"));

                    // Jika status masih ACTIVE, hitung estimasi denda berjalan saat ini
                    if ("ACTIVE".equalsIgnoreCase(tx.getStatus())) {
                        tx.setPenaltyFee(calculatePenalty(tx.getExpectedReturnDate(), LocalDateTime.now()));
                    } else {
                        tx.setPenaltyFee(rs.getDouble("penalty_fee"));
                    }

                    list.add(tx);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil riwayat sewa: " + e.getMessage());
        }
        return list;
    }
    
    public boolean createRental(int userId, String customerName, int durationDays, List<RentalDetail> details) {
        String insertTxSql = "INSERT INTO rental_transactions (user_id, start_date, expected_return_date, total_cost, status) VALUES (?, ?, ?, ?, 'ACTIVE')";
        String insertDetailSql = "INSERT INTO rental_details (transaction_id, equipment_id, quantity, subtotal_price) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE equipment SET available_stock = available_stock - ? WHERE id = ? AND available_stock >= ?";
        String insertNotifSql = "INSERT INTO notifications (user_id, message, scheduled_time) VALUES (?, ?, ?)";

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime expectedReturn = startDate.plusDays(durationDays);

        double totalCost = 0;
        for (RentalDetail d : details) {
            totalCost += d.getSubtotalPrice();
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. Simpan Header Transaksi Sewa
                int transactionId = 0;
                try (PreparedStatement pstmt = conn.prepareStatement(insertTxSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, userId);
                    pstmt.setTimestamp(2, Timestamp.valueOf(startDate));
                    pstmt.setTimestamp(3, Timestamp.valueOf(expectedReturn));
                    pstmt.setDouble(4, totalCost);
                    pstmt.executeUpdate();

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            transactionId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Gagal mendapatkan ID transaksi.");
                        }
                    }
                }

                // 2. Simpan Rincian Barang & Kurangi Stok Tersedia
                try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql);
                     PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {

                    for (RentalDetail item : details) {
                        // Kurangi stok dulu untuk memastikan stok cukup
                        stockStmt.setInt(1, item.getQuantity());
                        stockStmt.setInt(2, item.getEquipmentId());
                        stockStmt.setInt(3, item.getQuantity());
                        int updated = stockStmt.executeUpdate();
                        if (updated == 0) {
                            throw new SQLException("Stok tidak mencukupi untuk ID Alat: " + item.getEquipmentId());
                        }

                        // Simpan detail sewa
                        detailStmt.setInt(1, transactionId);
                        detailStmt.setInt(2, item.getEquipmentId());
                        detailStmt.setInt(3, item.getQuantity());
                        detailStmt.setDouble(4, item.getSubtotalPrice());
                        detailStmt.executeUpdate();
                    }
                }

                // 3. Buat Jadwal Notifikasi Otomatis (H-1 Hari, H-30 Menit, H-10 Menit)
                // Untuk Customer dan Semua Admin sekaligus
                List<Integer> adminIds = getAdminIds(conn);
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String batasWaktuStr = expectedReturn.format(fmt);

                LocalDateTime[] jadwalNotif = {
                    expectedReturn.minusDays(1),
                    expectedReturn.minusMinutes(30),
                    expectedReturn.minusMinutes(10)
                };
                String[] labelWaktu = {
                    "H-1 Hari",
                    "H-30 Menit",
                    "H-10 Menit"
                };

                try (PreparedStatement notifStmt = conn.prepareStatement(insertNotifSql)) {
                    for (int i = 0; i < jadwalNotif.length; i++) {
                        // Notifikasi untuk Penyewa
                        String msgCustomer = "[" + labelWaktu[i] + "] Pengingat Pengembalian Transaksi #" + transactionId +
                                             ": Harap kembalikan alat sebelum " + batasWaktuStr + " agar terhindar dari denda per jam.";
                        notifStmt.setInt(1, userId);
                        notifStmt.setString(2, msgCustomer);
                        notifStmt.setTimestamp(3, Timestamp.valueOf(jadwalNotif[i]));
                        notifStmt.executeUpdate();

                        // Notifikasi untuk Pemilik Toko (Admin)
                        for (int adminId : adminIds) {
                            String msgAdmin = "[" + labelWaktu[i] + "] Jadwal Kembali Transaksi #" + transactionId +
                                              " atas nama " + customerName + " pada " + batasWaktuStr + ".";
                            notifStmt.setInt(1, adminId);
                            notifStmt.setString(2, msgAdmin);
                            notifStmt.setTimestamp(3, Timestamp.valueOf(jadwalNotif[i]));
                            notifStmt.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Transaksi sewa dibatalkan: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Koneksi error saat transaksi sewa: " + e.getMessage());
            return false;
        }
    }

    private List<Integer> getAdminIds(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM users WHERE role = 'ADMIN'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        }
        return ids;
    }
}
