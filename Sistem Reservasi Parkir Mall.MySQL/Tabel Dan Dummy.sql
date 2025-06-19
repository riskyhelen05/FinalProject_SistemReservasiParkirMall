=============
 -- TABEL --
=============

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

==========
-- DATA --
==========

INSERT INTO user (nama_lengkap, email, no_hp, password, role) VALUES
('Andi Saputra', 'andi@gmail.com', '081234567890', 'password123', 'pengguna'),
('Amelia Putri', 'amel@gmail.com', '082345678901', 'password456', 'pengguna'),
('Admin Parkir 1', 'admin1parkir@gmail.com', '080000000000', 'adminpass1', 'admin'),
('Admin Parkir 2', 'admin2parkir@gmail.com', '080000000001', 'adminpass2', 'admin'),
('Admin Parkir 3', 'admin3parkir@gmail.com', '080000000002', 'adminpass3', 'admin');

INSERT INTO gedung (nama_gedung) values
('Gedung A'), ('Gedung B'), ('Gedung C');

INSERT INTO lantai_gedung (gedung_id, nomor_lantai) VALUES
(1, 1), (1, 2), (1, 3),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2), (3, 3);

INSERT INTO slot_parkir (kode_slot, tipe_slot, status_slot, lantai_gedung_id) VALUES
('A1-1', 'mobil', 'kosong', 1),
('B1-1', 'mobil', 'kosong', 1),
('A1-2', 'motor', 'kosong', 1),
('A1-3', 'electric_vehicle', 'kosong', 1),
('B2-1', 'mobil', 'kosong', 2),
('B2-2', 'motor', 'kosong', 3),
('B3-3', 'electric_vehicle', 'kosong', 3);

INSERT INTO kendaraan (plat_nomor, tipe_kendaraan, merk, warna, user_id) VALUES
('B1234ABC', 'mobil', 'Toyota Avanza', 'Hitam', 1),
('B5678XYZ', 'motor', 'Honda Vario', 'Merah', 2),
('B1122CD', 'mobil', 'Honda BR-V', 'Silver', 1),
('B3344EF', 'motor', 'Yamaha NMAX', 'Hitam', 2),
('B5566GH', 'mobil', 'Daihatsu Xenia', 'Putih', 2),
('B7788IJ', 'electric_vehicle', 'Wuling Air EV', 'Biru', 1),
('B9900KL', 'mobil', 'Suzuki Ertiga', 'Merah', 3),
('B2211MN', 'motor', 'Honda Beat', 'Putih', 4),
('B3322OP', 'electric_vehicle', 'Tesla Model 3', 'Hitam', 1),
('B4433QR', 'mobil', 'Toyota Yaris', 'Abu-abu', 2);

INSERT INTO log_parkir (kendaraan_id, slot_id, waktu_masuk, waktu_keluar) VALUES
(1, 1, '2025-06-02 08:05:00', '2025-06-02 11:00:00'),
(2, 3, '2025-06-02 09:10:00', '2025-06-02 10:30:00'),
(5, 2, '2025-06-02 11:10:00', NULL);

INSERT INTO pembayaran (log_id, metode_pembayaran, total_bayar) VALUES
(1, 'cash', 11000.00),
(2, 'e-wallet', 4000.00),
(3, 'debit', 6500.00);

INSERT INTO reservasi_parkir (user_id, slot_id, kendaraan_id, tanggal_reservasi, 
waktu_masuk, waktu_keluar, status_reservasi) VALUES
(1, 1, 1, '2025-06-02', '2025-06-02 08:00:00', '2025-06-02 11:00:00', 'selesai'),
(2, 3, 2, '2025-06-02', '2025-06-02 09:00:00', '2025-06-02 10:30:00', 'selesai'),
(1, 4, 6, '2025-06-02', '2025-06-02 10:30:00', NULL, 'batal'),
(2, 2, 5, '2025-06-02', '2025-06-02 11:00:00', NULL, 'aktif');

-- Tarif untuk Gedung A
INSERT INTO tarif_parkir (gedung_id, lantai_gedung_id, 
tipe_slot, tarif_awal, durasi_awal, tarif_per_jam_berikutnya) VALUES
(1, 1, 'mobil', 5000.00, 1, 3000.00),
(1, 2, 'motor', 2000.00, 1, 1000.00),
(1, 3, 'electric_vehicle', 7000.00, 1, 4000.00);

-- Tarif untuk Gedung B 
INSERT INTO tarif_parkir (gedung_id, lantai_gedung_id, 
tipe_slot, tarif_awal, durasi_awal, tarif_per_jam_berikutnya) VALUES
(2, 4, 'mobil', 5500.00, 1, 3200.00),
(2, 5, 'motor', 2500.00, 1, 1200.00),
(2, 6, 'electric_vehicle', 7500.00, 1, 4500.00);

-- Tarif untuk Gedung C
INSERT INTO tarif_parkir (gedung_id, lantai_gedung_id, 
tipe_slot, tarif_awal, durasi_awal, tarif_per_jam_berikutnya) VALUES
(3, 7, 'mobil', 6000.00, 1, 3500.00),
(3, 8, 'motor', 3000.00, 1, 1500.00),
(3, 9, 'electric_vehicle', 8000.00, 1, 5000.00);
