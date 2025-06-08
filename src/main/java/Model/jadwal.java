package Model;

import java.time.LocalDate;

public class jadwal {
    private int jadwalId;
    private LocalDate jadwalTanggal;
    private kegiatanClub kegiatanId;

    public jadwal(int jadwalId, LocalDate jadwalTanggal, kegiatanClub kegiatanId) {
        this.jadwalId = jadwalId;
        this.jadwalTanggal = jadwalTanggal;
        this.kegiatanId = kegiatanId;
    }

    public int getJadwalId() {
        return jadwalId;
    }

    public void setJadwalId(int jadwalId) {
        this.jadwalId = jadwalId;
    }

    public LocalDate getJadwalTanggal() {
        return jadwalTanggal;
    }

    public void setJadwalTanggal(LocalDate jadwalTanggal) {
        this.jadwalTanggal = jadwalTanggal;
    }

    public int getKegiatanId() {
        return kegiatanId.getKegiatanId();
    }

    public void setKegiatanId(kegiatanClub kegiatanId) {
        this.kegiatanId = kegiatanId;
    }
}
