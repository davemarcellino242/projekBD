package Model;

public class pendiriClub {
    private String pendiriClubId;
    private String namaPendirClub;
    private organisasiProfessional organisasiProfessionalId;

    public pendiriClub(String pendiriClubId, String namaPendirClub, organisasiProfessional organisasiProfessionalId) {
        this.pendiriClubId = pendiriClubId;
        this.namaPendirClub = namaPendirClub;
        this.organisasiProfessionalId = organisasiProfessionalId;
    }

    public String getPendiriClubId() {
        return pendiriClubId;
    }

    public void setPendiriClubId(String pendiriClubId) {
        this.pendiriClubId = pendiriClubId;
    }

    public String getNamaPendirClub() {
        return namaPendirClub;
    }

    public void setNamaPendirClub(String namaPendirClub) {
        this.namaPendirClub = namaPendirClub;
    }

    public String getOrganisasiProfessionalId() {
        return organisasiProfessionalId.getOrganisasiProfessionaId();
    }

    public void setOrganisasiProfessionalId(organisasiProfessional organisasiProfessionalId) {
        this.organisasiProfessionalId = organisasiProfessionalId;
    }
}
