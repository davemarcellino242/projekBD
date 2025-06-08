package Model;

public class kegiatanClub {
    private int kegiatanId;
    private String namaKegiatan;
    private club clubId;
    private lokasi lokasiId;
    private jenisKegiatan jenisKegiatanId;

    public kegiatanClub(int kegiatanId, String namaKegiatan, club clubId, lokasi lokasiId, jenisKegiatan jenisKegiatanId) {
        this.kegiatanId = kegiatanId;
        this.namaKegiatan = namaKegiatan;
        this.clubId = clubId;
        this.lokasiId = lokasiId;
        this.jenisKegiatanId = jenisKegiatanId;
    }

    public int getKegiatanId() {
        return kegiatanId;
    }

    public void setKegiatanId(int kegiatanId) {
        this.kegiatanId = kegiatanId;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public int getClubId() {
        return clubId.getClubId();
    }

    public void setClubId(club clubId) {
        this.clubId = clubId;
    }

    public int getLokasiId() {
        return lokasiId.getLokasiId();
    }

    public void setLokasiId(lokasi lokasiId) {
        this.lokasiId = lokasiId;
    }

    public int getJenisKegiatanId() {
        return jenisKegiatanId.getJenisKegiatanId();
    }

    public void setJenisKegiatanId(jenisKegiatan jenisKegiatanId) {
        this.jenisKegiatanId = jenisKegiatanId;
    }
}
