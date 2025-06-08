package Model;

public class club {
    private int clubId;
    private String namaClub;
    private String deskripsiClub;
    private int tahunBerdiriClub;
    private kategoriClub kategoriId;
    private pendiriClub pendiriClubId;

    public club(int clubId, String namaClub, String deskripsiClub, int tahunBerdiriClub, kategoriClub kategoriId, pendiriClub pendiriClubId) {
        this.clubId = clubId;
        this.namaClub = namaClub;
        this.deskripsiClub = deskripsiClub;
        this.tahunBerdiriClub = tahunBerdiriClub;
        this.kategoriId = kategoriId;
        this.pendiriClubId = pendiriClubId;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getNamaClub() {
        return namaClub;
    }

    public void setNamaClub(String namaClub) {
        this.namaClub = namaClub;
    }

    public String getDeskripsiClub() {
        return deskripsiClub;
    }

    public void setDeskripsiClub(String deskripsiClub) {
        this.deskripsiClub = deskripsiClub;
    }

    public int getTahunBerdiriClub() {
        return tahunBerdiriClub;
    }

    public void setTahunBerdiriClub(int tahunBerdiriClub) {
        this.tahunBerdiriClub = tahunBerdiriClub;
    }

    public int getKategoriId() {
        return kategoriId.getKategoriId();
    }

    public void setKategoriId(kategoriClub kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getPendiriClubId() {
        return pendiriClubId.getPendiriClubId();
    }

    public void setPendiriClubId(pendiriClub pendiriClubId) {
        this.pendiriClubId = pendiriClubId;
    }
}
