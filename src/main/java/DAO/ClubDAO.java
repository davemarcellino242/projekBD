package DAO;

import Model.club;
import Model.kategoriClub;
import Model.pendiriClub;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {
    private Connection conn;

    public ClubDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertClub(club club) throws SQLException {
        String query = "INSERT INTO data_club (club_id, nama_club, deskripsi_club, tahun_berdiri_club, kategori_id, pendiri_club_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, club.getClubId());
            stmt.setString(2, club.getNamaClub());
            stmt.setString(3, club.getDeskripsiClub());
            stmt.setInt(4, club.getTahunBerdiriClub());
            stmt.setInt(5, club.getKategoriId());
            stmt.setString(6, club.getPendiriClubId());
            stmt.executeUpdate();
        }
    }

    public club getClubById(int clubId) throws SQLException {
        String query = "SELECT * FROM data_club WHERE club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int kategoriId = rs.getInt("kategori_id");
                KategoriClubDAO kategoriClubDAO = new KategoriClubDAO(conn);
                kategoriClub kategori = kategoriClubDAO.getKategoriById(kategoriId);

                String pendiriClub = rs.getString("pendiri_club_id");
                PendiriClubDAO pendiriClubDAO = new PendiriClubDAO(conn);
                pendiriClub pc = pendiriClubDAO.getPendiriClubById(pendiriClub);

                return new club(
                        rs.getInt("club_id"),
                        rs.getString("nama_club"),
                        rs.getString("deskripsi_club"),
                        rs.getInt("tahun_berdiri_club"),
                        kategori,
                        pc
                );
            }
        }
        return null;
    }

    public List<club> getAllClubs() throws SQLException {
        List<club> clubs = new ArrayList<>();
        String query = "SELECT * FROM data_club";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int kategoriId = rs.getInt("kategori_id");
                KategoriClubDAO kategoriClubDAO = new KategoriClubDAO(conn);
                kategoriClub kategori = kategoriClubDAO.getKategoriById(kategoriId);

                String pendiriClub = rs.getString("pendiri_club_id");
                PendiriClubDAO pendiriClubDAO = new PendiriClubDAO(conn);
                pendiriClub pc = pendiriClubDAO.getPendiriClubById(pendiriClub);
                clubs.add(new club(
                        rs.getInt("club_id"),
                        rs.getString("nama_club"),
                        rs.getString("deskripsi_club"),
                        rs.getInt("tahun_berdiri_club"),
                        kategori,
                        pc
                ));
            }
        }
        return clubs;
    }

    public void updateClub(club club) throws SQLException {
        String query = "UPDATE data_club SET nama_club = ?, deskripsi_club = ?, tahun_berdiri_club = ?, " +
                "kategori_id = ?, pendiri_club_id = ? WHERE club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, club.getNamaClub());
            stmt.setString(2, club.getDeskripsiClub());
            stmt.setInt(3, club.getTahunBerdiriClub());
            stmt.setInt(4, club.getKategoriId());
            stmt.setString(5, club.getPendiriClubId());
            stmt.setInt(6, club.getClubId());
            stmt.executeUpdate();
        }
    }

    public void deleteClub(String clubId) throws SQLException {
        String query = "DELETE FROM data_club WHERE club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubId);
            stmt.executeUpdate();
        }
    }
}
