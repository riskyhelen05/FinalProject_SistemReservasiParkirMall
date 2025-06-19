package SistemReservasiParkir;

import java.sql.*;
import java.util.Scanner;

public class RegistrasiPenggunaDAO {

    // 1. REGISTRASI PENGGUNA BARU
    public static void daftarPenggunaBaru(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n===== Registrasi Pengguna Baru =====");
            System.out.print("Nama lengkap: ");
            String nama = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("No HP: ");
            String noHp = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            String sql = "INSERT INTO user (nama_lengkap, email, no_hp, password, role) VALUES (?, ?, ?, ?, 'pengguna')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setString(2, email);
            stmt.setString(3, noHp);
            stmt.setString(4, password);

            int hasil = stmt.executeUpdate();
            if (hasil > 0) {
                System.out.println("✅ Registrasi berhasil! Silakan login.");
            } else {
                System.out.println("❌ Registrasi gagal.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat registrasi: " + e.getMessage());
        }
    }

    // 2. EDIT PROFIL & GANTI PASSWORD
    public static void editProfilDanPassword(Connection conn, Scanner scanner, int userId) {
        try {
            System.out.println("\n===== Edit Profil & Password =====");

            // Tampilkan data pengguna saat ini
            String tampilSql = "SELECT nama_lengkap, email, no_hp FROM user WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(tampilSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Nama saat ini : " + rs.getString("nama_lengkap"));
                    System.out.println("Email saat ini: " + rs.getString("email"));
                    System.out.println("No HP saat ini: " + rs.getString("no_hp"));
                } else {
                    System.out.println("Data pengguna tidak ditemukan.");
                    return;
                }
            }

            // Input data baru
            System.out.print("Nama baru (kosongkan jika tidak diubah): ");
            String namaBaru = scanner.nextLine().trim();
            System.out.print("Email baru (kosongkan jika tidak diubah): ");
            String emailBaru = scanner.nextLine().trim();
            System.out.print("No HP baru (kosongkan jika tidak diubah): ");
            String nohpBaru = scanner.nextLine().trim();

            // Update profil jika ada perubahan
            String updateProfil = "UPDATE user SET nama_lengkap = COALESCE(NULLIF(?, ''), nama_lengkap), " +
                                  "email = COALESCE(NULLIF(?, ''), email), no_hp = COALESCE(NULLIF(?, ''), no_hp) " +
                                  "WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateProfil)) {
                ps.setString(1, namaBaru);
                ps.setString(2, emailBaru);
                ps.setString(3, nohpBaru);
                ps.setInt(4, userId);
                ps.executeUpdate();
                System.out.println("✅ Profil berhasil diperbarui.");
            }

            // Tanya apakah ingin mengganti password
            System.out.print("Ingin mengganti password? (y/n): ");
            String ubahPassword = scanner.nextLine().trim();
            if (ubahPassword.equalsIgnoreCase("y")) {
                System.out.print("Masukkan password lama: ");
                String oldPass = scanner.nextLine().trim();

                String cekSql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
                try (PreparedStatement ps = conn.prepareStatement(cekSql)) {
                    ps.setInt(1, userId);
                    ps.setString(2, oldPass);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        System.out.println("❌ Password lama salah.");
                        return;
                    }
                }

                System.out.print("Masukkan password baru: ");
                String newPass = scanner.nextLine().trim();
                if (newPass.isEmpty()) {
                    System.out.println("❌ Password baru tidak boleh kosong.");
                    return;
                }

                String updatePass = "UPDATE user SET password = ? WHERE user_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updatePass)) {
                    ps.setString(1, newPass);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                    System.out.println("✅ Password berhasil diperbarui.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat mengedit profil: " + e.getMessage());
        }
    }
}
