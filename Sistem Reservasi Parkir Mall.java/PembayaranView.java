package SistemReservasiParkir;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PembayaranView {
    private int pembayaranId;
    private String namaPengguna;
    private String platNomor;
    private String kodeSlot;
    private Timestamp waktuMulai;
    private Timestamp waktuSelesai;
    private BigDecimal totalBayar;
    private String metodePembayaran;
    private Timestamp waktuPembayaran;

    public PembayaranView(int pembayaranId, String namaPengguna, String platNomor, String kodeSlot,
                          Timestamp waktuMulai, Timestamp waktuSelesai, BigDecimal totalBayar,
                          String metodePembayaran, Timestamp waktuPembayaran) {
        this.pembayaranId = pembayaranId;
        this.namaPengguna = namaPengguna;
        this.platNomor = platNomor;
        this.kodeSlot = kodeSlot;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.waktuPembayaran = waktuPembayaran;
    }

    public int getPembayaranId() {
        return pembayaranId;
    }

    public void setPembayaranId(int pembayaranId) {
        this.pembayaranId = pembayaranId;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public void setPlatNomor(String platNomor) {
        this.platNomor = platNomor;
    }

    public String getKodeSlot() {
        return kodeSlot;
    }

    public void setKodeSlot(String kodeSlot) {
        this.kodeSlot = kodeSlot;
    }

    public Timestamp getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(Timestamp waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public Timestamp getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Timestamp waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public BigDecimal getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(BigDecimal totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public Timestamp getWaktuPembayaran() {
        return waktuPembayaran;
    }

    public void setWaktuPembayaran(Timestamp waktuPembayaran) {
        this.waktuPembayaran = waktuPembayaran;
    }
}
