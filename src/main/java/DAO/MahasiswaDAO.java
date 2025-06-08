package DAO;

import Model.fakultas;
import Model.mahasiswa;
import Model.program;
import Model.programStudi;
import db.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    private Connection conn;

    public MahasiswaDAO() throws SQLException {
        this.conn = DBConnector.connect();
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

    public mahasiswa validateLogin(String nrp, String password) throws SQLException {
        String query = "SELECT m.nrp, m.nama, m.email, m.tanggal_lahir, m.signup, " +
                "p.program_id, p.nama_program, " +
                "ps.program_studi_id, ps.nama_program_studi, " +
                "f.fakultas_id, f.nama_fakultas " +
                "FROM mahasiswa m " +
                "JOIN program p ON m.program_id = p.program_id " +
                "JOIN program_studi ps ON p.program_studi_id = ps.program_studi_id " +
                "JOIN fakultas f ON ps.fakultas_id = f.fakultas_id " +
                "WHERE m.nrp = ? AND m.password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nrp);
            stmt.setString(2, password); // plain-text, idealnya hashed

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fakultas f = new fakultas(
                        rs.getString("f.fakultas_id"),
                        rs.getString("f.nama_fakultas")
                );

                programStudi ps = new programStudi(
                        rs.getString("ps.program_studi_id"),
                        rs.getString("ps.nama_program_studi"),
                        f
                );

                program pr = new program(
                        rs.getString("p.program_id"),
                        rs.getString("p.nama_program"),
                        ps
                );

                mahasiswa m = new mahasiswa(
                        rs.getString("m.nrp"),
                        rs.getString("m.nama"),
                        rs.getString("m.email"),
                        rs.getDate("m.tanggal_lahir").toLocalDate(),
                        pr,
                        rs.getBoolean("m.signup")
                );

                return m;
            }
        }

        return null; // jika tidak ditemukan
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