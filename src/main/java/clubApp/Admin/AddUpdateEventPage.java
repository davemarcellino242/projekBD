package clubApp.Admin;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AddUpdateEventPage {

    @FXML private TextField namaEvent;
    @FXML private DatePicker startField, endField;
    @FXML private ComboBox<Integer> kegiatanKe;
    @FXML private ComboBox<String> clubField, jenisEvent, lokasiField;
    private int kegiatanIdCurrent = 0;

    private Map<Integer, String> clubMap = new LinkedHashMap<>();
    private Map<String, Integer> reverseClubMap = new HashMap<>();
    private Map<String, Integer> jenisKegiatanMap = new HashMap<>();
    private Map<String, Integer> lokasiMap = new HashMap<>();


    @FXML
    public void initialize() {
        updateExpiredEvents();
        loadClubsForAdmin();
        loadJenisKegiatanOptions();
        loadLokasiOptions();
        clubField.setOnAction(e -> {
            String selectedClubName = clubField.getValue();
            if (selectedClubName != null) {
                // setiap kali club dipilih, tampilkan 1 dan 2 di kegiatanKe
                kegiatanKe.setItems(FXCollections.observableArrayList(1, 2));
                kegiatanKe.setValue(1); // default bisa di-set ke 1 kalau mau
            } else {
                kegiatanKe.getItems().clear();
            }
        });
    }


    private void loadClubsForAdmin() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT d.club_id, d.nama_club FROM keanggotaan k " +
                    "JOIN data_club d ON k.club_id = d.club_id " +
                    "WHERE k.nrp = ? AND k.peran = 'Admin'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();

            List<String> clubNames = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("club_id");
                String nama = rs.getString("nama_club");
                clubMap.put(id, nama);
                reverseClubMap.put(nama, id);
                clubNames.add(nama);
            }

            clubField.setItems(FXCollections.observableArrayList(clubNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLokasiOptions() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT lokasi_id, nama_lokasi FROM lokasi";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<String> lokasiNames = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("lokasi_id");
                String nama = rs.getString("nama_lokasi");
                lokasiNames.add(nama);
                lokasiMap.put(nama, id);
            }

            lokasiField.setItems(FXCollections.observableArrayList(lokasiNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadJenisKegiatanOptions() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT jenis_kegiatan_id, nama_jenis_kegiatan FROM jenis_kegiatan";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<String> jenisNames = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("jenis_kegiatan_id");
                String nama = rs.getString("nama_jenis_kegiatan");
                jenisNames.add(nama);
                jenisKegiatanMap.put(nama, id);
            }

            jenisEvent.setItems(FXCollections.observableArrayList(jenisNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getAvailableKegiatanId(Connection conn) throws SQLException {
        String query = "SELECT kegiatan_id FROM kegiatan_club ORDER BY kegiatan_id ASC";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        int expected = 1;
        while (rs.next()) {
            int current = rs.getInt("kegiatan_id");
            if (current != expected) return expected;
            expected++;
        }
        return expected;
    }

    public void loadEventData(int kegiatanId) {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT kc.nama_kegiatan, kc.club_id, l.nama_lokasi, jk.nama_jenis_kegiatan, j.start_date, j.end_date " +
                    "FROM kegiatan_club kc " +
                    "JOIN lokasi l ON kc.lokasi_id = l.lokasi_id " +
                    "JOIN jenis_kegiatan jk ON kc.jenis_kegiatan_id = jk.jenis_kegiatan_id " +
                    "JOIN jadwal j ON kc.kegiatan_id = j.kegiatan_id " +
                    "WHERE kc.kegiatan_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, kegiatanId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kegiatanIdCurrent = kegiatanId; // masuk mode edit
                namaEvent.setText(rs.getString("nama_kegiatan"));
                lokasiField.setValue(rs.getString("nama_lokasi"));
                jenisEvent.setValue(rs.getString("nama_jenis_kegiatan"));
                startField.setValue(rs.getDate("start_date").toLocalDate());
                endField.setValue(rs.getDate("end_date").toLocalDate());

                int clubId = rs.getInt("club_id");
                String clubName = clubMap.get(clubId);
                clubField.setValue(clubName);
                kegiatanKe.setItems(FXCollections.observableArrayList(1, 2));
                kegiatanKe.setValue(kegiatanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleDone(ActionEvent event) {
        String namaKegiatan = namaEvent.getText();
        String lokasiNama = lokasiField.getValue();
        String namaJenis = jenisEvent.getValue();

        int clubId = reverseClubMap.get(clubField.getValue());
        LocalDate start = startField.getValue();
        LocalDate end = endField.getValue();

        if (start == null || end == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tanggal Tidak Lengkap");
            alert.setHeaderText(null);
            alert.setContentText("Tanggal mulai dan akhir harus diisi.");
            alert.showAndWait();
            return;
        }

        if (start.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tanggal Tidak Valid");
            alert.setHeaderText(null);
            alert.setContentText("Tanggal mulai harus hari ini atau setelah hari ini.");
            alert.showAndWait();
            return;
        }

        if (end.isBefore(start)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tanggal Tidak Valid");
            alert.setHeaderText(null);
            alert.setContentText("Tanggal akhir tidak boleh sebelum tanggal mulai.");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DBConnector.connect()) {
            int lokasiId = lokasiMap.getOrDefault(lokasiNama, -1);
            int jenisKegiatanId = jenisKegiatanMap.getOrDefault(namaJenis, -1);

            if (kegiatanIdCurrent == 0) {
                // Tambah baru
                int kegiatanId = getAvailableKegiatanId(conn);
                kegiatanIdCurrent = kegiatanId;

                PreparedStatement stmt = conn.prepareStatement("INSERT INTO kegiatan_club (kegiatan_id, nama_kegiatan, club_id, lokasi_id, jenis_kegiatan_id, active) VALUES (?, ?, ?, ?, ?, 'Yes')");
                stmt.setInt(1, kegiatanId);
                stmt.setString(2, namaKegiatan);
                stmt.setInt(3, clubId);
                stmt.setInt(4, lokasiId);
                stmt.setInt(5, jenisKegiatanId);
                stmt.executeUpdate();

                PreparedStatement stmtJadwal = conn.prepareStatement(
                        "INSERT INTO jadwal (jadwal_id, start_date, end_date, kegiatan_id) VALUES (?, ?, ?, ?)"
                );
                stmtJadwal.setInt(1, getAvailableJadwalId(conn));
                stmtJadwal.setDate(2, Date.valueOf(start));
                stmtJadwal.setDate(3, Date.valueOf(end));
                stmtJadwal.setInt(4, kegiatanId);
                stmtJadwal.executeUpdate();

                System.out.println("Kegiatan baru berhasil ditambahkan.");
            } else {
                // Update
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE kegiatan_club SET nama_kegiatan = ?, club_id = ?, lokasi_id = ?, jenis_kegiatan_id = ? WHERE kegiatan_id = ?"
                );
                stmt.setString(1, namaKegiatan);
                stmt.setInt(2, clubId);
                stmt.setInt(3, lokasiId);
                stmt.setInt(4, jenisKegiatanId);
                stmt.setInt(5, kegiatanIdCurrent);
                stmt.executeUpdate();

                PreparedStatement stmtJadwal = conn.prepareStatement(
                        "UPDATE jadwal SET start_date = ?, end_date = ? WHERE kegiatan_id = ?"
                );
                stmtJadwal.setDate(1, Date.valueOf(start));
                stmtJadwal.setDate(2, Date.valueOf(end));
                stmtJadwal.setInt(3, kegiatanIdCurrent);
                stmtJadwal.executeUpdate();

                System.out.println("Kegiatan berhasil diupdate.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getAvailableJadwalId(Connection conn) throws SQLException {
        String query = "SELECT jadwal_id FROM jadwal ORDER BY jadwal_id ASC";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        int expected = 1;
        while (rs.next()) {
            int current = rs.getInt("jadwal_id");
            if (current != expected) return expected;
            expected++;
        }
        return expected;
    }

    private int getOrCreateLokasiId(Connection conn, String lokasiNama) throws SQLException {
        String check = "SELECT lokasi_id FROM lokasi WHERE nama_lokasi = ?";
        PreparedStatement checkStmt = conn.prepareStatement(check);
        checkStmt.setString(1, lokasiNama);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) return rs.getInt("lokasi_id");

        String existing = "SELECT lokasi_id FROM lokasi ORDER BY lokasi_id ASC";
        ResultSet rsAll = conn.createStatement().executeQuery(existing);
        int id = 1;
        while (rsAll.next()) {
            if (rsAll.getInt("lokasi_id") != id) break;
            id++;
        }

        PreparedStatement insert = conn.prepareStatement("INSERT INTO lokasi (lokasi_id, nama_lokasi) VALUES (?, ?)");
        insert.setInt(1, id);
        insert.setString(2, lokasiNama);
        insert.executeUpdate();
        return id;
    }

    private void updateExpiredEvents() {
        try (Connection conn = DBConnector.connect()) {
            String sql = """
            UPDATE kegiatan_club kc
            SET active = 'No'
            FROM jadwal j
            WHERE kc.kegiatan_id = j.kegiatan_id AND j.end_date < CURRENT_DATE
        """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Personal-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Personal Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        SessionManager.clearSession();
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Home-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

    public void eventDatePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Event-Date-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Event Date Page");
        stage.show();
        stage.centerOnScreen();
    }
}
