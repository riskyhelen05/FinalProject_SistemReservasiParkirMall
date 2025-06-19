// ProsesReservasi.java
package SistemReservasiParkir;

import java.sql.*;
import java.util.Scanner;

public class ProsesReservasi {
    // Untuk admin tanpa userId
    public static void tampilkanMenuReservasi(Connection conn, Scanner scanner) {
        System.out.println("\nReservasi hanya dapat dilakukan oleh pengguna setelah login.");
    }

    // Untuk pengguna
    public static void tampilkanMenuReservasi(Connection conn, Scanner scanner, int userId) {
        try {
            System.out.println("\n===== Buat Reservasi Parkir =====");
            System.out.print("Masukkan plat nomor kendaraan Anda: ");
            String plat = scanner.nextLine();

            int kendaraanId = -1;
            String sqlCek = "SELECT kendaraan_id FROM kendaraan WHERE user_id = ? AND plat_nomor = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCek)) {
                ps.setInt(1, userId);
                ps.setString(2, plat);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    kendaraanId = rs.getInt("kendaraan_id");
                } else {
                    // Kendaraan belum ada, tawarkan untuk mendaftar
                    System.out.println("Kendaraan belum terdaftar. Daftarkan kendaraan baru.");
                    System.out.print("Tipe kendaraan (mobil/motor/electric_vehicle): ");
                    String tipe = scanner.nextLine();
                    System.out.print("Merk: ");
                    String merk = scanner.nextLine();
                    System.out.print("Warna: ");
                    String warna = scanner.nextLine();

                    String insertKendaraan = "INSERT INTO kendaraan (user_id, plat_nomor, tipe_kendaraan, merk, warna) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement ps2 = conn.prepareStatement(insertKendaraan, Statement.RETURN_GENERATED_KEYS)) {
                        ps2.setInt(1, userId);
                        ps2.setString(2, plat);
                        ps2.setString(3, tipe);
                        ps2.setString(4, merk);
                        ps2.setString(5, warna);
                        ps2.executeUpdate();

                        ResultSet keys = ps2.getGeneratedKeys();
                        if (keys.next()) kendaraanId = keys.getInt(1);
                    }
                }
            }

            // Tampilkan slot kosong
            String sqlSlot = "SELECT sp.slot_id, sp.kode_slot, g.nama_gedung, lg.nomor_lantai " +
                    "FROM slot_parkir sp JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                    "JOIN gedung g ON lg.gedung_id = g.gedung_id WHERE sp.status_slot = 'kosong'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSlot);
            System.out.println("\nSlot Kosong yang Tersedia:");
            while (rs.next()) {
                System.out.printf("ID: %d | Slot: %s | Gedung: %s | Lantai: %d\n",
                        rs.getInt("slot_id"), rs.getString("kode_slot"),
                        rs.getString("nama_gedung"), rs.getInt("nomor_lantai"));
            }

            System.out.print("\nPilih ID slot yang ingin Anda reservasi: ");
            int slotId = scanner.nextInt();
            scanner.nextLine();

            // Pastikan slot masih tersedia
            String cekSlot = "SELECT COUNT(*) FROM slot_parkir WHERE slot_id = ? AND status_slot = 'kosong'";
            try (PreparedStatement cek = conn.prepareStatement(cekSlot)) {
                cek.setInt(1, slotId);
                ResultSet rslot = cek.executeQuery();
                if (rslot.next() && rslot.getInt(1) == 0) {
                    System.out.println("Slot tidak tersedia atau sudah dibooking.");
                    return;
                }
            }

            System.out.print("Masukkan tanggal reservasi (YYYY-MM-DD): ");
            String tanggal = scanner.nextLine();
            System.out.print("Masukkan waktu masuk (YYYY-MM-DD HH:MM:SS): ");
            String waktuMasuk = scanner.nextLine();
            System.out.print("Masukkan waktu keluar (YYYY-MM-DD HH:MM:SS) atau kosong jika belum tahu: ");
            String waktuKeluar = scanner.nextLine();

            String sql = "INSERT INTO reservasi_parkir (user_id, slot_id, kendaraan_id, tanggal_reservasi, waktu_masuk, waktu_keluar, status_reservasi) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'aktif')";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, slotId);
                ps.setInt(3, kendaraanId);
                ps.setString(4, tanggal);
                ps.setString(5, waktuMasuk);
                if (waktuKeluar.isEmpty()) {
                    ps.setNull(6, Types.TIMESTAMP);
                } else {
                    ps.setString(6, waktuKeluar);
                }
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    System.out.println("Reservasi berhasil dibuat.");
                } else {
                    System.out.println("Reservasi gagal.");
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat membuat reservasi: " + e.getMessage());
        }
    }
}
