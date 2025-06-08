package Model;

public class jenisKegiatan {
    private int jenisKegiatanId;
    private String namaJenisKegiatan;

    public jenisKegiatan(int jenisKegiatanId, String namaJenisKegiatan) {
        this.jenisKegiatanId = jenisKegiatanId;
        this.namaJenisKegiatan = namaJenisKegiatan;
    }

    public int getJenisKegiatanId() {
        return jenisKegiatanId;
    }

    public void setJenisKegiatanId(int jenisKegiatanId) {
        this.jenisKegiatanId = jenisKegiatanId;
    }

    public String getNamaJenisKegiatan() {
        return namaJenisKegiatan;
    }

    public void setNamaJenisKegiatan(String namaJenisKegiatan) {
        this.namaJenisKegiatan = namaJenisKegiatan;
    }
}
