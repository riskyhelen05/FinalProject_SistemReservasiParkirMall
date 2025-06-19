package SistemReservasiParkir;
import java.sql.*;

public class LaporanSlotPerLantaiDAO {
    public static void tampilkanLaporanSlot(Connection conn) {
        String query = "SELECT g.nama_gedung, lg.nomor_lantai, COUNT(*) AS total_slot, " +
                       "SUM(sp.status_slot = 'kosong') AS jumlah_kosong, " +
                       "SUM(sp.status_slot = 'terisi') AS jumlah_terisi, " +
                       "SUM(sp.status_slot = 'reservasi') AS jumlah_reservasi " +
                       "FROM slot_parkir sp " +
                       "JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                       "JOIN gedung g ON lg.gedung_id = g.gedung_id " +
                       "GROUP BY g.nama_gedung, lg.nomor_lantai";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n-- LAPORAN SLOT PER LANTAI --");
            while (rs.next()) {
                LaporanSlotPerLantaiView view = new LaporanSlotPerLantaiView(
                    rs.getString("nama_gedung"),
                    rs.getInt("nomor_lantai"),
                    rs.getInt("total_slot"),
                    rs.getInt("jumlah_kosong"),
                    rs.getInt("jumlah_terisi"),
                    rs.getInt("jumlah_reservasi")
                );
                view.tampilkan();
            }

        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan laporan slot per lantai: " + e.getMessage());
        }
    }
}
