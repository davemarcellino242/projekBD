    package DAO;

    import Model.keanggotaan;
    import Model.*;
    import db.DBConnector;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;

    public class KeanggotaanDAO {
        private Connection conn;

        public KeanggotaanDAO() throws SQLException {
            this.conn = DBConnector.connect();
        }

        public void insertKeanggotaan(keanggotaan kgt) throws SQLException {
            String query = "INSERT INTO keanggotaan (keanggotaan_id, peran, status, tanggal_bergabung, nrp, club_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, kgt.getKeanggotaanId());
                stmt.setString(2, kgt.getPeran());
                stmt.setString(3, kgt.getStatus());
                stmt.setDate(4, Date.valueOf(kgt.getTanggalBergabung()));
                stmt.setString(5, kgt.getNrp());
                stmt.setInt(6, kgt.getClubId());
                stmt.executeUpdate();
            }
        }

        public keanggotaan getKeanggotaanById(int id) throws SQLException {
            String query = "SELECT * FROM keanggotaan WHERE keanggotaan_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String nrp = rs.getString("nrp");
                    MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                    mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);

                    int clubId = rs.getInt("club_id");
                    ClubDAO clubDAO = new ClubDAO();
                    club club = clubDAO.getClubById(clubId);
                    return new keanggotaan(
                            rs.getInt("keanggotaan_id"),
                            rs.getString("peran"),
                            rs.getString("status"),
                            rs.getDate("tanggal_bergabung").toLocalDate(),
                            mhs,
                            club
                    );
                }
            }
            return null;
        }

        public int getNextKeanggotaanId() throws SQLException {
            String query = "SELECT MAX(keanggotaan_id) FROM keanggotaan";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            }
            return 1;
        }

        public List<keanggotaan> getKeanggotaanByNRP(String nrp) throws SQLException {
            List<keanggotaan> keanggotaanList = new ArrayList<>();
            String query = "SELECT * FROM keanggotaan WHERE nrp = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nrp);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String np = rs.getString("nrp");
                        MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                        mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(np);

                        int clubId = rs.getInt("club_id");
                        ClubDAO clubDAO = new ClubDAO();
                        club club = clubDAO.getClubById(clubId);
                        keanggotaan k = new keanggotaan(
                                rs.getInt("keanggotaan_id"),
                                rs.getString("peran"),
                                rs.getString("status"),
                                rs.getDate("tanggal_bergabung").toLocalDate(),
                                mhs,
                                club
                        );
                        keanggotaanList.add(k);
                    }
                }
            }

            return keanggotaanList;
        }

        public static List<Integer> getClubIdsByNrp(String nrp) {
            List<Integer> clubIds = new ArrayList<>();
            String query = "SELECT club_id FROM keanggotaan WHERE nrp = ?";

            try (Connection conn = DBConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nrp);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    clubIds.add(rs.getInt("club_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return clubIds;
        }

        public List<keanggotaan> getAllKeanggotaan() throws SQLException {
            List<keanggotaan> list = new ArrayList<>();
            String query = "SELECT * FROM keanggotaan";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String nrp = rs.getString("nrp");
                    MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                    mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);

                    int clubId = rs.getInt("club_id");
                    ClubDAO clubDAO = new ClubDAO();
                    club club = clubDAO.getClubById(clubId);
                    list.add(new keanggotaan(
                            rs.getInt("keanggotaan_id"),
                            rs.getString("peran"),
                            rs.getString("status"),
                            rs.getDate("tanggal_bergabung").toLocalDate(),
                            mhs,
                            club
                    ));
                }
            }
            return list;
        }

        public void updateKeanggotaan(keanggotaan kgt) throws SQLException {
            String query = "UPDATE keanggotaan SET peran = ?, status = ?, tanggal_bergabung = ?, nrp = ?, club_id = ? WHERE keanggotaan_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, kgt.getPeran());
                stmt.setString(2, kgt.getStatus());
                stmt.setDate(3, Date.valueOf(kgt.getTanggalBergabung()));
                stmt.setString(4, kgt.getNrp());
                stmt.setInt(5, kgt.getClubId());
                stmt.setInt(6, kgt.getKeanggotaanId());
                stmt.executeUpdate();
            }
        }

        public void deleteKeanggotaan(int id) throws SQLException {
            String query = "DELETE FROM keanggotaan WHERE keanggotaan_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }

        public void updateStatusKeanggotaan(int kgId, String status) throws SQLException {
            String query = "UPDATE keanggotaan SET status = ? WHERE keanggotaan_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, status);
                stmt.setInt(2, kgId);
                stmt.executeUpdate();
            }
        }

        public void updatePeranKeanggotaan(int kgId, String peran) throws SQLException {
            String query = "UPDATE keanggotaan SET peran = ? WHERE keanggotaan_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, peran);
                stmt.setInt(2, kgId);
                stmt.executeUpdate();
            }
        }


    }
