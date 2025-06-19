=============================================================
-- CRUD TABEL RELASI MASTER-DETAIL DENGAN STORED PROCEDURE --
=============================================================

======================================
-- STORED PROCEDURE CRUD LOG_PARKIR --
======================================

-- Insert Log_Parkir
CREATE PROCEDURE sp_insert_log_parkir (
  IN p_kendaraan_id INT,
  IN p_slot_id INT,
  IN p_waktu_masuk DATETIME
)
BEGIN
  INSERT INTO Log_Parkir (kendaraan_id, slot_id, waktu_masuk)
  VALUES (p_kendaraan_id, p_slot_id, p_waktu_masuk);
END;

-- Cara Memanggil
CALL sp_insert_log_parkir(10, 6, '2025-06-07 08:30:00');
SELECT * FROM Log_Parkir ORDER BY log_id DESC LIMIT 1;

-- Read Log_Parkir by ID
CREATE PROCEDURE sp_get_log_parkir_by_id (
  IN p_log_id INT
)
BEGIN
  SELECT * FROM Log_Parkir WHERE log_id = p_log_id;
END;

-- Cara Memanggi
CALL sp_get_log_parkir_by_id(1);

-- Update Log_Parkir
CREATE PROCEDURE sp_update_log_parkir (
  IN p_log_id INT,
  IN p_kendaraan_id INT,
  IN p_slot_id INT,
  IN p_waktu_masuk DATETIME,
  IN p_waktu_keluar DATETIME
)
BEGIN
  UPDATE Log_Parkir
  SET kendaraan_id = p_kendaraan_id,
      slot_id = p_slot_id,
      waktu_masuk = p_waktu_masuk,
      waktu_keluar = p_waktu_keluar
  WHERE log_id = p_log_id;
END;

-- Cara Memanggil
CALL sp_update_log_parkir(1, 10, 6, '2025-06-07 08:30:00', '2025-06-07 12:30:00');
SELECT * FROM Log_Parkir WHERE log_id = 1;

-- Delete Log_Parkir
CREATE PROCEDURE sp_delete_log_parkir (
  IN p_log_id INT
)
BEGIN
  DELETE FROM Log_Parkir WHERE log_id = p_log_id;
END;

-- CARA MEMANGGIL --
CALL sp_delete_log_parkir(4);
SELECT * FROM Log_Parkir WHERE log_id = 4;

============================================
-- STORED PROCEDURE CRUD RESERVASI_PARKIR --
============================================

-- Insert Reservasi_Parkir
CREATE PROCEDURE sp_insert_reservasi_parkir (
  IN p_user_id INT,
  IN p_slot_id INT,
  IN p_kendaraan_id INT,
  IN p_tanggal_reservasi DATE,
  IN p_waktu_masuk DATETIME,
  IN p_waktu_keluar DATETIME,
  IN p_status_reservasi ENUM('aktif', 'selesai', 'batal')
)
BEGIN
  INSERT INTO Reservasi_Parkir (user_id, slot_id, kendaraan_id, tanggal_reservasi,
  waktu_masuk, waktu_keluar, status_reservasi)
  VALUES (p_user_id, p_slot_id, p_kendaraan_id, p_tanggal_reservasi, p_waktu_masuk,
p_waktu_keluar, p_status_reservasi);
END;

-- Cara Memanggil
CALL sp_insert_reservasi_parkir(3, 7, 10, '2025-06-10', '2025-06-10 09:00:00',
'2025-06-10 17:00:00', 'aktif');
SELECT * FROM Reservasi_Parkir ORDER BY reservasi_id DESC LIMIT 1;

-- Read Reservasi_Parkir by ID
CREATE PROCEDURE sp_get_reservasi_parkir_by_id (
  IN p_reservasi_id INT
)
BEGIN
  SELECT * FROM Reservasi_Parkir WHERE reservasi_id = p_reservasi_id;
END;

-- Cara Memanggil
CALL sp_get_reservasi_parkir_by_id(2);

