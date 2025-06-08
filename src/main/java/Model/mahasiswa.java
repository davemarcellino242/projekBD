package Model;

import java.time.LocalDate;

public class mahasiswa {
    private String nrp;
    private String nama;
    private String email;
    private LocalDate tanggalLahir;
    private program programID;  // ini adalah objek dari class program
    private boolean signup;

    public mahasiswa(String nrp, String nama, String email, LocalDate tanggalLahir, program programID, boolean signup) {
        this.nrp = nrp;
        this.nama = nama;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.programID = programID;
        this.signup = signup;
    }

    // Getter & Setter
    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public program getProgram() {
        return programID;
    }

    public void setProgram(program programID) {
        this.programID = programID;
    }

    public String getProgramID() {
        return programID.getProgramId();
    }

    public boolean isSignup() {
        return signup;
    }

    public void setSignup(boolean signup) {
        this.signup = signup;
    }

    public String getNamaProgram() {
        return programID.getNamaProgram(); // misalnya kamu punya ini
    }

    public String getNamaFakultas() {
        return programID.getStudi().getFakultas().getNamaFakultas();
    }

    public String getNamaProgramStudi() {
        return programID.getStudi().getNamaProgramStudi();
    }


}
