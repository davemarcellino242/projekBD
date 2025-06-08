package DAO;

import Model.fakultas;
import Model.program;
import Model.programStudi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramStudiDAO {
    private Connection conn;

    public ProgramStudiDAO(Connection conn) {
        this.conn = conn;
    }

    public programStudi getProgramStudiById(String id) throws SQLException {
        String query = "SELECT * FROM program_studi WHERE program_studi_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fakultasId = rs.getString("fakultasId");
                FakultasDAO fakultasDAO = new FakultasDAO(conn);
                fakultas fak = fakultasDAO.getFakultasById(fakultasId);
                return new programStudi(
                        rs.getString("program_studi_id"),
                        rs.getString("nama_program_studi"),
                        fak
                );
            }
        }
        return null;
    }

    public List<programStudi> getAllProgramStudi() throws SQLException {
        List<programStudi> list = new ArrayList<>();
        String query = "SELECT * FROM program_studi";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String fakultasId = rs.getString("fakultasId");
                FakultasDAO fakultasDAO = new FakultasDAO(conn);
                fakultas fak = fakultasDAO.getFakultasById(fakultasId);
                list.add(new programStudi(
                        rs.getString("program_studi_id"),
                        rs.getString("nama_program_studi"),
                        fak
                ));
            }
        }
        return list;
    }
}
