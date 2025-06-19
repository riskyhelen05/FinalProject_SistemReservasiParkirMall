===============================================
-- CRUD TABLE MASTER DENGAN STORED PROCEDURE--
===============================================

======================================
-- STORED PROCEDURE UNTUK CRUD USER --
======================================

-- CREATE USER --
CREATE PROCEDURE sp_create_user(
  IN p_nama VARCHAR(100), IN p_email VARCHAR(100), IN p_no_hp VARCHAR(20),
  IN p_password VARCHAR(100), IN p_role ENUM('admin','pengguna')
)
BEGIN
  INSERT INTO User (nama_lengkap, email, no_hp, password, role)
  VALUES (p_nama, p_email, p_no_hp, p_password, p_role);
END;

-- CARA MEMANGGIL --
CALL sp_create_user('Budi', 'budi@gmail.com', '08123456789', 'pass123', 'pengguna');
SELECT * FROM User WHERE email = 'budi@gmail.com';

-- READ USER --
CREATE PROCEDURE sp_read_user()
BEGIN
  SELECT * FROM User;
END;

-- CARA MEMANGGIL --
CALL sp_read_user();

-- UPDATE USER --
CREATE PROCEDURE sp_update_user(
  IN p_user_id INT, IN p_nama VARCHAR(100), IN p_email VARCHAR(100),
  IN p_no_hp VARCHAR(20), IN p_password VARCHAR(100), IN p_role ENUM('admin','pengguna')
)
BEGIN
  UPDATE User SET nama_lengkap=p_nama, email=p_email, no_hp=p_no_hp,
  password=p_password, role=p_role WHERE user_id=p_user_id;
END;

-- CARA MEMANGGIL --
CALL sp_update_user(1, 'Andi Saputra', 'andi@gmail.com', '081234567890', 'passbaru123', 'pengguna');
SELECT * FROM User WHERE user_id = 1;

-- DELETE USER --
CREATE PROCEDURE sp_delete_user(IN p_user_id INT)
BEGIN
  DELETE FROM User WHERE user_id = p_user_id;
END;

-- CARA MEMANGGIL --
CALL sp_delete_user(5);
SELECT * FROM User WHERE user_id = 5;

========================================
-- STORED PROCEDURE UNTUK CRUD GEDUNG --
========================================
 
-- CREATE GEDUNG --
CREATE PROCEDURE sp_create_gedung(IN p_nama VARCHAR(100))
BEGIN INSERT INTO Gedung(nama_gedung) VALUES(p_nama); END;

-- CARA MEMANGGIL --
CALL sp_create_gedung('Gedung A');
SELECT * FROM Gedung WHERE nama_gedung = 'Gedung A';

-- READ GEDUNG --
CREATE PROCEDURE sp_read_gedung() BEGIN SELECT * FROM Gedung; END;

-- CARA MEMANGGIL --
CALL sp_read_gedung();

-- UPDATE GEDUNG --
CREATE PROCEDURE sp_update_gedung(IN p_id INT, IN p_nama VARCHAR(100))
BEGIN UPDATE Gedung SET nama_gedung=p_nama WHERE gedung_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_update_gedung(1, 'Gedung A - Update');
SELECT * FROM Gedung WHERE gedung_id = 1;

-- DELETE GEDUNG --
CREATE PROCEDURE sp_delete_gedung(IN p_id INT)
BEGIN DELETE FROM Gedung WHERE gedung_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_delete_gedung(2);
SELECT * FROM Gedung WHERE gedung_id = 2;

===============================================
-- STORED PROCEDURE UNTUK CRUD LANTAI_GEDUNG --
===============================================

-- CREATE LANTAI_GEDUNG --
CREATE PROCEDURE sp_create_lantai(IN p_gedung_id INT, IN p_nomor INT)
BEGIN INSERT INTO Lantai_Gedung(gedung_id, nomor_lantai) VALUES(p_gedung_id, p_nomor); END;

-- CARA MEMANGGIL --
CALL sp_create_lantai(1, 1);
SELECT * FROM Lantai_Gedung WHERE gedung_id = 1 AND nomor_lantai = 1;

