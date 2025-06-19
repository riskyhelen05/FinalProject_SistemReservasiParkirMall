======================
-- STORED PROSEDURE --
======================

==================================================
-- 1. Hitung total bayar berdasarkan log parkir --
==================================================

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

==============================================
-- 2. Prosedur hitung dan simpan pembayaran --
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

=============================================
-- 3. Prosedur mulai parkir dari reservasi --
=============================================

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

===========================================================
-- 4. Prosedur proses pembayaran (menghitung dan simpan) --
===========================================================

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

=========================================
-- 5. Prosedur manual isi waktu keluar --
=========================================

CREATE PROCEDURE insert_keluar_manual(
  IN p_log_id INT,
  IN p_waktu_keluar DATETIME
)
BEGIN
  UPDATE log_parkir
  SET waktu_keluar = p_waktu_keluar
  WHERE log_id = p_log_id;
END;

-- Cara memanggil --
CALL insert_keluar_manual(1, NOW());
SELECT * FROM log_parkir WHERE log_id = 1;