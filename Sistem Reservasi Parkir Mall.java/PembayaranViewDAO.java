package SistemReservasiParkir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PembayaranViewDAO {
    public static void tampilkanLaporanTransaksi(Connection conn) {
        String sql = "SELECT * FROM view_laporan_transaksi_pengguna";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n================ LAPORAN TRANSAKSI PENGGUNA ================");
            System.out.printf("%-5s %-20s %-20s %-15s%n", "ID", "Nama Pengguna", "Jumlah Transaksi", "Total Bayar");

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String nama = rs.getString("nama_lengkap");
                int jumlah = rs.getInt("jumlah_transaksi");
                double total = rs.getDouble("total_bayar");

                System.out.printf("%-5d %-20s %-20d Rp%-15.2f%n", id, nama, jumlah, total);
            }

        } catch (Exception e) {
            System.out.println("❌ Error menampilkan laporan transaksi: " + e.getMessage());
        }
    }
}

