package SistemReservasiParkir;

import java.sql.*;

public class RiwayatParkirDAO {
    public static void tampilkanRiwayatPengguna(Connection conn, int userId) {
        String sql = "SELECT * FROM view_log_parkir_lengkap WHERE pengguna = (SELECT nama_lengkap FROM user WHERE user_id = ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            boolean adaData = false;
            System.out.println("\n-- Riwayat Parkir Anda --");
            while (rs.next()) {
                adaData = true;
                System.out.printf("LogID: %d | Slot: %s | Masuk: %s | Keluar: %s | Bayar: Rp%.2f | Metode: %s\n",
                        rs.getInt("log_id"), rs.getString("kode_slot"), rs.getTimestamp("waktu_masuk"),
                        rs.getTimestamp("waktu_keluar"), rs.getDouble("total_bayar"), rs.getString("metode_pembayaran"));
            }
            if (!adaData) {
                System.out.println("Tidak ada riwayat parkir yang ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan riwayat parkir: " + e.getMessage());
        }
    }
}
