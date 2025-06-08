package Model;

public class programStudi {
    private String programStudiId;
    private String namaProgramStudi;
    private fakultas fakultasId;

    public programStudi(String programStudiId, String namaProgramStudi, fakultas fakultasId) {
        this.programStudiId = programStudiId;
        this.namaProgramStudi = namaProgramStudi;
        this.fakultasId = fakultasId;
    }

    public String getProgramStudiId() {
        return programStudiId;
    }

    public void setProgramStudiId(String programStudiId) {
        this.programStudiId = programStudiId;
    }

    public String getNamaProgramStudi() {
        return namaProgramStudi;
    }

    public void setNamaProgramStudi(String namaProgramStudi) {
        this.namaProgramStudi = namaProgramStudi;
    }

    public String getFakultasId() {
        return fakultasId.getFakultasId();
    }

    public void setFakultasId(fakultas fakultasId) {
        this.fakultasId = fakultasId;
    }
}
