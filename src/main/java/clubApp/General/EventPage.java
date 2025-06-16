package clubApp.General;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPage {

    @FXML
    Button profilePageEventPage, homeEventPage, allClubEventPage, myClubEventPage, eventPage, logOutEventPage;
    @FXML private ComboBox<Integer> pageSelector;
    @FXML private VBox eventContainer;
    private final int EVENTS_PER_PAGE = 3;
    private List<Map<String, String>> allEventData = new ArrayList<>();
    public void initialize() {
        loadAllEventData(); // isi allEventData
        setupPagination();
    }

    private void loadAllEventData() {
        try (Connection conn = DBConnector.connect()) {
            String query =  "SELECT kc.nama_kegiatan, d.nama_club, jk.nama_jenis_kegiatan, j.start_date, j.end_date, kc.kegiatan_id " +
                    "FROM kegiatan_club kc " +
                    "JOIN data_club d ON kc.club_id = d.club_id " +
                    "JOIN jenis_kegiatan jk ON kc.jenis_kegiatan_id = jk.jenis_kegiatan_id " +
                    "JOIN jadwal j ON kc.kegiatan_id = j.kegiatan_id " +
                    "WHERE kc.active = 'Yes'";
            ResultSet rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("nama", rs.getString("nama_kegiatan"));
                map.put("club", rs.getString("nama_club"));
                map.put("jenis", rs.getString("nama_jenis_kegiatan"));
                map.put("tanggal", rs.getDate("start_date") + " s.d. " + rs.getDate("end_date"));
                map.put("id", rs.getString("kegiatan_id"));
                allEventData.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPagination() {
        int pages = (int) Math.ceil((double) allEventData.size() / EVENTS_PER_PAGE);
        pageSelector.getItems().clear();
        for (int i = 1; i <= pages; i++) pageSelector.getItems().add(i);
        if (pages > 0) pageSelector.setValue(1);

        pageSelector.setOnAction(e -> renderPage(pageSelector.getValue()));
        if (pages > 0) renderPage(1);
    }

    private void renderPage(int page) {
        eventContainer.getChildren().clear();
        int start = (page - 1) * EVENTS_PER_PAGE;
        int end = Math.min(start + EVENTS_PER_PAGE, allEventData.size());

        for (int i = start; i < end; i++) {
            Map<String, String> data = allEventData.get(i);
            Pane p = createEventPane(
                    data.get("id"), data.get("nama"),
                    data.get("club"), data.get("jenis"),
                    data.get("tanggal")
            );
            eventContainer.getChildren().add(p);
        }
    }

    private Pane createEventPane(String id, String nama, String club, String jenis, String tanggal) {
        Pane pane = new Pane();
        pane.setPrefSize(750, 150);
        pane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-border-color: black;");

        TextField tfNama = new TextField(nama);
        tfNama.setLayoutX(140); tfNama.setLayoutY(22);
        tfNama.setEditable(false);

        TextField tfClub = new TextField(club);
        tfClub.setLayoutX(140); tfClub.setLayoutY(65);
        tfClub.setEditable(false);

        TextField tfTanggal = new TextField(tanggal);
        tfTanggal.setLayoutX(140); tfTanggal.setLayoutY(111);
        tfTanggal.setEditable(false);

        TextField tfJenis = new TextField(jenis);
        tfJenis.setLayoutX(506); tfJenis.setLayoutY(22);
        tfJenis.setEditable(false);

        Button joinBtn = new Button("Join");
        joinBtn.setLayoutX(521); joinBtn.setLayoutY(110);
        joinBtn.setOnAction(e -> {
            joinEvent(Integer.parseInt(id));
            joinBtn.setText("Joined");
            joinBtn.setDisable(true);
        });

        pane.getChildren().addAll(tfNama, tfClub, tfTanggal, tfJenis, joinBtn);
        return pane;
    }

    private void joinEvent(int kegiatanId) {
        try (Connection conn = DBConnector.connect()) {
            String checkQuery = "SELECT COUNT(*) FROM data_registrasi_kegiatan WHERE kegiatan_id = ? AND nrp = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, kegiatanId);
            checkStmt.setString(2, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("User sudah terdaftar");
                return;
            }

            String insertReg = "INSERT INTO data_registrasi_kegiatan (registrasi_id, status_registrasi, tanggal_registrasi, nrp, kegiatan_id) " +
                    "VALUES (?, 'Registered', CURRENT_DATE, ?, ?)";
            PreparedStatement stmt1 = conn.prepareStatement(insertReg);
            stmt1.setInt(1, getAvailableId(conn, "data_registrasi_kegiatan", "registrasi_id"));
            stmt1.setString(2, SessionManager.getCurrentUser().getNrp());
            stmt1.setInt(3, kegiatanId);
            stmt1.executeUpdate();

            String insertPeserta = "INSERT INTO peserta_kegiatan (peserta_kegiatan_id, status_hadir, sertifikat_peserta, registrasi_id) VALUES (?, 'No', 'No', ?)";
            PreparedStatement stmt2 = conn.prepareStatement(insertPeserta);
            stmt2.setInt(1, getAvailableId(conn, "peserta_kegiatan", "peserta_kegiatan_id"));
            stmt2.setInt(2, getLastInsertedId(conn, "data_registrasi_kegiatan", "registrasi_id"));
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getAvailableId(Connection conn, String table, String idCol) throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT " + idCol + " FROM " + table + " ORDER BY " + idCol);
        int id = 1;
        while (rs.next()) {
            if (rs.getInt(idCol) != id) break;
            id++;
        }
        return id;
    }

    private int getLastInsertedId(Connection conn, String table, String idCol) throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(" + idCol + ") AS id FROM " + table);
        return rs.next() ? rs.getInt("id") : 0;
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
    public void profilePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Profile-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void allClubPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/All-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("All Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void myClubPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/My-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void eventPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Event-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Event Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        Parent root = loader.load();
        SessionManager.clearSession();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
        stage.centerOnScreen();
    }

    public void homePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Home-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page Admin");
        stage.show();
        stage.centerOnScreen();
    }
}
