package Model;

public class fakultas {
    private String fakultasId;
    private String namaFakultas;

    public fakultas(String fakultasId, String namaFakultas) {
        this.fakultasId = fakultasId;
        this.namaFakultas = namaFakultas;
    }

    public String getFakultasId() {
        return fakultasId;
    }

    public void setFakultasId(String fakultasId) {
        this.fakultasId = fakultasId;
    }

    public String getNamaFakultas() {
        return namaFakultas;
    }

    public void setNamaFakultas(String namaFakultas) {
        this.namaFakultas = namaFakultas;
    }
}
