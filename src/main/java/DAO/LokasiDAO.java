package DAO;

import Model.lokasi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LokasiDAO {
    private Connection conn;

    public LokasiDAO(Connection conn) {
        this.conn = conn;
    }

    public lokasi getLokasiById(int id) throws SQLException {
        String query = "SELECT * FROM lokasi WHERE lokasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new lokasi(
                        rs.getInt("lokasi_id"),
                        rs.getString("nama_lokasi"),
                        rs.getString("alamat")
                );
            }
        }
        return null;
    }

    public List<lokasi> getAllLokasi() throws SQLException {
        List<lokasi> list = new ArrayList<>();
        String query = "SELECT * FROM lokasi";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new lokasi(
                        rs.getInt("lokasi_id"),
                        rs.getString("nama_lokasi"),
                        rs.getString("alamat")
                ));
            }
        }
        return list;
    }

    public void insertLokasi(lokasi lks) throws SQLException {
        String query = "INSERT INTO lokasi (nama_lokasi, alamat) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, lks.getLokasiId());          // Lokasi ID harus diset manual
            stmt.setString(2, lks.getNamaLokasi());
            stmt.setString(3, lks.getAlamat());
            stmt.executeUpdate();
        }
    }

    public void updateLokasi(lokasi lks) throws SQLException {
        String query = "UPDATE lokasi SET nama_lokasi = ?, alamat = ? WHERE lokasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, lks.getNamaLokasi());
            stmt.setString(2, lks.getAlamat());
            stmt.setInt(3, lks.getLokasiId());
            stmt.executeUpdate();
        }
    }

    public void deleteLokasi(int id) throws SQLException {
        String query = "DELETE FROM lokasi WHERE lokasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