-- READ LANTAI_GEDUNG --
CREATE PROCEDURE sp_read_lantai() BEGIN SELECT * FROM Lantai_Gedung; END;

-- CARA MEMANGGIL --
CALL sp_read_lantai();

-- UPDATE LANTAI_GEDUNG --
CREATE PROCEDURE sp_update_lantai(IN p_id INT, IN p_gedung_id INT, IN p_nomor INT)
BEGIN UPDATE Lantai_Gedung SET gedung_id=p_gedung_id, nomor_lantai=p_nomor WHERE lantai_gedung_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_update_lantai(1, 1, 2);
SELECT * FROM Lantai_Gedung WHERE lantai_gedung_id = 1;

-- DELETE LANTAI_GEDUNG --
CREATE PROCEDURE sp_delete_lantai(IN p_id INT)
BEGIN DELETE FROM Lantai_Gedung WHERE lantai_gedung_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_delete_lantai(7);
SELECT * FROM Lantai_Gedung WHERE lantai_gedung_id = 7;

=============================================
-- STORED PROCEDURE UNTUK CRUD SLOT_PARKIR --
=============================================

-- CREATE SLOT_PARKIR --
CREATE PROCEDURE sp_create_slot(
  IN p_kode VARCHAR(10), IN p_tipe ENUM('mobil','motor','electric_vehicle'),
  IN p_status ENUM('kosong','terisi','reservasi'), IN p_lantai_id INT)
BEGIN
  INSERT INTO Slot_Parkir(kode_slot, tipe_slot, status_slot, lantai_gedung_id)
  VALUES(p_kode, p_tipe, p_status, p_lantai_id);
END;

-- CARA MEMANGGIL --
CALL sp_create_slot('A1', 'mobil', 'kosong', 1);
SELECT * FROM Slot_Parkir WHERE kode_slot = 'A1';

 -- READ SLOT_PARKIR --
CREATE PROCEDURE sp_read_slot() BEGIN SELECT * FROM Slot_Parkir; END;

-- CARA MEMANGGIL --
CALL sp_read_slot();

-- UPDATE SLOT_PARKIR --
CREATE PROCEDURE sp_update_slot(
  IN p_id INT, IN p_kode VARCHAR(10), IN p_tipe ENUM('mobil','motor','electric_vehicle'),
  IN p_status ENUM('kosong','terisi','reservasi'), IN p_lantai_id INT)
BEGIN
  UPDATE Slot_Parkir SET kode_slot=p_kode, tipe_slot=p_tipe,
  status_slot=p_status, lantai_gedung_id=p_lantai_id WHERE slot_id=p_id;
END;

-- CARA MEMANGGIL --
CALL sp_update_slot(3, 'A2', 'mobil', 'kosong', 1);
SELECT * FROM Slot_Parkir WHERE slot_id = 3;

-- DELETE SLOT_PARKIR --
CREATE PROCEDURE sp_delete_slot(IN p_id INT)
BEGIN DELETE FROM Slot_Parkir WHERE slot_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_delete_slot(5);
SELECT * FROM Slot_Parkir WHERE slot_id = 5;

===========================================
-- STORED PROCEDURE UNTUK CRUD KENDARAAN --
===========================================

-- CREATE KENDARAAN --
CREATE PROCEDURE sp_create_kendaraan(
  IN p_user_id INT,
  IN p_plat_nomor VARCHAR(20),
  IN p_tipe ENUM('mobil','motor','electric_vehicle'),
  IN p_warna VARCHAR(30),
  IN p_merk VARCHAR(50)
)
BEGIN
  INSERT INTO kendaraan (user_id, plat_nomor, tipe_kendaraan, warna, merk)
  VALUES (p_user_id, p_plat_nomor, p_tipe, p_warna, p_merk);
END;

-- CARA MEMANGGIL --
CALL sp_create_kendaraan(1, 'B1234XYZ', 'mobil', 'hitam', 'Toyota');
SELECT * FROM kendaraan WHERE plat_nomor = 'B1234XYZ';

