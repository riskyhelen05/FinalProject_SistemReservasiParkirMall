package SistemReservasiParkir;

public class LaporanSlotPerLantaiView {
    private String namaGedung;
    private int nomorLantai;
    private int totalSlot;
    private int jumlahKosong;
    private int jumlahTerisi;
    private int jumlahReservasi;

    public LaporanSlotPerLantaiView(String namaGedung, int nomorLantai, int totalSlot, int jumlahKosong, int jumlahTerisi, int jumlahReservasi) {
        this.namaGedung = namaGedung;
        this.nomorLantai = nomorLantai;
        this.totalSlot = totalSlot;
        this.jumlahKosong = jumlahKosong;
        this.jumlahTerisi = jumlahTerisi;
        this.jumlahReservasi = jumlahReservasi;
    }

    public void tampilkan() {
        System.out.println("Gedung: " + namaGedung + " | Lantai: " + nomorLantai +
            " | Total: " + totalSlot +
            " | Kosong: " + jumlahKosong +
            " | Terisi: " + jumlahTerisi +
            " | Reservasi: " + jumlahReservasi);
    }
}
