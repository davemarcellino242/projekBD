package DAO;

import Model.pendiriClub;
import Model.organisasiProfessional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PendiriClubDAO {
    private Connection conn;

    public PendiriClubDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertPendiriClub(pendiriClub pc) throws SQLException {
        String query = "INSERT INTO pendiri_club (pendiri_club_id, nama_pendiri_club, organisasi_profesional_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pc.getPendiriClubId());
            stmt.setString(2, pc.getNamaPendirClub());
            stmt.setString(3, pc.getOrganisasiProfessionalId());
            stmt.executeUpdate();
        }
    }

    public pendiriClub getPendiriClubById(String id) throws SQLException {
        String query = "SELECT * FROM pendiri_club WHERE pendiri_club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrganisasiProfessionalDAO pdao = new OrganisasiProfessionalDAO(conn);
                organisasiProfessional op = null;

                try {
                    // Coba ambil kolom organisasi_profesional_id jika ada
                    String organisasiProfessionalId = rs.getString("organisasi_profesional_id");
                    if (organisasiProfessionalId != null) {
                        op = pdao.getOrganisasiProfessionalById(organisasiProfessionalId);
                    }
                } catch (SQLException | RuntimeException e) {
                    // Kolom tidak ditemukan atau error lainnya → biarkan op tetap null
                }

                return new pendiriClub(
                        rs.getString("pendiri_club_id"),
                        rs.getString("nama_pendiri_club"),
                        op
                );
            }
        }
        return null;
    }


    public List<pendiriClub> getAllPendiriClub() throws SQLException {
        List<pendiriClub> list = new ArrayList<>();
        String query = "SELECT * FROM pendiri_club";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String organisasiProfessionalId = rs.getString("organisasi_profesional_id");
                OrganisasiProfessionalDAO pdao = new OrganisasiProfessionalDAO(conn);
                organisasiProfessional op = pdao.getOrganisasiProfessionalById(organisasiProfessionalId);
                list.add(new pendiriClub(
                        rs.getString("pendiri_club_id"),
                        rs.getString("nama_pendiri_club"),
                        op
                ));
            }
        }
        return list;
    }

    public void updatePendiriClub(pendiriClub pc) throws SQLException {
        String query = "UPDATE pendiri_club SET nama_pendiri_club = ?, organisasi_profesional_id = ? WHERE pendiri_club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pc.getNamaPendirClub());
            stmt.setString(2, pc.getOrganisasiProfessionalId());
            stmt.setString(3, pc.getPendiriClubId());
            stmt.executeUpdate();
        }
    }

    public void deletePendiriClub(String id) throws SQLException {
        String query = "DELETE FROM pendiri_club WHERE pendiri_club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}
