package SistemReservasiParkir;

import java.sql.Timestamp;

public class ReservasiAktifView {
    private int reservasiId;
    private String namaUser;
    private String kodeSlot;
    private String platNomor;
    private Timestamp tanggalReservasi;
    private Timestamp waktuMasuk;
    private Timestamp waktuKeluar;
    private String statusReservasi;

    // Constructor Lengkap
    public ReservasiAktifView(int reservasiId, String namaUser, String kodeSlot, String platNomor,
                              Timestamp tanggalReservasi, Timestamp waktuMasuk,
                              Timestamp waktuKeluar, String statusReservasi) {
        this.reservasiId = reservasiId;
        this.namaUser = namaUser;
        this.kodeSlot = kodeSlot;
        this.platNomor = platNomor;
        this.tanggalReservasi = tanggalReservasi;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
        this.statusReservasi = statusReservasi;
    }

    // Constructor Ringkas (khusus untuk vw_reservasi_aktif)
    public ReservasiAktifView(int reservasiId, String namaUser, String platNomor, String kodeSlot,
                              Timestamp waktuMasuk, Timestamp waktuKeluar) {
        this.reservasiId = reservasiId;
        this.namaUser = namaUser;
        this.platNomor = platNomor;
        this.kodeSlot = kodeSlot;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
    }

    // Getter & Setter
    public int getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(int reservasiId) {
        this.reservasiId = reservasiId;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getKodeSlot() {
        return kodeSlot;
    }

    public void setKodeSlot(String kodeSlot) {
        this.kodeSlot = kodeSlot;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public Timestamp getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(Timestamp tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public Timestamp getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(Timestamp waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public Timestamp getWaktuKeluar() {
        return waktuKeluar;
    }

    public void setWaktuKeluar(Timestamp waktuKeluar) {
        this.waktuKeluar = waktuKeluar;
    }

    public String getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(String statusReservasi) {
        this.statusReservasi = statusReservasi;
    }
}
