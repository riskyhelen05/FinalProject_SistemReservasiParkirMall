package SistemReservasiParkir;

import java.sql.*;
import java.util.Scanner;

public class ReservasiPenggunaDAO {

    public static void tampilkanReservasiSaya(Connection conn, int userId) {
        String sql = "SELECT * FROM view_reservasi_aktif WHERE pengguna = (SELECT nama_lengkap FROM user WHERE user_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            boolean adaData = false;
            System.out.println("\n-- Reservasi Anda --");
            while (rs.next()) {
                adaData = true;
                System.out.printf("ID: %d | Slot: %s | Gedung: %s | Masuk: %s | Keluar: %s | Status: %s\n",
                        rs.getInt("reservasi_id"), rs.getString("kode_slot"), rs.getString("nama_gedung"),
                        rs.getTimestamp("waktu_masuk"), rs.getTimestamp("waktu_keluar"), rs.getString("status_reservasi"));
            }
            if (!adaData) {
                System.out.println("Tidak ada reservasi aktif yang ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan reservasi: " + e.getMessage());
        }
    }

    public static void batalkanReservasi(Connection conn, Scanner scanner, int userId) {
        try {
            System.out.print("Masukkan ID Reservasi yang ingin dibatalkan: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "UPDATE reservasi_parkir SET status_reservasi = 'batal' WHERE reservasi_id = ? AND user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, userId);
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    System.out.println("Reservasi berhasil dibatalkan.");
                } else {
                    System.out.println("Reservasi gagal dibatalkan atau tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal membatalkan reservasi: " + e.getMessage());
        } catch (Exception ex) {
            System.out.println("❌ Input tidak valid. Pastikan ID reservasi berupa angka.");
            scanner.nextLine(); // Bersihkan input buffer
        }
    }
}
