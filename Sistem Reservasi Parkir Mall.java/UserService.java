package SistemReservasiParkir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserService {
    public static void daftarAkunBaru(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Daftar Akun Baru ===");
            System.out.print("Nama lengkap: ");
            String nama = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("No. HP: ");
            String noHp = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            // Cek apakah email sudah terdaftar
            String cekSql = "SELECT COUNT(*) FROM user WHERE email = ?";
            try (PreparedStatement cekStmt = conn.prepareStatement(cekSql)) {
                cekStmt.setString(1, email);
                ResultSet rs = cekStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Email sudah digunakan. Silakan gunakan email lain.");
                    return;
                }
            }

            String insertSql = "INSERT INTO user (nama_lengkap, email, no_hp, password, role) VALUES (?, ?, ?, ?, 'pengguna')";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, nama);
                stmt.setString(2, email);
                stmt.setString(3, noHp);
                stmt.setString(4, password);
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    System.out.println("Akun berhasil dibuat. Silakan login.");
                } else {
                    System.out.println("Pendaftaran gagal.");
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat mendaftar: " + e.getMessage());
        }
    }

    public static void gantiPassword(Connection conn, Scanner scanner, int userId) {
        try {
            System.out.print("Masukkan password lama: ");
            String oldPass = scanner.nextLine();
            System.out.print("Masukkan password baru: ");
            String newPass = scanner.nextLine();

            String cekSql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
            try (PreparedStatement cekStmt = conn.prepareStatement(cekSql)) {
                cekStmt.setInt(1, userId);
                cekStmt.setString(2, oldPass);
                ResultSet rs = cekStmt.executeQuery();
                if (rs.next()) {
                    String updateSql = "UPDATE user SET password = ? WHERE user_id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                        stmt.setString(1, newPass);
                        stmt.setInt(2, userId);
                        stmt.executeUpdate();
                        System.out.println("Password berhasil diperbarui.");
                    }
                } else {
                    System.out.println("Password lama salah.");
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat mengganti password: " + e.getMessage());
        }
    }
}
