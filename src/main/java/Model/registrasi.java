package Model;

import java.time.LocalDate;

public class registrasi {
    private int registrasiId;
    private String statusRegistrasi;
    private LocalDate tanggalRegistrasi;
    private mahasiswa nrp;
    private kegiatanClub kegiatanId;

    public registrasi(int registrasiId, String statusRegistrasi, LocalDate tanggalRegistrasi, mahasiswa nrp, kegiatanClub kegiatanId) {
        this.registrasiId = registrasiId;
        this.statusRegistrasi = statusRegistrasi;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.nrp = nrp;
        this.kegiatanId = kegiatanId;
    }

    public String getStatusRegistrasi() {
        return statusRegistrasi;
    }

    public void setStatusRegistrasi(String statusRegistrasi) {
        this.statusRegistrasi = statusRegistrasi;
    }

    public LocalDate getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }

    public void setTanggalRegistrasi(LocalDate tanggalRegistrasi) {
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    public String getNrp() {
        return nrp.getNrp();
    }

    public void setNrp(mahasiswa nrp) {
        this.nrp = nrp;
    }

    public int getKegiatanId() {
        return kegiatanId.getKegiatanId();
    }

    public void setKegiatanId(kegiatanClub kegiatanId) {
        this.kegiatanId = kegiatanId;
    }

    public int getRegistrasiId() {
        return registrasiId;
    }

    public void setRegistrasiId(int registrasiId) {
        this.registrasiId = registrasiId;
    }
}
