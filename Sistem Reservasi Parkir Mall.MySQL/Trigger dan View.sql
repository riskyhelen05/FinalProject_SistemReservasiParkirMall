=============
-- TRIGGER --
=============

======================================================
-- 1. Update status slot saat reservasi ditambahkan --
======================================================

CREATE TRIGGER trg_after_reservasi_insert
AFTER INSERT ON reservasi_parkir
FOR EACH ROW
BEGIN
  UPDATE slot_parkir
  SET status_slot = 'reservasi'
  WHERE slot_id = NEW.slot_id;
END;

=================================================
-- 2. Update status slot saat reservasi diubah --
=================================================

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

===============================================================
-- 3. Otomatis ubah status reservasi jika waktu_keluar diisi --
===============================================================

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

=============================================
-- 4. Kosongkan slot jika kendaraan keluar --
=============================================

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

================================================
-- 5. Cegah penggunaan slot jika sudah terisi --
================================================

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

=====================================================
-- 6. Cegah kendaraan masuk dua kali ke log parkir --
=====================================================

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

===============================================
-- 7. Cegah reservasi jika slot tidak kosong --
===============================================

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

==========
-- VIEW --
==========

===============================================
-- 1. LAPORAN SLOT DAN STATUS SLOT PER LANTAI--
===============================================

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

============================================
-- 2. DATA LOG_PARKIR LENGKAP + PEMBAYARAN--
============================================

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

=============================
-- 3. DATA RESERVASI AKTIF --
=============================

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

===================================
-- 4. LAPORAN TRANSAKSI PENGGUNA --
===================================

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