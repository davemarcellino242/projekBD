package Model;

public class pesertaEvent {
    private String nrp;
    private String nama;
    private String program;
    private String statusHadir;
    private String tanggalRegist;
    private String sertifikat;

    public pesertaEvent(String nrp, String nama, String program, String statusHadir, String tanggalRegist, String sertifikat) {
        this.nrp = nrp;
        this.nama = nama;
        this.program = program;
        this.statusHadir = statusHadir;
        this.tanggalRegist = tanggalRegist;
        this.sertifikat = sertifikat;
    }

    public String getNrp() { return nrp; }
    public String getNama() { return nama; }
    public String getProgram() { return program; }
    public String getStatusHadir() { return statusHadir; }
    public String getTanggalRegist() { return tanggalRegist; }
    public String getSertifikat() { return sertifikat; }

    public void setStatusHadir(String statusHadir) { this.statusHadir = statusHadir; }
    public void setSertifikat(String sertifikat) { this.sertifikat = sertifikat; }
}
