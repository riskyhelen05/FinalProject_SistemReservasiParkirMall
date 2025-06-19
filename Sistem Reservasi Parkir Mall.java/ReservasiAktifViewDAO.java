package SistemReservasiParkir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservasiAktifViewDAO {
    public static void tampilkanReservasiAktif(Connection conn) {
        String sql = "SELECT * FROM view_reservasi_aktif";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=========================================================== RESERVASI AKTIF ===========================================================");
            System.out.printf("%-10s %-20s %-12s %-10s %-15s %-8s %-25s %-20s %-10s%n",
                "ID", "Pengguna", "Plat", "Slot", "Gedung", "Lantai", "Masuk", "Keluar", "Status");

            while (rs.next()) {
                int id = rs.getInt("reservasi_id");
                String nama = rs.getString("pengguna");
                String plat = rs.getString("plat_nomor");
                String slot = rs.getString("kode_slot");
                String gedung = rs.getString("nama_gedung");
                int lantai = rs.getInt("nomor_lantai");
                String masuk = rs.getString("waktu_masuk");
                String keluar = rs.getString("waktu_keluar");
                String status = rs.getString("status_reservasi");

                System.out.printf("%-10d %-20s %-12s %-10s %-15s %-8d %-25s %-20s %-10s%n",
                    id, nama, plat, slot, gedung, lantai, masuk, keluar, status);
            }

        } catch (Exception e) {
            System.out.println("❌ Error menampilkan reservasi aktif: " + e.getMessage());
        }
    }
}
