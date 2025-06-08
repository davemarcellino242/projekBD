package DAO;

import Model.fakultas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FakultasDAO {
    private Connection conn;

    public FakultasDAO(Connection conn) {
        this.conn = conn;
    }

    public fakultas getFakultasById(String id) throws SQLException {
        String query = "SELECT * FROM fakultas WHERE fakultas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new fakultas(
                        rs.getString("fakultas_id"),
                        rs.getString("nama_fakultas")
                );
            }
        }
        return null;
    }

    public List<fakultas> getAllFakultas() throws SQLException {
        List<fakultas> list = new ArrayList<>();
        String query = "SELECT * FROM fakultas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new fakultas(
                        rs.getString("fakultas_id"),
                        rs.getString("nama_fakultas")
                ));
            }
        }
        return list;
    }

    public void insertFakultas(fakultas f) throws SQLException {
        String query = "INSERT INTO fakultas (fakultas_id, nama_fakultas) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, f.getFakultasId());
            stmt.setString(2, f.getNamaFakultas());
            stmt.executeUpdate();
        }
    }

    public void updateFakultas(fakultas f) throws SQLException {
        String query = "UPDATE fakultas SET nama_fakultas = ? WHERE fakultas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, f.getNamaFakultas());
            stmt.setString(2, f.getFakultasId());
            stmt.executeUpdate();
        }
    }

    public void deleteFakultas(String id) throws SQLException {
        String query = "DELETE FROM fakultas WHERE fakultas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}
