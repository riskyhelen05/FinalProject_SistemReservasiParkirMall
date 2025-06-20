# 🅿️ Sistem Informasi Reservasi Tempat Parkir Mall

Sistem informasi ini dirancang untuk memudahkan proses pemesanan slot parkir di gedung bertingkat seperti mall secara digital, mulai dari pencatatan pengguna dan kendaraan, pemilihan slot, proses reservasi, hingga pembayaran dan laporan statistik.

---

## 🔑 Fitur Utama

### 🏗️ Manajemen Data Master
- Menyimpan dan mengelola data **pengguna**, **gedung**, **lantai gedung**, **slot parkir**, **kendaraan**, dan **tarif parkir**.
- Sistem validasi bawaan untuk menjaga integritas relasi antar data (misalnya gedung ↔ lantai ↔ slot).

### 📅 Reservasi Slot Parkir
- Pengguna dapat melihat slot kosong berdasarkan lantai dan jenis kendaraan.
- Melakukan **reservasi slot** dengan sistem otomatis yang akan mengubah status slot ke “reservasi”.
- Sistem **mencegah reservasi ganda** dengan trigger bawaan.

### 🚗 Log Aktivitas Parkir (Masuk/Keluar)
- Sistem mencatat waktu masuk dan keluar kendaraan ke dalam **log parkir**.
- Slot yang sedang dipakai akan otomatis ditandai sebagai "terisi".
- Kendaraan **tidak bisa masuk dua kali** sebelum keluar (dicegah via trigger).

### 💳 Pembayaran Otomatis
- Sistem akan menghitung total biaya berdasarkan:
  - Durasi parkir
  - Tarif berdasarkan jenis slot dan lantai
- Pembayaran dapat dilakukan secara otomatis melalui stored procedure yang menghitung dan menyimpan transaksi.

### 📊 Laporan & Statistik
- Menyediakan laporan berupa:
  - **Statistik harian** (jumlah transaksi dan pendapatan)
  - **Statistik bulanan**
  - Slot parkir aktif, kosong, terisi per lantai
- Laporan tersedia dalam bentuk **view SQL siap pakai** dan bisa diekspor.

### 🧠 Trigger & Validasi Otomatis
- Sistem menggunakan **trigger MySQL** untuk:
  - Mengubah status slot saat reservasi/check-out
  - Mencegah kendaraan dan slot digunakan lebih dari satu kali secara bersamaan
  - Menjaga konsistensi data log dan reservasi

### 👁 View & Laporan Canggih
- View SQL siap digunakan oleh antarmuka aplikasi dan backend:
  - `view_status_slot_per_lantai`
  - `view_log_parkir_lengkap`
  - `view_reservasi_aktif`
  - `view_laporan_transaksi_pengguna`
- Laporan lanjutan menggunakan **Crosstab**, **CTE**, dan **Subquery** untuk analisis data yang lebih dalam.

### 📤 Ekspor CSV
- Data log parkir dan pembayaran dapat diekspor ke file CSV menggunakan query SELECT dan tools seperti DBeaver atau MySQL Workbench.

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
| `user`               | `user_id`, `nama_lengkap`, `email`, `no_hp`, `password`, `role`                               |
| `kendaraan`          | `kendaraan_id`, `user_id`, `plat_nomor`, `tipe_kendaraan`, `merk`, `warna`               |
| `gedung`             | `gedung_id`, `nama_gedung`                                                               |
| `lantai_gedung`      | `lantai_gedung_id`, `gedung_id`, `nomor_lantai`                                          |
| `slot_parkir`        | `slot_id`, `kode_slot`, `tipe_slot`, `status_slot`, `lantai_gedung_id`                   |
| `reservasi_parkir`   | `reservasi_id`, `user_id`, `slot_id`, `kendaraan_id`, `tanggal_reservasi`, `waktu_masuk`, `waktu_keluar`, `status_reservasi`      |
| `log_parkir`         | `log_id`, `kendaraan_id`, `slot_id`, `waktu_masuk`, `waktu_keluar`                       |
| `pembayaran`         | `pembayaran_id`, `log_id`, `metode_pembayaran`, `total_bayar`                            |
| `tarif_parkir`       | `tarif_id`, `gedung_id`, `lantai_gedung_id`, `tipe_slot`, `tarif_awal`, `durasi_awal`, `tarif_per_jam_berikutnya`  |

