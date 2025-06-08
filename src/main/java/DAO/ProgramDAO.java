package DAO;

import Model.program;
import Model.programStudi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramDAO {
    private Connection conn;

    public ProgramDAO(Connection conn) {
        this.conn = conn;
    }

    public program getProgramById(String id) throws SQLException {
        String query = "SELECT * FROM program WHERE program_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String programStudiId = rs.getString("program_studi_id");
                ProgramStudiDAO psDAO = new ProgramStudiDAO(conn);
                programStudi ps = psDAO.getProgramStudiById(programStudiId);

                return new program(
                        rs.getString("program_id"),
                        rs.getString("nama_program"),
                        ps
                );
            }
        }
        return null;
    }

    public List<program> getAllProgram() throws SQLException {
        List<program> list = new ArrayList<>();
        String query = "SELECT * FROM program";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String programStudiId = rs.getString("program_studi_id");

                ProgramStudiDAO psDAO = new ProgramStudiDAO(conn);
                programStudi ps = psDAO.getProgramStudiById(programStudiId);

                list.add(new program(
                        rs.getString("program_id"),
                        rs.getString("nama_program"),
                        ps
                ));
            }
        }
        return list;
    }

    public void insertProgram(program prog) throws SQLException {
        String query = "INSERT INTO program (program_id, nama_program, program_studi_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, prog.getProgramId());
            stmt.setString(2, prog.getNamaProgram());
            stmt.setString(3, prog.getProgramStudiId());
            stmt.executeUpdate();
        }
    }

    public void updateProgram(program prog) throws SQLException {
        String query = "UPDATE program SET nama_program = ?, program_studi_id = ? WHERE program_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, prog.getNamaProgram());
            stmt.setString(2, prog.getProgramStudiId());
            stmt.setString(3, prog.getProgramId());
            stmt.executeUpdate();
        }
    }

    public void deleteProgram(String id) throws SQLException {
        String query = "DELETE FROM program WHERE program_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}
