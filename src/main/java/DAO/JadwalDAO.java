package DAO;

import Model.jadwal;
import Model.pendiriClub;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class JadwalDAO {
    private Connection conn;

    public JadwalDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertJadwal(jadwal jadwal) throws SQLException {
        String query = "INSERT INTO jadwal (kegiatan_id, kegiatan_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, jadwal.getKegiatanId());
            stmt.setDate(2,  Date.valueOf(jadwal.getJadwalTanggal()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                jadwal.setJadwalId(rs.getInt(1));
            }
        }
    }

    public jadwal getJadwalById(int id) throws SQLException {
        String query = "SELECT * FROM jadwal WHERE jadwal_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int kegiatanId = rs.getInt("kegiatan_id");
                KegiatanClubDAO kegiatanClubDAO = new KegiatanClubDAO(conn);
                Model.kegiatanClub kc = kegiatanClubDAO.getKegiatanById(kegiatanId);
                return new jadwal(
                        rs.getInt("jadwal_id"),
                        rs.getDate("tanggal").toLocalDate(),
                        kc
                );
            }
        }
        return null;
    }

    public List<jadwal> getAllJadwal() throws SQLException {
        List<jadwal> list = new ArrayList<>();
        String query = "SELECT * FROM jadwal";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int kegiatanId = rs.getInt("kegiatan_id");
                KegiatanClubDAO kegiatanClubDAO = new KegiatanClubDAO(conn);
                Model.kegiatanClub kc = kegiatanClubDAO.getKegiatanById(kegiatanId);
                list.add(new jadwal(
                        rs.getInt("jadwal_id"),
                        rs.getDate("tanggal").toLocalDate(),
                        kc
                ));
            }
        }
        return list;
    }

    public void updateJadwal(jadwal jadwal) throws SQLException {
        String query = "UPDATE jadwal SET tanggal = ?, kegiatan_id = ? WHERE jadwal_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(jadwal.getJadwalTanggal()));
            stmt.setInt(2, jadwal.getKegiatanId());
            stmt.setInt(3, jadwal.getJadwalId());
            stmt.executeUpdate();
        }
    }

    public void deleteJadwal(int id) throws SQLException {
        String query = "DELETE FROM jadwal WHERE jadwal_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}