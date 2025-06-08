package DAO;

import Model.mahasiswa;
import Model.program;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    private Connection conn;

    public MahasiswaDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertMahasiswa(mahasiswa mhs) throws SQLException {
        String query = "INSERT INTO data_mahasiswa (nrp, nama, email, tangga_lahir, program_id, signup) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mhs.getNrp());
            stmt.setString(2, mhs.getNama());
            stmt.setString(3, mhs.getEmail());
            stmt.setDate(4, Date.valueOf(mhs.getTanggalLahir()));
            stmt.setString(5, mhs.getProgramID());
            stmt.setString(6, mhs.isSignup() ? "Yes" : "No");
            stmt.executeUpdate();
        }
    }

    public mahasiswa getMahasiswaByNrp(String nrp) throws SQLException {
        String query = "SELECT * FROM data_mahasiswa WHERE nrp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String programId = rs.getString("program_id");
                ProgramDAO programDAO = new ProgramDAO(conn);
                program prog = programDAO.getProgramById(programId);
                return new mahasiswa(
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        prog,
                        rs.getString("signup").equalsIgnoreCase("Yes")
                );
            }
        }
        return null;
    }

    public List<mahasiswa> getAllMahasiswa() throws SQLException {
        List<mahasiswa> list = new ArrayList<>();
        String query = "SELECT * FROM mahasiswa";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String programId = rs.getString("program_id");
                ProgramDAO programDAO = new ProgramDAO(conn);
                program prog = programDAO.getProgramById(programId);
                list.add(new mahasiswa(
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getDate("tanggal_lahir").toLocalDate(),
                        prog,
                        rs.getString("signup").equalsIgnoreCase("Y")
                ));
            }
        }
        return list;
    }

    public void updateMahasiswa(mahasiswa mhs) throws SQLException {
        String query = "UPDATE mahasiswa SET nama = ?, email = ?, tanggal_lahir = ?, program_id = ?, signup = ? WHERE nrp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mhs.getNama());
            stmt.setString(2, mhs.getEmail());
            stmt.setDate(3, Date.valueOf(mhs.getTanggalLahir()));
            stmt.setString(4, mhs.getProgramID());
            stmt.setString(5, mhs.isSignup() ? "Y" : "N");
            stmt.setString(6, mhs.getNrp());
            stmt.executeUpdate();
        }
    }

    public void deleteMahasiswa(String nrp) throws SQLException {
        String query = "DELETE FROM mahasiswa WHERE nrp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nrp);
            stmt.executeUpdate();
        }
    }
}