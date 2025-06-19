package SistemReservasiParkir;
import java.util.Date;

public class LogParkirView {
    private int logId;
    private String namaPengguna;  // Tambahan
    private String platNomor;
    private String kodeSlot;
    private Date waktuMasuk;
    private Date waktuKeluar;

    public LogParkirView(int logId, String namaPengguna, String platNomor, String kodeSlot,
                         Date waktuMasuk, Date waktuKeluar) {
        this.logId = logId;
        this.namaPengguna = namaPengguna;
        this.platNomor = platNomor;
        this.kodeSlot = kodeSlot;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
    }

    // Getter & Setter untuk namaPengguna
    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    // Getter & Setter lainnya
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public Date getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(Date waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public Date getWaktuKeluar() {
        return waktuKeluar;
    }

    public void setWaktuKeluar(Date waktuKeluar) {
        this.waktuKeluar = waktuKeluar;
    }
}
