// Main.java
package SistemReservasiParkir;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("\u2705 Koneksi ke database berhasil!");

                Scanner scanner = new Scanner(System.in);
                int pilihan;

                do {
                    System.out.println("\n========== MENU UTAMA ==========\n");
                    System.out.println("1. Login");
                    System.out.println("2. Daftar Pengguna Baru");
                    System.out.println("3. Keluar");
                    System.out.print("Pilih menu: ");
                    pilihan = scanner.nextInt();
                    scanner.nextLine();

                    switch (pilihan) {
                        case 1 -> loginMenu(conn, scanner);
                        case 2 -> RegistrasiPenggunaDAO.daftarPenggunaBaru(conn, scanner);
                        case 3 -> System.out.println("Terima kasih telah menggunakan sistem.");
                        default -> System.out.println("Pilihan tidak valid.");
                    }
                } while (pilihan != 3);

                scanner.close();
            } else {
                System.out.println("\u274C Koneksi database gagal. Periksa konfigurasi.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    public static void loginMenu(Connection conn, Scanner scanner) {
        try {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("\nLogin berhasil sebagai " + role);
                if ("admin".equalsIgnoreCase(role)) {
                    menuAdmin(conn, scanner, rs.getInt("user_id"));
                } else {
                    menuPengguna(conn, scanner, rs.getInt("user_id"));
                }
            } else {
                System.out.println("Login gagal. Periksa email atau password.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat login: " + e.getMessage());
        }
    }

    public static void menuAdmin(Connection conn, Scanner scanner, int userId) {
        int pilihan;
        do {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Ganti Password & Edit Profil");
            System.out.println("2. Mulai Parkir dari Reservasi");
            System.out.println("3. Proses Pembayaran Manual");
            System.out.println("4. Tampilkan View");
            System.out.println("5. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (pilihan) {
                    case 1 -> RegistrasiPenggunaDAO.editProfilDanPassword(conn, scanner, userId);
                    case 2 -> {
                        tampilkanReservasiBelumMulai(conn);
                        System.out.print("Masukkan ID Reservasi: ");
                        int resId = scanner.nextInt(); scanner.nextLine();
                        tampilkanDetailReservasi(conn, resId);
                        if (isSlotTerisi(conn, resId)) {
                            System.out.println("Slot sudah terisi. Silakan pilih slot lain.");
                            gantiSlotReservasi(conn, scanner, resId);
                        }
                        CallableStatement cs = conn.prepareCall("CALL sp_mulai_parkir_dari_reservasi(?)");
                        cs.setInt(1, resId);
                        cs.execute();
                        System.out.println("Reservasi telah dimulai dan slot diperbarui.");
                    }
                    case 3 -> {
                        System.out.print("Masukkan ID Log Parkir: ");
                        int logId = scanner.nextInt(); scanner.nextLine();
                        tampilkanDetailLog(conn, logId);
                        System.out.print("Metode pembayaran (cash/e-wallet/debit): ");
                        String metode = scanner.nextLine();
                        CallableStatement bayar = conn.prepareCall("CALL sp_proses_pembayaran(?, ?)");
                        bayar.setInt(1, logId); bayar.setString(2, metode); bayar.execute();
                        System.out.println("Pembayaran berhasil diproses.");
                    }
                    case 4 -> menuViewAdmin(conn, scanner);
                    case 5 -> System.out.println("Kembali ke menu utama.");
                    default -> System.out.println("Pilihan tidak valid.");
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        } while (pilihan != 5);
    }

    public static void menuViewAdmin(Connection conn, Scanner scanner) {
    int pilihan;
    do {
        System.out.println("\n===== TAMPILKAN VIEW =====");
        System.out.println("1. Tampilkan Slot Parkir Tersedia");
        System.out.println("2. Tampilkan Reservasi Aktif");
        System.out.println("3. Tampilkan Log Parkir Lengkap");
        System.out.println("4. Laporan Transaksi Pengguna");
        System.out.println("5. Laporan Slot Crosstab per Lantai");
        System.out.println("6. Pengguna dengan Total Bayar > Rata-rata");
        System.out.println("7. Kendaraan Belum Keluar / Belum Bayar");
        System.out.println("8. Statistik Harian");
        System.out.println("9. Statistik Bulanan");
        System.out.println("10. Kembali");
        System.out.print("Pilih menu: ");
        pilihan = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (pilihan) {
                case 1 -> SlotTersediaDAO.tampilkanSlotTersedia(conn);
                case 2 -> ReservasiAktifViewDAO.tampilkanReservasiAktif(conn);
                case 3 -> LogParkirViewDAO.tampilkanLogParkir(conn);
                case 4 -> PembayaranViewDAO.tampilkanLaporanTransaksi(conn);
                case 5 -> LaporanSlotPerLantaiDAO.tampilkanLaporanSlot(conn);
                case 6 -> PenggunaDiAtasRataRataDAO.tampilkanPenggunaDiAtasRata(conn);
                case 7 -> KendaraanBelumKeluarDAO.tampilkanKendaraanBelumKeluar(conn);
                case 8 -> StatistikDAO.tampilkanStatistikHarian(conn);
                case 9 -> StatistikDAO.tampilkanStatistikBulanan(conn);
                case 10 -> System.out.println("Kembali ke menu admin.");
                default -> System.out.println("Pilihan tidak valid.");
            }
        } catch (Exception e) {
            System.out.println("Gagal menampilkan view: " + e.getMessage());
        }
    } while (pilihan != 10);
}

    public static void menuPengguna(Connection conn, Scanner scanner, int userId) {
        int pilihan;
        do {
            System.out.println("\n===== MENU PENGGUNA =====");
            System.out.println("1. Ganti Password & Edit Profil");
            System.out.println("2. Lihat Slot Parkir Tersedia");
            System.out.println("3. Buat Reservasi");
            System.out.println("4. Lihat Reservasi Saya");
            System.out.println("5. Batalkan Reservasi");
            System.out.println("6. Mulai Parkir dari Reservasi");
            System.out.println("7. Proses Pembayaran");
            System.out.println("8. Riwayat Parkir dan Pembayaran");
            System.out.println("9. Kembali ke Menu Utama");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (pilihan) {
                    case 1 -> RegistrasiPenggunaDAO.editProfilDanPassword(conn, scanner, userId);
                    case 2 -> SlotTersediaDAO.tampilkanSlotTersedia(conn);
                    case 3 -> ProsesReservasi.tampilkanMenuReservasi(conn, scanner, userId);
                    case 4 -> ReservasiPenggunaDAO.tampilkanReservasiSaya(conn, userId);
                    case 5 -> ReservasiPenggunaDAO.batalkanReservasi(conn, scanner, userId);
                    case 6 -> {
                        tampilkanReservasiBelumMulai(conn);
                        System.out.print("Masukkan ID Reservasi: ");
                        int resId = scanner.nextInt(); scanner.nextLine();
                        tampilkanDetailReservasi(conn, resId);
                        if (isSlotTerisi(conn, resId)) {
                            System.out.println("Slot sudah terisi. Silakan pilih slot lain.");
                            gantiSlotReservasi(conn, scanner, resId);
                        }
                        CallableStatement cs = conn.prepareCall("CALL sp_mulai_parkir_dari_reservasi(?)");
                        cs.setInt(1, resId); cs.execute();
                        System.out.println("Reservasi dimulai.");
                    }
                    case 7 -> {
                        System.out.print("Masukkan ID Log Parkir: ");
                        int logId = scanner.nextInt(); scanner.nextLine();
                        tampilkanDetailLog(conn, logId);
                        System.out.print("Metode pembayaran (cash/e-wallet/debit): ");
                        String metode = scanner.nextLine();
                        CallableStatement bayar = conn.prepareCall("CALL sp_proses_pembayaran(?, ?)");
                        bayar.setInt(1, logId); bayar.setString(2, metode); bayar.execute();
                        System.out.println("Pembayaran berhasil.");
                    }
                    case 8 -> RiwayatParkirDAO.tampilkanRiwayatPengguna(conn, userId);
                    case 9 -> System.out.println("Kembali ke menu utama.");
                    default -> System.out.println("Pilihan tidak valid.");
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        } while (pilihan != 9);
    }

    public static void tampilkanReservasiBelumMulai(Connection conn) throws SQLException {
        String sql = "SELECT r.reservasi_id, u.nama_lengkap, k.plat_nomor, k.merk, sp.kode_slot, g.nama_gedung, lg.nomor_lantai, r.waktu_masuk " +
                "FROM reservasi_parkir r " +
                "JOIN user u ON r.user_id = u.user_id " +
                "JOIN kendaraan k ON r.kendaraan_id = k.kendaraan_id " +
                "JOIN slot_parkir sp ON r.slot_id = sp.slot_id " +
                "JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                "JOIN gedung g ON lg.gedung_id = g.gedung_id " +
                "WHERE r.status_reservasi = 'aktif'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println("\n-- Daftar Reservasi Aktif --");
            while (rs.next()) {
                System.out.printf("ID: %d | Pengguna: %s | Kendaraan: %s %s | Slot: %s | Gedung: %s | Lantai: %d | Masuk: %s\n",
                        rs.getInt("reservasi_id"), rs.getString("nama_lengkap"), rs.getString("merk"),
                        rs.getString("plat_nomor"), rs.getString("kode_slot"), rs.getString("nama_gedung"),
                        rs.getInt("nomor_lantai"), rs.getTimestamp("waktu_masuk"));
            }
        }
    }

    public static boolean isSlotTerisi(Connection conn, int reservasiId) throws SQLException {
        String sql = "SELECT sp.status_slot FROM reservasi_parkir rp JOIN slot_parkir sp ON rp.slot_id = sp.slot_id WHERE rp.reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getString("status_slot").equalsIgnoreCase("terisi");
        }
    }

    public static void gantiSlotReservasi(Connection conn, Scanner scanner, int reservasiId) throws SQLException {
        tampilkanSlotKosong(conn);
        System.out.print("Masukkan ID slot pengganti: ");
        int slotBaru = scanner.nextInt(); scanner.nextLine();

        String update = "UPDATE reservasi_parkir SET slot_id = ? WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setInt(1, slotBaru);
            ps.setInt(2, reservasiId);
            int hasil = ps.executeUpdate();
            if (hasil > 0) {
                System.out.println("Slot berhasil diganti ke ID: " + slotBaru);
            } else {
                System.out.println("Gagal mengganti slot.");
            }
        }
    }

    public static void tampilkanSlotKosong(Connection conn) throws SQLException {
        String sql = "SELECT sp.slot_id, sp.kode_slot, g.nama_gedung, lg.nomor_lantai FROM slot_parkir sp " +
                "JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                "JOIN gedung g ON lg.gedung_id = g.gedung_id WHERE sp.status_slot = 'kosong'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println("\n-- Daftar Slot Kosong --");
            while (rs.next()) {
                System.out.printf("ID: %d | Slot: %s | Gedung: %s | Lantai: %d\n",
                        rs.getInt("slot_id"), rs.getString("kode_slot"),
                        rs.getString("nama_gedung"), rs.getInt("nomor_lantai"));
            }
        }
    }

    public static void tampilkanDetailReservasi(Connection conn, int reservasiId) throws SQLException {
        String sql = "SELECT r.reservasi_id, u.nama_lengkap, k.plat_nomor, k.merk, sp.kode_slot, g.nama_gedung, lg.nomor_lantai, r.waktu_masuk " +
                "FROM reservasi_parkir r " +
                "JOIN user u ON r.user_id = u.user_id " +
                "JOIN kendaraan k ON r.kendaraan_id = k.kendaraan_id " +
                "JOIN slot_parkir sp ON r.slot_id = sp.slot_id " +
                "JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                "JOIN gedung g ON lg.gedung_id = g.gedung_id " +
                "WHERE r.reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("\n-- Detail Reservasi --\nID: %d | Pengguna: %s | Kendaraan: %s %s | Slot: %s | Gedung: %s | Lantai: %d | Masuk: %s\n",
                        rs.getInt("reservasi_id"), rs.getString("nama_lengkap"), rs.getString("merk"),
                        rs.getString("plat_nomor"), rs.getString("kode_slot"), rs.getString("nama_gedung"),
                        rs.getInt("nomor_lantai"), rs.getTimestamp("waktu_masuk"));
            }
        }
    }

    public static void tampilkanDetailLog(Connection conn, int logId) throws SQLException {
        String sql = "SELECT lp.log_id, u.nama_lengkap, k.plat_nomor, k.merk, sp.kode_slot, g.nama_gedung, lg.nomor_lantai, lp.waktu_masuk, lp.waktu_keluar, IFNULL(p.total_bayar, 0) AS total_bayar " +
                "FROM log_parkir lp " +
                "JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id " +
                "JOIN user u ON k.user_id = u.user_id " +
                "JOIN slot_parkir sp ON lp.slot_id = sp.slot_id " +
                "JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id " +
                "JOIN gedung g ON lg.gedung_id = g.gedung_id " +
                "LEFT JOIN pembayaran p ON lp.log_id = p.log_id " +
                "WHERE lp.log_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("\n-- Detail Log Parkir --\nID: %d | Pengguna: %s | Kendaraan: %s %s | Slot: %s | Gedung: %s | Lantai: %d | Masuk: %s | Keluar: %s | Total Bayar: Rp%.2f\n",
                        rs.getInt("log_id"), rs.getString("nama_lengkap"), rs.getString("merk"),
                        rs.getString("plat_nomor"), rs.getString("kode_slot"), rs.getString("nama_gedung"),
                        rs.getInt("nomor_lantai"), rs.getTimestamp("waktu_masuk"),
                        rs.getTimestamp("waktu_keluar"), rs.getDouble("total_bayar"));
            }
        }
    }
}
