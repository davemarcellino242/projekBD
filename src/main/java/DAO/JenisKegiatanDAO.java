package DAO;

import Model.jenisKegiatan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JenisKegiatanDAO {
    private Connection conn;

    public JenisKegiatanDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertJenisKegiatan(jenisKegiatan jk) throws SQLException {
        String query = "INSERT INTO jenis_kegiatan (nama_jenis_kegiatan) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, jk.getNamaJenisKegiatan());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                jk.setJenisKegiatanId(rs.getInt(1)); // Set id yang di-generate
            }
        }
    }

    public jenisKegiatan getJenisKegiatanById(int id) throws SQLException {
        String query = "SELECT * FROM jenis_kegiatan WHERE jenis_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new jenisKegiatan(
                        rs.getInt("jenis_kegiatan_id"),
                        rs.getString("nama_jenis_kegiatan")
                );
            }
        }
        return null;
    }

    public List<jenisKegiatan> getAllJenisKegiatan() throws SQLException {
        List<jenisKegiatan> list = new ArrayList<>();
        String query = "SELECT * FROM jenis_kegiatan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new jenisKegiatan(
                        rs.getInt("jenis_kegiatan_id"),
                        rs.getString("nama_jenis_kegiatan")
                ));
            }
        }
        return list;
    }

    public void updateJenisKegiatan(jenisKegiatan jk) throws SQLException {
        String query = "UPDATE jenis_kegiatan SET nama_jenis_kegiatan = ? WHERE jenis_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, jk.getNamaJenisKegiatan());
            stmt.setInt(2, jk.getJenisKegiatanId());
            stmt.executeUpdate();
        }
    }

    public void deleteJenisKegiatan(int id) throws SQLException {
        String query = "DELETE FROM jenis_kegiatan WHERE jenis_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Hapus method generate manual, tidak dibutuhkan lagi karena pakai SERIAL
}
