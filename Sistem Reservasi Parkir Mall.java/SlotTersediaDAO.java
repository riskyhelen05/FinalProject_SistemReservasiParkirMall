package SistemReservasiParkir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SlotTersediaDAO {
    public static void tampilkanSlotTersedia(Connection conn) {
        String sql = "SELECT * FROM view_status_slot_per_lantai";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=================== STATUS SLOT PARKIR PER LANTAI ===================");
            System.out.printf("%-15s %-10s %-10s %-10s %-10s %-10s%n",
                "Gedung", "Lantai", "Total", "Kosong", "Terisi", "Reservasi");

            while (rs.next()) {
                String gedung = rs.getString("nama_gedung");
                int lantai = rs.getInt("nomor_lantai");
                int total = rs.getInt("total_slot");
                int kosong = rs.getInt("jumlah_kosong");
                int terisi = rs.getInt("jumlah_terisi");
                int reservasi = rs.getInt("jumlah_reservasi");

                System.out.printf("%-15s %-10d %-10d %-10d %-10d %-10d%n",
                    gedung, lantai, total, kosong, terisi, reservasi);
            }

        } catch (Exception e) {
            System.out.println("❌ Error menampilkan slot: " + e.getMessage());
        }
    }
}
