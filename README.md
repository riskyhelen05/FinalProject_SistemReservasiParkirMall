# 🅿️ Sistem Informasi Reservasi Tempat Parkir Mall

Sistem informasi ini dirancang untuk memudahkan proses pemesanan slot parkir di gedung bertingkat seperti mall secara digital, mulai dari pencatatan pengguna dan kendaraan, pemilihan slot, proses reservasi, hingga pembayaran dan laporan statistik.

---

## 🔑 Fitur Utama
## 🏗️ Manajemen Data Master

Menyimpan dan mengelola data pengguna, gedung, lantai gedung, slot parkir, kendaraan, dan tarif parkir.

Sistem validasi bawaan untuk menjaga integritas relasi antar data (misalnya gedung ↔ lantai ↔ slot).

## 📅 Reservasi Slot Parkir
Pengguna dapat melihat slot kosong berdasarkan lantai dan jenis kendaraan.

Melakukan reservasi slot dengan sistem otomatis yang akan mengubah status slot ke “reservasi”.

Sistem mencegah reservasi ganda dengan trigger bawaan.

## 🚗 Log Aktivitas Parkir (Masuk/Keluar)
Sistem mencatat waktu masuk dan keluar kendaraan ke dalam log parkir.

Slot yang sedang dipakai akan otomatis ditandai sebagai "terisi".

Kendaraan tidak bisa masuk dua kali sebelum keluar (dicegah via trigger).

## 💳 Pembayaran Otomatis
Sistem akan menghitung total biaya berdasarkan:

Durasi parkir

Tarif berdasarkan jenis slot dan lantai

Pembayaran dapat dilakukan secara otomatis melalui stored procedure yang menghitung dan menyimpan transaksi.

## 📊 Laporan & Statistik
Menyediakan laporan berupa:

Statistik harian (jumlah transaksi dan pendapatan)

Statistik bulanan

Slot parkir aktif, kosong, terisi per lantai

Laporan tersedia dalam bentuk view SQL siap pakai dan bisa diekspor.

## 🧠 Trigger & Validasi Otomatis
Sistem menggunakan trigger MySQL untuk:

Mengubah status slot saat reservasi/check-out

Mencegah kendaraan dan slot digunakan lebih dari satu kali secara bersamaan

Menjaga konsistensi data log dan reservasi

## 👁 View & Laporan Canggih
View SQL siap digunakan oleh antarmuka aplikasi dan backend:

view_status_slot_per_lantai

view_log_parkir_lengkap

view_reservasi_aktif

view_laporan_transaksi_pengguna

Laporan lanjutan menggunakan Crosstab, CTE, dan Subquery untuk analisis data yang lebih dalam.

## 📤 Ekspor CSV
Data log parkir dan pembayaran dapat diekspor ke file CSV menggunakan query SELECT dan tools seperti DBeaver atau MySQL Workbench.

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
