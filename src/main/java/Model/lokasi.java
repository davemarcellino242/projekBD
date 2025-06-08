package Model;

public class lokasi {
    private int lokasiId;
    private String namaLokasi;
    private String alamat;

    public lokasi(int lokasiId, String namaLokasi, String alamat) {
        this.lokasiId = lokasiId;
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
    }

    public int getLokasiId() {
        return lokasiId;
    }

    public void setLokasiId(int lokasiId) {
        this.lokasiId = lokasiId;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
