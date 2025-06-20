# 🅿️ Sistem Informasi Reservasi Tempat Parkir Mall

Sistem informasi ini dirancang untuk memudahkan proses pemesanan slot parkir di gedung bertingkat seperti mall secara digital, mulai dari pencatatan pengguna dan kendaraan, pemilihan slot, proses reservasi, hingga pembayaran dan laporan statistik.

---

## 🔑 Fitur Utama

- **Reservasi Slot Parkir Otomatis**  
  Pengguna dapat memilih slot parkir berdasarkan lantai dan gedung.

- **Validasi Slot dan Kendaraan**  
  Slot yang sudah dipesan otomatis diblokir oleh sistem menggunakan trigger.

- **Check-In dan Check-Out**  
  Sistem mencatat waktu masuk dan keluar kendaraan, serta mengatur status slot secara otomatis.

- **Hitung Tarif & Pembayaran Otomatis**  
  Menggunakan stored procedure untuk perhitungan biaya berdasarkan waktu parkir.

- **Laporan & Statistik**  
  Menampilkan laporan harian, bulanan, rekap transaksi, dan pengguna aktif.

---

## 👥 Biodata Kelompok 5 - Kelas B

| Nama Lengkap                  | NPM           |
|-------------------------------|---------------|
| Nayla Sifa’ul Qolbi           | 24082010047   |
| Helen Risky Dwi Wahyuni       | 24082010054   |
| Habibi Irfan Bayu             | 24082010075   |
| Rafael Marselino Mlasmene     | 22082011265   |

**Dosen Pengampu:** Mohamad Irwan Afandi, ST, M.Sc

---

## 🗂️ Struktur Tabel (Ringkasan)

| Tabel                | Kolom Penting                                                                            |
|----------------------|------------------------------------------------------------------------------------------|
| `user`               | `user_id`, `nama_lengkap`, `email`, `role`, `password`                                   |
| `kendaraan`          | `kendaraan_id`, `user_id`, `plat_nomor`, `tipe_kendaraan`, `merk`, `warna`               |
| `gedung`             | `gedung_id`, `nama_gedung`                                                               |
| `lantai_gedung`      | `lantai_gedung_id`, `gedung_id`, `nomor_lantai`                                          |
| `slot_parkir`        | `slot_id`, `kode_slot`, `tipe_slot`, `status_slot`, `lantai_gedung_id`                   |
| `reservasi_parkir`   | `reservasi_id`, `user_id`, `slot_id`, `kendaraan_id`, `waktu_masuk`, `waktu_keluar`      |
| `log_parkir`         | `log_id`, `kendaraan_id`, `slot_id`, `waktu_masuk`, `waktu_keluar`                       |
| `pembayaran`         | `pembayaran_id`, `log_id`, `metode_pembayaran`, `total_bayar`                            |
| `tarif_parkir`       | `tarif_id`, `gedung_id`, `lantai_gedung_id`, `tipe_slot`, `tarif_awal`, `tarif_per_jam`  |

---

## ⚙️ Trigger Otomatis

### 1. Update Status Slot Saat Reservasi Ditambahkan

```sql
CREATE TRIGGER trg_after_reservasi_insert
AFTER INSERT ON reservasi_parkir
FOR EACH ROW
BEGIN
  UPDATE slot_parkir SET status_slot = 'reservasi'
  WHERE slot_id = NEW.slot_id;
END;