-- READ KENDARAAN --
CREATE PROCEDURE sp_read_kendaraan()
BEGIN
  SELECT * FROM kendaraan;
END;

-- CARA MEMANGGIL --
CALL sp_read_kendaraan();

-- UPDATE KENDARAAN --
CREATE PROCEDURE sp_update_kendaraan(
  IN p_kendaraan_id INT,
  IN p_plat_nomor VARCHAR(20),
  IN p_tipe ENUM('mobil','motor','electric_vehicle'),
  IN p_warna VARCHAR(30),
  IN p_merk VARCHAR(50)
)
BEGIN
  UPDATE kendaraan
  SET plat_nomor = p_plat_nomor,
      tipe_kendaraan = p_tipe,
      warna = p_warna,
      merk = p_merk
  WHERE kendaraan_id = p_kendaraan_id;
END;

-- CARA MEMANGGIL --
CALL sp_update_kendaraan(1, 'B1234ABC', 'mobil', 'merah', 'Honda');
SELECT * FROM kendaraan WHERE kendaraan_id = 1;

-- DELETE KENDARAAN --
CREATE PROCEDURE sp_delete_kendaraan(
  IN p_kendaraan_id INT
)
BEGIN
  DELETE FROM kendaraan WHERE kendaraan_id = p_kendaraan_id;
END;

-- CARA MEMANGGIL --
CALL sp_delete_kendaraan(3);
SELECT * FROM kendaraan WHERE kendaraan_id = 3;

==============================================
-- STORED PROCEDURE UNTUK CRUD TARIF_PARKIR --
==============================================

 -- CREATE TARIF_PARKIR --
CREATE PROCEDURE sp_create_tarif(
  IN p_tipe ENUM('mobil','motor','electric_vehicle'), IN p_awal DECIMAL(10,2),
  IN p_durasi INT, IN p_per_jam DECIMAL(10,2), IN p_gedung INT, IN p_lantai INT)
BEGIN
  INSERT INTO Tarif_Parkir(tipe_slot, tarif_awal, durasi_awal,
  tarif_per_jam_berikutnya, gedung_id, lantai_gedung_id)
  VALUES(p_tipe, p_awal, p_durasi, p_per_jam, p_gedung, p_lantai);
END;

-- CARA MEMANGGIL --
CALL sp_create_tarif('mobil', 3000.00, 1, 2000.00, 1, 1);
SELECT * FROM Tarif_Parkir WHERE gedung_id = 1 AND lantai_gedung_id = 1 AND tipe_slot = 'mobil';

-- READ TARIF_PARKIR --
CREATE PROCEDURE sp_read_tarif() BEGIN SELECT * FROM Tarif_Parkir; END;

-- CARA MEMANGGIL --
CALL sp_read_tarif();

-- UPDATE TARIF_PARKIR --
CREATE PROCEDURE sp_update_tarif(
  IN p_id INT, IN p_tipe ENUM('mobil','motor','electric_vehicle'), IN p_awal DECIMAL(10,2),
  IN p_durasi INT, IN p_per_jam DECIMAL(10,2), IN p_gedung INT, IN p_lantai INT)
BEGIN
  UPDATE Tarif_Parkir SET tipe_slot=p_tipe, tarif_awal=p_awal,
  durasi_awal=p_durasi, tarif_per_jam_berikutnya=p_per_jam,
  gedung_id=p_gedung, lantai_gedung_id=p_lantai WHERE tarif_parkir_id=p_id;
END;

-- CARA MEMANGGIL --
CALL sp_update_tarif(1, 'mobil', 4000.00, 2, 2500.00, 1, 1);
SELECT * FROM Tarif_Parkir WHERE tarif_parkir_id = 1;

-- DELETE TARIF_PARKIR --
CREATE PROCEDURE sp_delete_tarif(IN p_id INT)
BEGIN DELETE FROM Tarif_Parkir WHERE tarif_parkir_id=p_id; END;

-- CARA MEMANGGIL --
CALL sp_delete_tarif(1);
SELECT * FROM Tarif_Parkir WHERE tarif_parkir_id = 1;