package DAO;

import Model.kategoriClub;
import Model.pendiriClub;
import Model.registrasi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrasiDAO {
    private Connection conn;

    public RegistrasiDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRegistrasi(registrasi rgs) throws SQLException {
        String query = "INSERT INTO data_registrasi_kegiatan (status_registrasi, tanggal_registrasi, nrp, kegiatan_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, rgs.getStatusRegistrasi());
            stmt.setDate(2, Date.valueOf(rgs.getTanggalRegistrasi()));
            stmt.setString(3, rgs.getNrp());
            stmt.setInt(4, rgs.getKegiatanId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                rgs.setRegistrasiId(rs.getInt(1));
            }
        }
    }

    public registrasi getRegistrasiById(int id) throws SQLException {
        String query = "SELECT * FROM data_registrasi_kegiatan WHERE registrasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nrp = rs.getString("nrp");
                MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                Model.mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);

                int kegiatanId = rs.getInt("kegiatan_id");
                KegiatanClubDAO kegiatanClubDAO = new KegiatanClubDAO(conn);
                Model.kegiatanClub kc = kegiatanClubDAO.getKegiatanById(kegiatanId);
                return new registrasi(
                        rs.getInt("registrasi_id"),
                        rs.getString("status_registrasi"),
                        rs.getDate("tanggal_registrasi").toLocalDate(),
                        mhs,
                        kc
                );
            }
        }
        return null;
    }

    public List<registrasi> getAllRegistrasi() throws SQLException {
        List<registrasi> list = new ArrayList<>();
        String query = "SELECT * FROM data_registrasi_kegiatan";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String nrp = rs.getString("nrp");
                MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                Model.mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);

                int kegiatanId = rs.getInt("kegiatan_id");
                KegiatanClubDAO kegiatanClubDAO = new KegiatanClubDAO(conn);
                Model.kegiatanClub kc = kegiatanClubDAO.getKegiatanById(kegiatanId);
                list.add(new registrasi(
                        rs.getInt("registrasi_id"),
                        rs.getString("status_registrasi"),
                        rs.getDate("tanggal_registrasi").toLocalDate(),
                        mhs,
                        kc
                ));
            }
        }
        return list;
    }

    public void updateRegistrasi(registrasi rgs) throws SQLException {
        String query = "UPDATE data_registrasi_kegiatan SET status_registrasi = ?, tanggal_registrasi = ?, nrp = ?, kegiatan_id = ? WHERE registrasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rgs.getStatusRegistrasi());
            stmt.setDate(2, Date.valueOf(rgs.getTanggalRegistrasi()));
            stmt.setString(3, rgs.getNrp());
            stmt.setInt(4, rgs.getKegiatanId());
            stmt.setInt(5, rgs.getRegistrasiId());
            stmt.executeUpdate();
        }
    }

    public void deleteRegistrasi(int id) throws SQLException {
        String query = "DELETE FROM data_registrasi_kegiatan WHERE registrasi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
