package SistemReservasiParkir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogParkirViewDAO {
    public static void tampilkanLogParkir(Connection conn) {
        String sql = "SELECT * FROM view_log_parkir_lengkap";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=================================================================== LOG PARKIR LENGKAP ===================================================================");
            System.out.printf("%-5s %-15s %-15s %-8s %-15s %-10s %-10s %-20s %-20s %-15s %-10s%n",
                "ID", "Pengguna", "Plat", "Tipe", "Gedung", "Lantai", "Slot", "Masuk", "Keluar", "Bayar", "Metode");

            while (rs.next()) {
                int id = rs.getInt("log_id");
                String pengguna = rs.getString("pengguna");
                String plat = rs.getString("plat_nomor");
                String tipe = rs.getString("tipe_kendaraan");
                String gedung = rs.getString("nama_gedung");
                int lantai = rs.getInt("nomor_lantai");
                String slot = rs.getString("kode_slot");
                String masuk = rs.getString("waktu_masuk");
                String keluar = rs.getString("waktu_keluar");
                double bayar = rs.getDouble("total_bayar");
                String metode = rs.getString("metode_pembayaran");

                System.out.printf("%-5d %-15s %-15s %-8s %-15s %-10d %-10s %-20s %-20s %-15.2f %-10s%n",
                    id, pengguna, plat, tipe, gedung, lantai, slot, masuk, keluar != null ? keluar : "-", bayar, metode != null ? metode : "-");
            }

        } catch (Exception e) {
            System.out.println("❌ Error menampilkan log parkir: " + e.getMessage());
        }
    }
}
