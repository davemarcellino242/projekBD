package Model;

public class kategoriClub {
    private int kategoriId;
    private String namaKategori;

    public kategoriClub(int kategoriId, String namaKategori) {
        this.kategoriId = kategoriId;
        this.namaKategori = namaKategori;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        kategoriId = kategoriId;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
}
