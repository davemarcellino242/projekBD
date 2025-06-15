package Model;

public class ArsipPesertaEvent {
    private String nrp;
    private String nama;
    private String namaProgram;
    private String namaClub;
    private String statusRegistrasi;
    private String tanggalRegistrasi;
    private String statusHadir;
    private String sertifikatPeserta;
    private int kegiatanId;
    private String namaKegiatan;

    public ArsipPesertaEvent(String nrp, String nama, String namaProgram, String namaClub,
                             String statusRegistrasi, String tanggalRegistrasi,
                             String statusHadir, String sertifikatPeserta,
                             int kegiatanId, String namaKegiatan) {
        this.nrp = nrp;
        this.nama = nama;
        this.namaProgram = namaProgram;
        this.namaClub = namaClub;
        this.statusRegistrasi = statusRegistrasi;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.statusHadir = statusHadir;
        this.sertifikatPeserta = sertifikatPeserta;
        this.kegiatanId = kegiatanId;
        this.namaKegiatan = namaKegiatan;
    }

    // Getters
    public String getNrp() { return nrp; }
    public String getNama() { return nama; }
    public String getNamaProgram() { return namaProgram; }
    public String getNamaClub() { return namaClub; }
    public String getStatusRegistrasi() { return statusRegistrasi; }
    public String getTanggalRegistrasi() { return tanggalRegistrasi; }
    public String getStatusHadir() { return statusHadir; }
    public String getSertifikatPeserta() { return sertifikatPeserta; }
    public int getKegiatanId() { return kegiatanId; }
    public String getNamaKegiatan() { return namaKegiatan; }

    // Setters (opsional)
    public void setStatusHadir(String statusHadir) { this.statusHadir = statusHadir; }
    public void setSertifikatPeserta(String sertifikatPeserta) { this.sertifikatPeserta = sertifikatPeserta; }
}
