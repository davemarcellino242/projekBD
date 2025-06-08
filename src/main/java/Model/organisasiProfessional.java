package Model;

public class organisasiProfessional {
    private String organisasiProfessionaId;
    private String namaOrganisasiProfessional;

    public organisasiProfessional(String organisasiProfessionaId, String namaOrganisasiProfessional) {
        this.organisasiProfessionaId = organisasiProfessionaId;
        this.namaOrganisasiProfessional = namaOrganisasiProfessional;
    }

    public String getOrganisasiProfessionaId() {
        return organisasiProfessionaId;
    }

    public void setOrganisasiProfessionaId(String organisasiProfessionaId) {
        this.organisasiProfessionaId = organisasiProfessionaId;
    }

    public String getNamaOrganisasiProfessional() {
        return namaOrganisasiProfessional;
    }

    public void setNamaOrganisasiProfessional(String namaOrganisasiProfessional) {
        this.namaOrganisasiProfessional = namaOrganisasiProfessional;
    }
}
