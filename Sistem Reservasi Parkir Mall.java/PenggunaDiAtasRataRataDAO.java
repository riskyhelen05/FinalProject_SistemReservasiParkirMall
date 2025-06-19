package SistemReservasiParkir;
import java.sql.*;

public class PenggunaDiAtasRataRataDAO {
    public static void tampilkanPenggunaDiAtasRata(Connection conn) {
        String query = """
            WITH total_pembayaran_per_user AS (
                SELECT u.user_id, u.nama_lengkap, SUM(p.total_bayar) AS total_bayar
                FROM pembayaran p
                JOIN log_parkir lp ON p.log_id = lp.log_id
                JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
                JOIN user u ON k.user_id = u.user_id
                GROUP BY u.user_id, u.nama_lengkap
            ),
            rata_rata AS (
                SELECT AVG(total_bayar) AS rata_rata_bayar FROM total_pembayaran_per_user
            )
            SELECT tpu.nama_lengkap, tpu.total_bayar
            FROM total_pembayaran_per_user tpu, rata_rata
            WHERE tpu.total_bayar > rata_rata.rata_rata_bayar
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            boolean adaData = false;
            System.out.println("\n-- Pengguna dengan Total Pembayaran di Atas Rata-Rata --");

            while (rs.next()) {
                adaData = true;
                PenggunaDiAtasRataRataView view = new PenggunaDiAtasRataRataView(
                    rs.getString("nama_lengkap"),
                    rs.getDouble("total_bayar")
                );
                view.tampilkan();
            }

            if (!adaData) {
                System.out.println("Tidak ada pengguna dengan total bayar di atas rata-rata.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan data pengguna di atas rata-rata: " + e.getMessage());
        }
    }
}
