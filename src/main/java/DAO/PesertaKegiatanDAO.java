package DAO;

import Model.pesertaKegiatan;
import Model.programStudi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PesertaKegiatanDAO {
    private Connection conn;

    public PesertaKegiatanDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertPeserta(pesertaKegiatan pk) throws SQLException {
        String query = "INSERT INTO peserta_kegiatan (status_hadir, sertifikat_peserta, registrasi_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pk.getStatusHadir());
            stmt.setString(2, pk.getSertifikatPeserta());
            stmt.setInt(3, pk.getRegistrasiId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pk.setPesertaKegiatanId(rs.getInt(1));
            }
        }
    }

    public pesertaKegiatan getPesertaById(int id) throws SQLException {
        String query = "SELECT * FROM peserta_kegiatan WHERE peserta_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int registrasiId = rs.getInt("registrasi_id");
                RegistrasiDAO registrasiDAO = new RegistrasiDAO(conn);
                Model.registrasi regis = registrasiDAO.getRegistrasiById(registrasiId);
                return new pesertaKegiatan(
                        rs.getInt("peserta_kegiatan_id"),
                        rs.getString("status_hadir"),
                        rs.getString("sertifikat_peserta"),
                        regis
                );
            }
        }
        return null;
    }

    public List<pesertaKegiatan> getAllPeserta() throws SQLException {
        List<pesertaKegiatan> list = new ArrayList<>();
        String query = "SELECT * FROM peserta_kegiatan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int registrasiId = rs.getInt("registrasi_id");
                RegistrasiDAO registrasiDAO = new RegistrasiDAO(conn);
                Model.registrasi regis = registrasiDAO.getRegistrasiById(registrasiId);
                list.add(new pesertaKegiatan(
                        rs.getInt("peserta_kegiatan_id"),
                        rs.getString("status_hadir"),
                        rs.getString("sertifikat_peserta"),
                        regis
                ));
            }
        }
        return list;
    }

    public void updatePeserta(pesertaKegiatan pk) throws SQLException {
        String query = "UPDATE peserta_kegiatan SET status_hadir = ?, sertifikat_peserta = ?, registrasi_id = ? WHERE peserta_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pk.getStatusHadir());
            stmt.setString(2, pk.getSertifikatPeserta());
            stmt.setInt(3, pk.getRegistrasiId());
            stmt.setInt(4, pk.getPesertaKegiatanId());
            stmt.executeUpdate();
        }
    }

    public void deletePeserta(int id) throws SQLException {
        String query = "DELETE FROM peserta_kegiatan WHERE peserta_kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
