package SistemReservasiParkir;

public class PenggunaDiAtasRataRataView {
     private String namaLengkap;
    private double totalBayar;

    public PenggunaDiAtasRataRataView(String namaLengkap, double totalBayar) {
        this.namaLengkap = namaLengkap;
        this.totalBayar = totalBayar;
    }

    public void tampilkan() {
        System.out.println("Nama: " + namaLengkap + " | Total Bayar: Rp" + totalBayar);
    }
}