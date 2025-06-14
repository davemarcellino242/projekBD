package Model;

import java.time.LocalDate;

public class keanggotaan {
    private int keanggotaanId;
    private String peran;
    private String status;
    private LocalDate tanggalBergabung;
    private mahasiswa nrp;
    private club clubId;

    public keanggotaan(int keanggotaanId, String peran, String status, LocalDate tanggalBergabung, mahasiswa nrp, club clubId) {
        this.keanggotaanId = keanggotaanId;
        this.peran = peran;
        this.status = status;
        this.tanggalBergabung = tanggalBergabung;
        this.nrp = nrp;
        this.clubId = clubId;
    }

    public LocalDate getTanggalBergabung() {
        return tanggalBergabung;
    }

    public void setTanggalBergabung(LocalDate tanggalBergabung) {
        this.tanggalBergabung = tanggalBergabung;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeran() {
        return peran;
    }

    public void setPeran(String peran) {
        this.peran = peran;
    }

    public String getNrp() {
        return nrp.getNrp();
    }

    public void setNrp(mahasiswa nrp) {
        this.nrp = nrp;
    }

    public int getClubId() {
        return clubId.getClubId();
    }

    public void setClubId(club clubId) {
        this.clubId = clubId;
    }

    public int getKeanggotaanId() {
        return keanggotaanId;
    }

    public void setKeanggotaanId(int keanggotaanId) {
        this.keanggotaanId = keanggotaanId;
    }

    public mahasiswa getNrpObject() {
        return this.nrp;
    }
}
