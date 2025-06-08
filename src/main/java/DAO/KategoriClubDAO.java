package DAO;

import Model.kategoriClub;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriClubDAO {
    private Connection conn;

    public KategoriClubDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertKategori(kategoriClub kategori) throws SQLException {
        String query = "INSERT INTO kategori_club (nama_kategori) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kategori.getNamaKategori());
            stmt.executeUpdate();

            // Ambil ID auto-increment yang baru dibuat
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                kategori.setKategoriId(rs.getInt(1));
            }
        }
    }

    public kategoriClub getKategoriById(int id) throws SQLException {
        String query = "SELECT * FROM kategori_club WHERE kategori_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new kategoriClub(
                        rs.getInt("kategori_id"),
                        rs.getString("nama_kategori")
                );
            }
        }
        return null;
    }

    public List<kategoriClub> getAllKategori() throws SQLException {
        List<kategoriClub> list = new ArrayList<>();
        String query = "SELECT * FROM kategori_club";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new kategoriClub(
                        rs.getInt("kategori_id"),
                        rs.getString("nama_kategori")
                ));
            }
        }
        return list;
    }

    public void updateKategori(kategoriClub kategori) throws SQLException {
        String query = "UPDATE kategori_club SET nama_kategori = ? WHERE kategori_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kategori.getNamaKategori());
            stmt.setInt(2, kategori.getKategoriId());
            stmt.executeUpdate();
        }
    }

    public void deleteKategori(int id) throws SQLException {
        String query = "DELETE FROM kategori_club WHERE kategori_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
