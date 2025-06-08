package Model;

public class pesertaKegiatan {
    private int pesertaKegiatanId;
    private String statusHadir;
    private String sertifikatPeserta;
    private registrasi registrasiId;

    public pesertaKegiatan(int pesertaKegiatanId, String statusHadir, String sertifikatPeserta, registrasi registrasiId) {
        this.pesertaKegiatanId = pesertaKegiatanId;
        this.statusHadir = statusHadir;
        this.sertifikatPeserta = sertifikatPeserta;
        this.registrasiId = registrasiId;
    }

    public int getPesertaKegiatanId() {
        return pesertaKegiatanId;
    }

    public void setPesertaKegiatanId(int pesertaKegiatanId) {
        this.pesertaKegiatanId = pesertaKegiatanId;
    }

    public String getStatusHadir() {
        return statusHadir;
    }

    public void setStatusHadir(String statusHadir) {
        this.statusHadir = statusHadir;
    }

    public String getSertifikatPeserta() {
        return sertifikatPeserta;
    }

    public void setSertifikatPeserta(String sertifikatPeserta) {
        this.sertifikatPeserta = sertifikatPeserta;
    }

    public int getRegistrasiId() {
        return registrasiId.getRegistrasiId();
    }

    public void setRegistrasiId(registrasi registrasiId) {
        this.registrasiId = registrasiId;
    }
}
