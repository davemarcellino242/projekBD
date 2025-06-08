package DAO;

import Model.organisasiProfessional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganisasiProfessionalDAO {
    private Connection conn;

    public OrganisasiProfessionalDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertOrganisasiProfessional(organisasiProfessional op) throws SQLException {
        String query = "INSERT INTO organisasi_professional (organisasi_professional_id, organisasi_professional) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, op.getOrganisasiProfessionaId());
            stmt.setString(2, op.getNamaOrganisasiProfessional());
            stmt.executeUpdate();
        }
    }

    public organisasiProfessional getOrganisasiProfessionalById(String id) throws SQLException {
        String query = "SELECT * FROM organisasi_professional WHERE organisasi_professional_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new organisasiProfessional(
                        rs.getString("organisasi_professional_id"),
                        rs.getString("organisasi_professional")
                );
            }
        }
        return null;
    }

    public List<organisasiProfessional> getAllOrganisasiProfessional() throws SQLException {
        List<organisasiProfessional> list = new ArrayList<>();
        String query = "SELECT * FROM organisasi_professional";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new organisasiProfessional(
                        rs.getString("organisasi_professional_id"),
                        rs.getString("organisasi_professional")
                ));
            }
        }
        return list;
    }

    public void updateOrganisasiProfessional(organisasiProfessional op) throws SQLException {
        String query = "UPDATE organisasi_professional SET organisasi_professional = ? WHERE organisasi_professional_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, op.getNamaOrganisasiProfessional());
            stmt.setString(2, op.getOrganisasiProfessionaId());
            stmt.executeUpdate();
        }
    }

    public void deleteOrganisasiProfessional(String id) throws SQLException {
        String query = "DELETE FROM organisasi_professional WHERE organisasi_professional_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}