---

## 🔗 Relasi Utama
- `User` ↔ `Kendaraan` : One to Many  
- `Gedung` ↔ `Lantai_Gedung` : One to Many  
- `Slot_Parkir` ↔ `Log_Parkir` / `Reservasi_Parkir` : One to Many  
- `Log_Parkir` ↔ `Pembayaran` : One to One  
- `Tarif_Parkir` : Many to One dari `Gedung` dan `Lantai_Gedung`

---

## 🗃 Struktur SQL Lengkap
create table User (
user_id INT PRIMARY KEY AUTO_INCREMENT,
nama_lengkap VARCHAR(100) NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
no_hp VARCHAR(20),
password VARCHAR(100) NOT NULL,
role ENUM('admin', 'pengguna') DEFAULT 'pengguna',
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table Gedung (
gedung_id INT primary key auto_increment,
nama_gedung VARCHAR (100) not null
);

create table Lantai_Gedung (
lantai_gedung_id INT primary key auto_increment,
gedung_id INT not null,
nomor_lantai INT not NULL,
FOREIGN KEY (gedung_id) REFERENCES Gedung(gedung_id) ON DELETE CASCADE
);

create table Slot_Parkir (
slot_id INT PRIMARY KEY AUTO_INCREMENT,
kode_slot VARCHAR(10) NOT null UNIQUE,
tipe_slot ENUM('mobil', 'motor', 'electric_vehicle') NOT NULL,
status_slot ENUM('kosong', 'terisi', 'reservasi') DEFAULT 'kosong',
lantai_gedung_id INT NOT NULL,
FOREIGN KEY (lantai_gedung_id) REFERENCES Lantai_Gedung(lantai_gedung_id)
);

create table Kendaraan (
kendaraan_id INT PRIMARY KEY AUTO_INCREMENT,
plat_nomor VARCHAR(20) UNIQUE NOT NULL,
tipe_kendaraan ENUM('mobil', 'motor', 'electric_vehicle') NOT NULL,
merk VARCHAR(50),
warna VARCHAR(30),
user_id INT NOT NULL,
FOREIGN KEY (user_id) REFERENCES User(user_id)
);

create table Log_Parkir (
log_id INT PRIMARY KEY AUTO_INCREMENT,
kendaraan_id INT NOT NULL,
slot_id INT NOT NULL,
waktu_masuk DATETIME NOT NULL,
waktu_keluar DATETIME default NULL,
FOREIGN KEY (kendaraan_id) REFERENCES Kendaraan(kendaraan_id),
FOREIGN KEY (slot_id) REFERENCES Slot_Parkir(slot_id)
);

create table Pembayaran (
pembayaran_id INT primary key AUTO_increment,
log_id INT not null,
metode_pembayaran ENUM('cash', 'e-wallet', 'debit'),
total_bayar DECIMAL (10,2),
foreign key (log_id) references Log_Parkir(log_id)
);

create table Reservasi_Parkir ( 
reservasi_id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
slot_id INT NOT NULL,
kendaraan_id INT NOT NULL,
tanggal_reservasi DATE NOT NULL,
waktu_masuk DATETIME NOT NULL,
waktu_keluar DATETIME default NULL,
status_reservasi ENUM('aktif', 'selesai', 'batal') DEFAULT 'aktif',
FOREIGN KEY (user_id) REFERENCES User(user_id),
FOREIGN KEY (slot_id) REFERENCES Slot_Parkir(slot_id),
FOREIGN KEY (kendaraan_id) REFERENCES Kendaraan(kendaraan_id)
);

create table Tarif_Parkir (
tarif_parkir_id INT primary key auto_increment,
tipe_slot ENUM('mobil', 'motor', 'electric_vehicle') NOT NULL,
tarif_awal DECIMAL (10,2),
durasi_awal INT,
tarif_per_jam_berikutnya DECIMAL (10,2),
gedung_id INT NOT NULL,
lantai_gedung_id INT NOT NULL,
FOREIGN KEY (gedung_id) REFERENCES Gedung(gedung_id),
FOREIGN KEY (lantai_gedung_id) REFERENCES Lantai_Gedung(lantai_gedung_id) ON DELETE CASCADE
);

---

## ⚙️ Trigger Otomatis

### 1. Update Status Slot Saat Reservasi Ditambahkan

CREATE TRIGGER trg_after_reservasi_insert
AFTER INSERT ON reservasi_parkir
FOR EACH ROW
BEGIN
  UPDATE slot_parkir SET status_slot = 'reservasi'
  WHERE slot_id = NEW.slot_id;
END;

### 2. Update status slot saat reservasi diubah
CREATE TRIGGER trg_after_reservasi_update_status
AFTER UPDATE ON reservasi_parkir
FOR EACH ROW
BEGIN
  IF NEW.status_reservasi = 'batal' THEN
    UPDATE slot_parkir
    SET status_slot = 'kosong'
    WHERE slot_id = NEW.slot_id;
  ELSEIF NEW.status_reservasi = 'selesai' THEN
    UPDATE slot_parkir
    SET status_slot = 'terisi'
    WHERE slot_id = NEW.slot_id;
  END IF;
END;

### 3. Otomatis ubah status reservasi jika waktu_keluar diisi
CREATE TRIGGER trg_auto_selesai_reservasi_waktu_keluar
AFTER UPDATE ON reservasi_parkir
FOR EACH ROW
BEGIN
  IF NEW.waktu_keluar IS NOT NULL AND OLD.waktu_keluar IS NULL THEN
    UPDATE reservasi_parkir
    SET status_reservasi = 'selesai'
    WHERE reservasi_id = NEW.reservasi_id;
  END IF;
END;

### 4. Kosongkan slot jika kendaraan keluar
CREATE TRIGGER trg_auto_kosong_slot_after_log_keluar
AFTER UPDATE ON log_parkir
FOR EACH ROW
BEGIN
  IF NEW.waktu_keluar IS NOT NULL AND OLD.waktu_keluar IS NULL THEN
    UPDATE slot_parkir
    SET status_slot = 'kosong'
    WHERE slot_id = NEW.slot_id;
  END IF;
END;

### 5. Cegah penggunaan slot jika sudah terisi
CREATE TRIGGER trg_prevent_double_use_slot
BEFORE INSERT ON log_parkir
FOR EACH ROW
BEGIN
  DECLARE v_status ENUM('kosong', 'terisi', 'reservasi');

  SELECT status_slot INTO v_status
  FROM slot_parkir
  WHERE slot_id = NEW.slot_id;

  IF v_status = 'terisi' THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Slot sedang terisi, tidak dapat digunakan!';
  END IF;
END;

### 6. Cegah kendaraan masuk dua kali ke log parkir
CREATE TRIGGER trg_prevent_double_park_kendaraan
BEFORE INSERT ON log_parkir
FOR EACH ROW
BEGIN
  DECLARE count_active INT;

  SELECT COUNT(*) INTO count_active
  FROM log_parkir
  WHERE kendaraan_id = NEW.kendaraan_id AND waktu_keluar IS NULL;

  IF count_active > 0 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Kendaraan ini masih aktif di log parkir!';
  END IF;
END;

### 7. Cegah reservasi jika slot tidak kosong
CREATE TRIGGER trg_prevent_reservasi_slot_terisi
BEFORE INSERT ON reservasi_parkir
FOR EACH ROW
BEGIN
  DECLARE v_status ENUM('kosong', 'terisi', 'reservasi');
  SELECT status_slot INTO v_status FROM slot_parkir WHERE slot_id = NEW.slot_id;
  IF v_status != 'kosong' THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Slot tidak tersedia untuk reservasi';
  END IF;
END;

---

## ⚙️ Stored Procedures

### 1. Hitung total bayar berdasarkan log parkir

CREATE PROCEDURE sp_hitung_total_bayar (
    IN p_log_id INT,
    OUT p_total DECIMAL(10,2)
)
BEGIN
  DECLARE masuk DATETIME;
  DECLARE keluar DATETIME;
  DECLARE durasi_jam INT;
  DECLARE tarif_awal DECIMAL(10,2);
  DECLARE tarif_per_jam DECIMAL(10,2);
  DECLARE durasi_awal INT;
  DECLARE tipe_kendaraan ENUM('mobil','motor','electric_vehicle');
  DECLARE v_slot_id INT;
  DECLARE v_gedung_id INT;
  DECLARE v_lantai_id INT;

  -- Cek apakah log_id valid
  IF EXISTS (SELECT 1 FROM log_parkir WHERE log_id = p_log_id) THEN

  -- Ambil data waktu dan kendaraan, serta slot_id dari log parkir
  SELECT lp.waktu_masuk, lp.waktu_keluar, k.tipe_kendaraan, lp.slot_id
  INTO masuk, keluar, tipe_kendaraan, v_slot_id
  FROM log_parkir lp
  JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
  WHERE lp.log_id = p_log_id;

  -- Jika waktu_keluar NULL, set sekarang
  IF keluar IS NULL THEN
    SET keluar = NOW();
  END IF;

  -- Cari gedung dan lantai dari slot_parkir
  SELECT lg.gedung_id, lg.lantai_gedung_id
  INTO v_gedung_id, v_lantai_id
  FROM slot_parkir sp
  JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id
  WHERE sp.slot_id = v_slot_id;

  -- Ambil tarif sesuai tipe kendaraan, gedung, dan lantai
  SELECT tarif_awal, tarif_per_jam_berikutnya, durasi_awal
  INTO tarif_awal, tarif_per_jam, durasi_awal
  FROM tarif_parkir
  WHERE tipe_slot = tipe_kendaraan
    AND gedung_id = v_gedung_id
    AND lantai_gedung_id = v_lantai_id
  LIMIT 1;

  -- Hitung durasi dalam jam, dibulatkan ke atas
   IF tarif_awal IS NOT NULL AND durasi_awal IS NOT NULL AND tarif_per_jam IS NOT NULL THEN
  SET durasi_jam = CEIL(TIMESTAMPDIFF(MINUTE, masuk, keluar) / 60);

  -- Hitung total bayar
  IF durasi_jam <= durasi_awal THEN
    SET p_total = tarif_awal;
  ELSE
    SET p_total = tarif_awal + (durasi_jam - durasi_awal) * tarif_per_jam;
  END IF;
  ELSE
      -- Jika tarif tidak ditemukan
      SET p_total = NULL;
    END IF;

  ELSE
    -- log_id tidak ditemukan
    SET p_total = NULL;
  END IF;
END;

-- Cara Memanggil --
SET @total_bayar = 0;
CALL sp_hitung_total_bayar(3, @total_bayar);
SELECT @total_bayar;

### 2. Prosedur hitung dan simpan pembayaran
==============================================

CREATE PROCEDURE sp_hitung_dan_simpan_pembayaran(
  IN p_log_id INT,
  IN p_metode ENUM('cash', 'e-wallet', 'debit')
)
BEGIN
  DECLARE v_total DECIMAL(10,2);

  -- Hitung total bayar dengan memanggil prosedur sebelumnya
  CALL sp_hitung_total_bayar(p_log_id, v_total);

  -- Insert data pembayaran
  INSERT INTO pembayaran (log_id, metode_pembayaran, total_bayar)
  VALUES (p_log_id, p_metode, v_total);
END;

-- Cara memanggil --
CALL sp_hitung_dan_simpan_pembayaran(1, 'cash');
SELECT * FROM pembayaran WHERE log_id = 1;

### 3. Prosedur mulai parkir dari reservasi

CREATE PROCEDURE sp_mulai_parkir_dari_reservasi(IN p_reservasi_id INT)
BEGIN
  DECLARE v_kendaraan_id INT;
  DECLARE v_slot_id INT;
  DECLARE v_waktu_masuk DATETIME;

  SELECT kendaraan_id, slot_id, waktu_masuk
  INTO v_kendaraan_id, v_slot_id, v_waktu_masuk
  FROM reservasi_parkir
  WHERE reservasi_id = p_reservasi_id;

  INSERT INTO log_parkir (kendaraan_id, slot_id, waktu_masuk)
  VALUES (v_kendaraan_id, v_slot_id, v_waktu_masuk);

  -- Update status slot_parkir menjadi 'terisi'
  UPDATE slot_parkir
  SET status_slot = 'terisi'
  WHERE slot_id = v_slot_id;
END;

-- Cara memanggil --
CALL sp_mulai_parkir_dari_reservasi(2);
SELECT * FROM log_parkir ORDER BY log_id DESC LIMIT 1;
SELECT * FROM slot_parkir WHERE slot_id = (SELECT slot_id FROM reservasi_parkir WHERE reservasi_id = 2);

### 4. Prosedur proses pembayaran (menghitung dan simpan)

CREATE PROCEDURE sp_proses_pembayaran(
  IN p_log_id INT,
  IN p_metode_pembayaran ENUM('cash', 'e-wallet', 'debit')
)
BEGIN
  DECLARE p_total DECIMAL(10,2);

  CALL sp_hitung_total_bayar(p_log_id, p_total);

  INSERT INTO pembayaran (log_id, metode_pembayaran, total_bayar)
  VALUES (p_log_id, p_metode_pembayaran, p_total);

  -- Update waktu_keluar log_parkir dengan waktu sekarang
  UPDATE log_parkir
  SET waktu_keluar = NOW()
  WHERE log_id = p_log_id;
END;

-- Cara memanggil --
CALL sp_proses_pembayaran(1, 'e-wallet');
SELECT * FROM pembayaran WHERE log_id = 1;
SELECT * FROM log_parkir WHERE log_id = 1;

### 5. Prosedur manual isi waktu keluar

CREATE PROCEDURE insert_keluar_manual(
  IN p_log_id INT,
  IN p_waktu_keluar DATETIME
)
BEGIN
  UPDATE log_parkir
  SET waktu_keluar = p_waktu_keluar
  WHERE log_id = p_log_id;
END;

Cara memanggil
CALL insert_keluar_manual(1, NOW());
SELECT * FROM log_parkir WHERE log_id = 1;

---

## 📊 View & Laporan
### 👁 View Utama
#### 1. view_status_slot_per_lantai – Jumlah slot kosong/terisi/reservasi per lantai

CREATE OR REPLACE VIEW view_status_slot_per_lantai AS
SELECT 
    g.nama_gedung,
    lg.nomor_lantai,
    COUNT(sp.slot_id) AS total_slot,
    SUM(CASE WHEN sp.status_slot = 'kosong' THEN 1 ELSE 0 END) AS jumlah_kosong,
    SUM(CASE WHEN sp.status_slot = 'terisi' THEN 1 ELSE 0 END) AS jumlah_terisi,
    SUM(CASE WHEN sp.status_slot = 'reservasi' THEN 1 ELSE 0 END) AS jumlah_reservasi
FROM slot_parkir sp
JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id
JOIN gedung g ON lg.gedung_id = g.gedung_id
GROUP BY g.nama_gedung, lg.nomor_lantai;

SELECT * FROM view_status_slot_per_lantai;

#### 2. view_log_parkir_lengkap – Data parkir lengkap + pembayaran

CREATE VIEW view_log_parkir_lengkap AS
SELECT 
    lp.log_id,
    u.nama_lengkap AS pengguna,
    k.plat_nomor,
    k.tipe_kendaraan,
    g.nama_gedung,
    lg.nomor_lantai,
    sp.kode_slot,
    lp.waktu_masuk,
    lp.waktu_keluar,
    IFNULL(p.total_bayar, 0) AS total_bayar,
    p.metode_pembayaran
FROM log_parkir lp
JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
JOIN user u ON k.user_id = u.user_id
JOIN slot_parkir sp ON lp.slot_id = sp.slot_id
JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id
JOIN gedung g ON lg.gedung_id = g.gedung_id
LEFT JOIN pembayaran p ON lp.log_id = p.log_id;

SELECT * FROM view_log_parkir_lengkap;

#### 4. view_reservasi_aktif – Daftar reservasi dengan status aktif

CREATE VIEW view_reservasi_aktif AS
SELECT 
    rp.reservasi_id,
    u.nama_lengkap AS pengguna,
    k.plat_nomor,
    sp.kode_slot,
    g.nama_gedung,
    lg.nomor_lantai,
    rp.waktu_masuk,
    rp.waktu_keluar,
    rp.status_reservasi
FROM reservasi_parkir rp
JOIN user u ON rp.user_id = u.user_id
JOIN kendaraan k ON rp.kendaraan_id = k.kendaraan_id
JOIN slot_parkir sp ON rp.slot_id = sp.slot_id
JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id
JOIN gedung g ON lg.gedung_id = g.gedung_id
WHERE rp.status_reservasi = 'aktif';

SELECT * FROM view_reservasi_aktif;

#### 4. view_laporan_transaksi_pengguna – Total pembayaran per user

CREATE OR REPLACE VIEW view_laporan_transaksi_pengguna AS
SELECT 
    u.user_id,
    u.nama_lengkap,
    COUNT(p.pembayaran_id) AS jumlah_transaksi,
    IFNULL(SUM(p.total_bayar), 0) AS total_bayar
FROM pembayaran p
JOIN log_parkir lp ON p.log_id = lp.log_id
JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
JOIN user u ON k.user_id = u.user_id
GROUP BY u.user_id, u.nama_lengkap;

SELECT * FROM view_laporan_transaksi_pengguna;

### 📈 Report Lanjutan
#### 1. Crosstab: vw_rekap_slot_parkir

-- Laporan Jumlah Slot Parkir per Status per Lantai (pivot) --

CREATE OR REPLACE VIEW vw_rekap_slot_parkir AS
SELECT
    g.nama_gedung,
    lg.nomor_lantai,
    COUNT(*) AS total_slot,
    SUM(CASE WHEN sp.status_slot = 'kosong' THEN 1 ELSE 0 END) AS jumlah_kosong,
    SUM(CASE WHEN sp.status_slot = 'terisi' THEN 1 ELSE 0 END) AS jumlah_terisi,
    SUM(CASE WHEN sp.status_slot = 'reservasi' THEN 1 ELSE 0 END) AS jumlah_reservasi
FROM slot_parkir sp
JOIN lantai_gedung lg ON sp.lantai_gedung_id = lg.lantai_gedung_id
JOIN gedung g ON lg.gedung_id = g.gedung_id
GROUP BY g.nama_gedung, lg.nomor_lantai
ORDER BY g.nama_gedung, lg.nomor_lantai;

SELECT * FROM vw_rekap_slot_parkir;

#### 2. CTE: vw_user_atas_rata_rata_pembayaran

-- Laporan Pengguna yang Memiliki Total Pembayaran diatas Rata-rata --

CREATE OR REPLACE VIEW vw_user_atas_rata_rata_pembayaran AS
WITH total_pembayaran_per_user AS (
    SELECT 
        u.user_id,
        u.nama_lengkap,
        SUM(p.total_bayar) AS total_bayar
    FROM pembayaran p
    JOIN log_parkir lp ON p.log_id = lp.log_id
    JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
    JOIN user u ON k.user_id = u.user_id
    GROUP BY u.user_id, u.nama_lengkap
),
rata_rata AS (
    SELECT AVG(total_bayar) AS rata_rata_bayar FROM total_pembayaran_per_user
)
SELECT 
    tpu.user_id,
    tpu.nama_lengkap,
    tpu.total_bayar
FROM total_pembayaran_per_user tpu, rata_rata
WHERE tpu.total_bayar > rata_rata.rata_rata_bayar;

SELECT * FROM vw_user_atas_rata_rata_pembayaran;

#### 3. Subquery: vw_parkir_aktif_belum_bayar

-- Laporan Kendaraan yang Belum Pernah Keluar dari Area Parkir --

CREATE OR REPLACE VIEW vw_parkir_aktif_belum_bayar AS
SELECT *
FROM (
    SELECT 
        k.kendaraan_id,
        k.plat_nomor,
        k.merk,
        u.nama_lengkap,
        lp.waktu_masuk,
        lp.waktu_keluar,
        ROW_NUMBER() OVER (PARTITION BY k.kendaraan_id ORDER BY lp.waktu_masuk DESC) AS rn
    FROM log_parkir lp
    JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
    JOIN user u ON k.user_id = u.user_id
    WHERE lp.waktu_keluar IS NULL
    AND lp.log_id NOT IN (SELECT log_id FROM pembayaran)
) AS sub
WHERE rn = 1;

SELECT * FROM vw_parkir_aktif_belum_bayar;

### 📤 Ekspor CSV
Ekspor data log parkir + pembayaran untuk digunakan di Excel

SELECT 
  lp.log_id,
  u.nama_lengkap,
  k.plat_nomor,
  k.tipe_kendaraan,
  lp.waktu_masuk,
  lp.waktu_keluar,
  IFNULL(p.total_bayar, 0) AS total_bayar
FROM log_parkir lp
JOIN kendaraan k ON lp.kendaraan_id = k.kendaraan_id
JOIN user u ON k.user_id = u.user_id
LEFT JOIN pembayaran p ON lp.log_id = p.log_id;

### 📆 Statistik Harian & Bulanan
#### 1. laporan_statistik_harian() – Jumlah transaksi dan total pendapatan harian

CREATE PROCEDURE laporan_statistik_harian()
BEGIN
  SELECT 
    DATE(lp.waktu_masuk) AS tanggal,
    COUNT(p.pembayaran_id) AS jumlah_transaksi,
    SUM(p.total_bayar) AS total_pendapatan
  FROM pembayaran p
  JOIN log_parkir lp ON p.log_id = lp.log_id
  GROUP BY DATE(lp.waktu_masuk);
END;

--Cara memanggil
CALL laporan_statistik_harian();
 
#### 2. laporan_statistik_bulanan() – Ringkasan keuangan per bulan

CREATE PROCEDURE laporan_statistik_bulanan()
BEGIN
  SELECT 
    MONTH(lp.waktu_masuk) AS bulan,
    YEAR(lp.waktu_masuk) AS tahun,
    COUNT(p.pembayaran_id) AS jumlah_transaksi,
    SUM(p.total_bayar) AS total_pendapatan
  FROM pembayaran p
  JOIN log_parkir lp ON p.log_id = lp.log_id
  GROUP BY YEAR(lp.waktu_masuk), MONTH(lp.waktu_masuk);
END;

--Cara memanggil

---
## 📄 Lisensi
Proyek ini dibuat sebagai bagian dari Tugas Akhir Mata Kuliah Basis Data
Universitas Pembangunan Nasional Veteran Jawa Timur – Tahun 2025

---
## 📌 Catatan
-Denda parkir diterapkan jika keluar melebihi waktu, sesuai durasi dan tarif per jam.
-Sistem menghindari pemakaian slot ganda menggunakan Trigger.
-Stored Procedure dapat dipanggil dari backend (Java GUI) untuk otomatisasi pembayaran.

## 🚀 Cara Menjalankan Proyek
### 📁 1. Persiapan Tools
Pastikan sudah menginstal:

✅ MySQL Server (rekomendasi: MySQL 8+)

✅ DBeaver atau MySQL Workbench (untuk eksekusi SQL)

✅ Java JDK 17 atau terbaru

✅ NetBeans atau text editor + terminal

✅ JDBC driver: mysql-connector-j-8.0.33.jar


---

### 🗃️ 2. Setup Database

1. **Buka DBeaver/MySQL Workbench**
2. Buat database:
CREATE DATABASE db_parkir_mall;
USE db_parkir_mall;
3.Import file SQL secara berurutan:

Tabel Dan Dummy.sql

CRUD Tabel Master dengan Stored Procedure.sql

CRUD Tabel Relasi Master-Detail dengan Stored Procedure.sql

Stored Procedure.sql

Trigger dan View.sql

Fitur Tambahan dan Report 3 Laporan.sql

---

### 🖥 3. Jalankan Aplikasi Java
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" Main
