package SistemReservasiParkir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KendaraanBelumKeluarDAO {
    public static void tampilkanKendaraanBelumKeluar(Connection conn) {
        String sql = "SELECT * FROM vw_parkir_aktif_belum_bayar";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n-- Kendaraan Belum Keluar / Belum Bayar --");
            boolean ada = false;
            while (rs.next()) {
                ada = true;
                System.out.printf("Plat: %s | Pemilik: %s | Masuk: %s\n",
                    rs.getString("plat_nomor"), rs.getString("nama_lengkap"),
                    rs.getTimestamp("waktu_masuk"));
            }

            if (!ada) {
                System.out.println("✅ Tidak ada kendaraan yang belum keluar atau belum bayar.");
            }

        } catch (Exception e) {
            System.out.println("❌ Gagal menampilkan data kendaraan: " + e.getMessage());
        }
    }
}
