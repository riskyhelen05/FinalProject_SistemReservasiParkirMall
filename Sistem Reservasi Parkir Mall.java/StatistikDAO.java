package SistemReservasiParkir;
import java.sql.*;

public class StatistikDAO {

    public static void tampilkanStatistikHarian(Connection conn) {
        String sql = "CALL laporan_statistik_harian()";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n-- Statistik Harian --");
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                System.out.printf("Tanggal: %s | Transaksi: %d | Total: Rp%.2f\n",
                    rs.getDate("tanggal"), rs.getInt("jumlah_transaksi"), rs.getDouble("total_pendapatan"));
            }
            if (!adaData) {
                System.out.println("Tidak ada data statistik harian.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan statistik harian: " + e.getMessage());
        }
    }

    public static void tampilkanStatistikBulanan(Connection conn) {
        String sql = "CALL laporan_statistik_bulanan()";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n-- Statistik Bulanan --");
            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                System.out.printf("Bulan: %d-%d | Transaksi: %d | Total: Rp%.2f\n",
                    rs.getInt("bulan"), rs.getInt("tahun"), rs.getInt("jumlah_transaksi"), rs.getDouble("total_pendapatan"));
            }
            if (!adaData) {
                System.out.println("Tidak ada data statistik bulanan.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal menampilkan statistik bulanan: " + e.getMessage());
        }
    }
}
