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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddUpdateEventPage {

    @FXML private TextField namaEvent, jenisEvent, startField, endField, lokasiField;
    @FXML private ComboBox<Integer> Kegiatan;
    @FXML private ComboBox<String> clubField;
    @FXML private Button Done;

    private Map<Integer, String> clubMap = new LinkedHashMap<>();
    private List<Integer> kegiatanIds = new ArrayList<>();

    public void initialize() {
        loadKegiatanClubs();

        Kegiatan.setItems(FXCollections.observableArrayList(kegiatanIds));

        Kegiatan.setOnAction(e -> {
            int selectedIndex = Kegiatan.getValue() - 1;
            if (selectedIndex < kegiatanIds.size()) {
                loadKegiatanByClubId(kegiatanIds.get(selectedIndex));
            } else {
                clearAllFields();
            }
        });

        if (!kegiatanIds.isEmpty()) {
            Kegiatan.setValue(kegiatanIds.get(0));
            loadKegiatanByClubId(kegiatanIds.get(0));
        } else {
            clearAllFields();
        }
    }

    private void loadKegiatanClubs() {
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
                kegiatanIds.add(id);
                clubMap.put(id, nama);
                clubNames.add(nama);
            }

            clubField.setItems(FXCollections.observableArrayList(clubNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKegiatanByClubId(int clubId) {
        String clubName = clubMap.get(clubId);
        if (clubName != null) {
            clubField.setValue(clubName);
        }
    }

    private void clearAllFields() {
        namaEvent.clear();
        jenisEvent.clear();
        startField.clear();
        endField.clear();
        lokasiField.clear();
        clubField.setValue(null);
    }


    @FXML
    public void handleDone(ActionEvent event) {
        String namaKegiatan = namaEvent.getText();
        String namaJenis = jenisEvent.getText();
        String lokasiNama = lokasiField.getText();

        int clubId = getClubIdByName(clubField.getValue());
        LocalDate start = LocalDate.parse(startField.getText());
        LocalDate end = LocalDate.parse(endField.getText());

        try (Connection conn = DBConnector.connect()) {

            int lokasiId = getOrCreateLokasiId(conn, lokasiNama);
            int jenisKegiatanId = getOrCreateJenisKegiatanId(conn, namaJenis);
            int kegiatanId = getAvailableKegiatanId(conn);

            String insert = "INSERT INTO kegiatan_club (kegiatan_id, nama_kegiatan, club_id, lokasi_id, jenis_kegiatan_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, kegiatanId);
            stmt.setString(2, namaKegiatan);
            stmt.setInt(3, clubId);
            stmt.setInt(4, lokasiId);
            stmt.setInt(5, jenisKegiatanId);
            stmt.executeUpdate();

            String insertJadwal = "INSERT INTO jadwal (jadwal_id, start_date, end_date, kegiatan_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtJadwal = conn.prepareStatement(insertJadwal);
            int jadwalId = getAvailableJadwalId(conn); // Gunakan metode mirip dengan getAvailableKegiatanId
            stmtJadwal.setInt(1, jadwalId);
            stmtJadwal.setDate(2, Date.valueOf(start));
            stmtJadwal.setDate(3, Date.valueOf(end));
            stmtJadwal.setInt(4, kegiatanId);
            stmtJadwal.executeUpdate();

            System.out.println("Kegiatan berhasil ditambahkan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getClubIdByName(String name) {
        return clubMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(0); // fallback kalau tidak ditemukan
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

    private int getOrCreateJenisKegiatanId(Connection conn, String namaJenis) throws SQLException {
        String check = "SELECT jenis_kegiatan_id FROM jenis_kegiatan WHERE nama_jenis_kegiatan = ?";
        PreparedStatement checkStmt = conn.prepareStatement(check);
        checkStmt.setString(1, namaJenis);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) return rs.getInt("jenis_kegiatan_id");

        String existing = "SELECT jenis_kegiatan_id FROM jenis_kegiatan ORDER BY jenis_kegiatan_id ASC";
        ResultSet rsAll = conn.createStatement().executeQuery(existing);
        int id = 1;
        while (rsAll.next()) {
            if (rsAll.getInt("jenis_kegiatan_id") != id) break;
            id++;
        }

        PreparedStatement insert = conn.prepareStatement("INSERT INTO jenis_kegiatan (jenis_kegiatan_id, nama_jenis_kegiatan) VALUES (?, ?)");
        insert.setInt(1, id);
        insert.setString(2, namaJenis);
        insert.executeUpdate();
        return id;
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
