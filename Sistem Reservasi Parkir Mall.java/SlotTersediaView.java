package SistemReservasiParkir;

public class SlotTersediaView {
    private int slotId;
    private String kodeSlot;
    private String tipeSlot;
    private String statusSlot;
    private int nomorLantai;
    private String namaGedung;

    public SlotTersediaView(int slotId, String kodeSlot, String tipeSlot, String statusSlot, int nomorLantai, String namaGedung) {
        this.slotId = slotId;
        this.kodeSlot = kodeSlot;
        this.tipeSlot = tipeSlot;
        this.statusSlot = statusSlot;
        this.nomorLantai = nomorLantai;
        this.namaGedung = namaGedung;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getKodeSlot() {
        return kodeSlot;
    }

    public void setKodeSlot(String kodeSlot) {
        this.kodeSlot = kodeSlot;
    }

    public String getTipeSlot() {
        return tipeSlot;
    }

    public void setTipeSlot(String tipeSlot) {
        this.tipeSlot = tipeSlot;
    }

    public String getStatusSlot() {
        return statusSlot;
    }

    public void setStatusSlot(String statusSlot) {
        this.statusSlot = statusSlot;
    }

    public int getNomorLantai() {
        return nomorLantai;
    }

    public void setNomorLantai(int nomorLantai) {
        this.nomorLantai = nomorLantai;
    }

    public String getNamaGedung() {
        return namaGedung;
    }

    public void setNamaGedung(String namaGedung) {
        this.namaGedung = namaGedung;
    }
}
