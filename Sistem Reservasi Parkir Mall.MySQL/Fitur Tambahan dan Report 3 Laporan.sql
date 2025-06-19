====================
-- FITUR TAMBAHAN --
====================

===================
-- 1. EKSPOR CSV --
===================

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

=================================
-- 2. LAPORAN STATISTIK HARIAN --
=================================

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

==================================
-- 3. LAPORAN STATISTIK BULANAN --
==================================

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
CALL laporan_statistik_bulanan();

=============================================
-- REPORT MENGGUNAKAN 3 JENIS BUAH LAPORAN --
=============================================

===============================
-- 1. CROSSTAB (PIVOT TABLE) --
===============================
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

===================================
-- CTE (COMMON TABLE EXPRESSION) --
===================================
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

==============
-- Subquery --
==============
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