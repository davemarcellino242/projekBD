package Model;

public class program {
    private String programId;
    private String namaProgram;
    private programStudi programStudiId;

    public program(String programId, String namaProgram, programStudi programStudiId) {
        this.programId = programId;
        this.namaProgram= namaProgram;
        this.programStudiId = programStudiId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getNamaProgram() {
        return namaProgram;
    }

    public void setNamaProgram(String namaProgram) {
        this.namaProgram = namaProgram;
    }

    public String getProgramStudiId() {
        return programStudiId.getProgramStudiId();
    }

    public void setProgramStudiId(programStudi programStudiId) {
        this.programStudiId = programStudiId;
    }

    @Override
    public String toString() {
        return namaProgram;
    }

    public programStudi getStudi() {
        return programStudiId;
    }
}