-- Update Reservasi_Parkir
CREATE PROCEDURE sp_update_reservasi_parkir (
  IN p_reservasi_id INT,
  IN p_user_id INT,
  IN p_slot_id INT,
  IN p_kendaraan_id INT,
  IN p_tanggal_reservasi DATE,
  IN p_waktu_masuk DATETIME,
  IN p_waktu_keluar DATETIME,
  IN p_status_reservasi ENUM('aktif', 'selesai', 'batal')
)
BEGIN
  UPDATE Reservasi_Parkir
  SET user_id = p_user_id,
      slot_id = p_slot_id,
      kendaraan_id = p_kendaraan_id,
      tanggal_reservasi = p_tanggal_reservasi,
      waktu_masuk = p_waktu_masuk,
      waktu_keluar = p_waktu_keluar,
      status_reservasi = p_status_reservasi
  WHERE reservasi_id = p_reservasi_id;
END;

-- Cara Memanggil
CALL sp_update_reservasi_parkir(2, 3, 8, 11, '2025-06-11', '2025-06-11 09:00:00', '2025-06-11 18:00:00', 'selesai');
SELECT * FROM Reservasi_Parkir WHERE reservasi_id = 2;

-- Delete Reservasi_Parkir
CREATE PROCEDURE sp_delete_reservasi_parkir (
  IN p_reservasi_id INT
)
BEGIN
  DELETE FROM Reservasi_Parkir WHERE reservasi_id = p_reservasi_id;
END;

-- CARA MEMANGGIL --
CALL sp_delete_reservasi_parkir(1);
SELECT * FROM Reservasi_Parkir WHERE reservasi_id = 1;

======================================
-- STORED PROCEDURE CRUD PEMBAYARAN --
======================================

-- Insert Pembayaran
CREATE PROCEDURE sp_insert_pembayaran (
  IN p_log_id INT,
  IN p_metode_pembayaran ENUM('cash', 'e-wallet', 'debit'),
  IN p_total_bayar DECIMAL(10,2)
)
BEGIN
  INSERT INTO Pembayaran (log_id, metode_pembayaran, total_bayar)
  VALUES (p_log_id, p_metode_pembayaran, p_total_bayar);
END;

-- Cara Memanggil
CALL sp_insert_pembayaran(2, 'cash', 25000.00);
SELECT * FROM Pembayaran ORDER BY pembayaran_id DESC LIMIT 1;

-- Read Pembayaran by ID
CREATE PROCEDURE sp_get_pembayaran_by_id (
  IN p_pembayaran_id INT
)
BEGIN
  SELECT * FROM Pembayaran WHERE pembayaran_id = p_pembayaran_id;
END;

-- Cara Memanggil
CALL sp_get_pembayaran_by_id(3);

-- Update Pembayaran
CREATE PROCEDURE sp_update_pembayaran (
  IN p_pembayaran_id INT,
  IN p_log_id INT,
  IN p_metode_pembayaran ENUM('cash', 'e-wallet', 'debit'),
  IN p_total_bayar DECIMAL(10,2)
)
BEGIN
  UPDATE Pembayaran
  SET log_id = p_log_id,
      metode_pembayaran = p_metode_pembayaran,
      total_bayar = p_total_bayar
  WHERE pembayaran_id = p_pembayaran_id;
END;

-- Cara Memanggil
CALL sp_update_pembayaran(2, 2, 'e-wallet', 30000.00);
SELECT * FROM Pembayaran WHERE pembayaran_id = 2;

-- Delete Pembayaran
CREATE PROCEDURE sp_delete_pembayaran (
  IN p_pembayaran_id INT
)
BEGIN
  DELETE FROM Pembayaran WHERE pembayaran_id = p_pembayaran_id;
END;

-- CARA MEMANGGIL --
CALL sp_delete_pembayaran(1);
SELECT * FROM Pembayaran WHERE pembayaran_id = 1;