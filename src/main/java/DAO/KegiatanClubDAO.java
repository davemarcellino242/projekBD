package DAO;

import Model.*;
import DAO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KegiatanClubDAO {
    private Connection conn;

    public KegiatanClubDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertKegiatan(kegiatanClub kegiatan) throws SQLException {
        String query = "INSERT INTO kegiatan_club (kegiatan_id, nama_kegiatan, club_id, lokasi_id, jenis_kegiatan_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, kegiatan.getKegiatanId());
            stmt.setString(2, kegiatan.getNamaKegiatan());
            stmt.setInt(3, kegiatan.getClubId());
            stmt.setInt(4, kegiatan.getLokasiId());
            stmt.setInt(5, kegiatan.getJenisKegiatanId());
            stmt.executeUpdate();
        }
    }

    public kegiatanClub getKegiatanById(int id) throws SQLException {
        String query = "SELECT * FROM kegiatan_club WHERE kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int lokasiId = rs.getInt("lokasi_id");
                LokasiDAO lokasiDAO = new LokasiDAO(conn);
                lokasi lks = lokasiDAO.getLokasiById(lokasiId);

                int clubId = rs.getInt("club_id");
                ClubDAO clubDAO = new ClubDAO(conn);
                club club = clubDAO.getClubById(clubId);

                int jenisKegiatanId = rs.getInt("jenis_kegiatan_id");
                JenisKegiatanDAO jenisKegiatanDAO = new JenisKegiatanDAO(conn);
                jenisKegiatan jk = jenisKegiatanDAO.getJenisKegiatanById(jenisKegiatanId);
                return new kegiatanClub(
                        rs.getInt("kegiatan_id"),
                        rs.getString("nama_kegiatan"),
                        club,
                        lks,
                        jk
                );
            }
        }
        return null;
    }

    public List<kegiatanClub> getAllKegiatan() throws SQLException {
        List<kegiatanClub> list = new ArrayList<>();
        String query = "SELECT * FROM kegiatan_club";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int lokasiId = rs.getInt("lokasi_id");
                LokasiDAO lokasiDAO = new LokasiDAO(conn);
                lokasi lks = lokasiDAO.getLokasiById(lokasiId);

                int clubId = rs.getInt("club_id");
                ClubDAO clubDAO = new ClubDAO(conn);
                club club = clubDAO.getClubById(clubId);

                int jenisKegiatanId = rs.getInt("jenis_kegiatan_id");
                JenisKegiatanDAO jenisKegiatanDAO = new JenisKegiatanDAO(conn);
                jenisKegiatan jk = jenisKegiatanDAO.getJenisKegiatanById(jenisKegiatanId);
                list.add(new kegiatanClub(
                        rs.getInt("kegiatan_id"),
                        rs.getString("nama_kegiatan"),
                        club,
                        lks,
                        jk
                ));
            }
        }
        return list;
    }

    public void updateKegiatan(kegiatanClub kegiatan) throws SQLException {
        String query = "UPDATE kegiatan_club SET nama_kegiatan = ?, club_id = ?, lokasi_id = ?, jenis_kegiatan_id = ? WHERE kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kegiatan.getNamaKegiatan());
            stmt.setInt(2, kegiatan.getClubId());
            stmt.setInt(3, kegiatan.getLokasiId());
            stmt.setInt(4, kegiatan.getJenisKegiatanId());
            stmt.setInt(5, kegiatan.getKegiatanId());
            stmt.executeUpdate();
        }
    }

    public void deleteKegiatan(int id) throws SQLException {
        String query = "DELETE FROM kegiatan_club WHERE kegiatan_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
