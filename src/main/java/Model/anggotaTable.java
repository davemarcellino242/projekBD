package Model;

public class anggotaTable {
    private mahasiswa mhs;
    private keanggotaan kg;

    public anggotaTable(keanggotaan kg) {
        this.kg = kg;
        this.mhs = kg.getNrpObject(); // ambil data mahasiswa dari dalam keanggotaan
    }

    public mahasiswa getMhs() {
        return mhs;
    }

    public keanggotaan getKg() {
        return kg;
    }

    public void setStatus(String newStatus) {
        kg.setStatus(newStatus);
    }

    public void setPeran(String newPeran) {
        kg.setPeran(newPeran);
    }

    public String getStatus() {
        return kg.getStatus();
    }

    public String getPeran() {
        return kg.getPeran();
    }
}
