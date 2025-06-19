package SistemReservasiParkir;
import java.sql.Timestamp;

public class KendaraanBelumKeluarView {
    private String platNomor;
    private String namaLengkap;
    private Timestamp waktuMasuk;

    public KendaraanBelumKeluarView(String platNomor, String namaLengkap, Timestamp waktuMasuk) {
        this.platNomor = platNomor;
        this.namaLengkap = namaLengkap;
        this.waktuMasuk = waktuMasuk;
    }

    public void tampilkan() {
        System.out.println("Plat: " + platNomor + " | Pengguna: " + namaLengkap + " | Masuk: " + waktuMasuk);
    }
}
