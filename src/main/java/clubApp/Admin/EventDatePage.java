package clubApp.Admin;

import currentUser.SessionManager;
import currentUser.ShowAlert;
import currentUser.SwitchPage;
import db.DBConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class EventDatePage {

    @FXML
    private ComboBox<String> clubAdmin;

    @FXML
    private VBox paneContainer;

    @FXML
    private Button addEventButton;

    private Map<String, Integer> clubMap = new HashMap<>();

    @FXML
    public void initialize() {
        deactivateExpiredEvents();
        loadClubsForAdmin();
        clubAdmin.setOnAction(e -> {
            String selectedClub = clubAdmin.getValue();
            if (selectedClub != null) {
                int clubId = clubMap.get(selectedClub);
                loadKegiatanForClub(clubId, selectedClub);
            }
        });
    }

    private void loadClubsForAdmin() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT d.club_id, d.nama_club FROM keanggotaan k JOIN data_club d ON k.club_id = d.club_id WHERE k.nrp = ? AND k.peran = 'Admin'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser.SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();

            List<String> clubNames = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("club_id");
                String name = rs.getString("nama_club");
                clubMap.put(name, id);
                clubNames.add(name);
            }
            clubAdmin.getItems().addAll(clubNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKegiatanForClub(int clubId, String clubName) {
        paneContainer.getChildren().clear();

        int shownKegiatanCount = 0;

        try (Connection conn = DBConnector.connect()) {

            String sql = """
            SELECT kc.kegiatan_id, kc.nama_kegiatan, l.nama_lokasi, jk.nama_jenis_kegiatan,
                   j.start_date, j.end_date
            FROM kegiatan_club kc
            JOIN lokasi l ON kc.lokasi_id = l.lokasi_id
            JOIN jenis_kegiatan jk ON kc.jenis_kegiatan_id = jk.jenis_kegiatan_id
            JOIN jadwal j ON kc.kegiatan_id = j.kegiatan_id
            WHERE kc.club_id = ? AND kc.active = 'Yes'
        """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int kegiatanId = rs.getInt("kegiatan_id");
                String nama = rs.getString("nama_kegiatan");
                String lokasi = rs.getString("nama_lokasi");
                String jenis = rs.getString("nama_jenis_kegiatan");
                String start = rs.getDate("start_date").toString();
                String end = rs.getDate("end_date").toString();

                Pane pane = createEventPane(kegiatanId, nama, jenis, start, end, lokasi, clubName);
                paneContainer.getChildren().add(pane);
                VBox.setMargin(pane, new Insets(0, 0, 20, 0));
                shownKegiatanCount++;
            }

            // Atur tombol Add Event berdasarkan jumlah kegiatan aktif yang ditampilkan
            if (shownKegiatanCount >= 2) {
                addEventButton.setDisable(true);
                addEventButton.setOnAction(e -> showAlert("Club ini sudah memiliki 2 kegiatan aktif. Tidak dapat menambah lagi."));
            } else {
                addEventButton.setDisable(false);
                addEventButton.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Add-Update-Event-Page.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Tambah Kegiatan");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deactivateExpiredEvents() {
        try (Connection conn = DBConnector.connect()) {
            String sql = """
            UPDATE kegiatan_club 
            SET active = 'No'
            WHERE kegiatan_id IN (
                SELECT kegiatan_id 
                FROM jadwal 
                WHERE end_date < CURRENT_DATE
            )
        """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Pane createEventPane(int id, String nama, String jenis, String start, String end, String lokasi, String club) {
        Pane pane = new Pane();
        pane.setPrefSize(1200, 173);
        pane.setStyle("-fx-background-color: gray; -fx-border-color: black;");

        Text namaText = new Text("Nama Event: " + nama);
        namaText.setLayoutX(25); namaText.setLayoutY(37); namaText.setStyle("-fx-fill: white;");

        Text jenisText = new Text("Jenis Event: " + jenis);
        jenisText.setLayoutX(17); jenisText.setLayoutY(71); jenisText.setStyle("-fx-fill: white;");

        Text jadwalText = new Text("Jadwal: " + start + " s.d. " + end);
        jadwalText.setLayoutX(432); jadwalText.setLayoutY(37); jadwalText.setStyle("-fx-fill: white;");

        Text lokasiText = new Text("Lokasi: " + lokasi);
        lokasiText.setLayoutX(406); lokasiText.setLayoutY(70); lokasiText.setStyle("-fx-fill: white;");

        Text clubText = new Text("Club: " + club);
        clubText.setLayoutX(18); clubText.setLayoutY(107); clubText.setStyle("-fx-fill: white;");

        Button update = new Button("✎ Update Event");
        update.setLayoutX(16); update.setLayoutY(132); update.setPrefWidth(258);
        update.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Add-Update-Event-Page.fxml"));
                Parent root = loader.load();
                AddUpdateEventPage controller = loader.getController();
                controller.loadEventData(id);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Event");
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        update.setStyle("-fx-background-color: black; -fx-text-fill: white");

        Button view = new Button("👁 View Info");
        view.setLayoutX(291); view.setLayoutY(131); view.setPrefWidth(240);
        view.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/View-Info-Event-Page.fxml"));
                Parent root = loader.load();

                ViewInfoEventPage controller = loader.getController();
                controller.loadPesertaData(id); // `id` adalah kegiatan_id yang dikirim dari createEventPane

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("View Info Event");
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        view.setStyle("-fx-background-color: black; -fx-text-fill: white");


        Button delete = new Button("⊠ Delete Event");
        delete.setLayoutX(549); delete.setLayoutY(131); delete.setPrefWidth(247);
        delete.setOnAction(e -> {
            try (Connection conn = DBConnector.connect()) {
                PreparedStatement delJadwal = conn.prepareStatement("DELETE FROM jadwal WHERE kegiatan_id = ?");
                delJadwal.setInt(1, id);
                delJadwal.executeUpdate();

                PreparedStatement delKegiatan = conn.prepareStatement("DELETE FROM kegiatan_club WHERE kegiatan_id = ?");
                delKegiatan.setInt(1, id);
                delKegiatan.executeUpdate();

                ((VBox) pane.getParent()).getChildren().remove(pane); // hilangkan dari tampilan

                ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kegiatan berhasil dihapus.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan saat menghapus kegiatan.");
            }
        });
        delete.setStyle("-fx-background-color: black; -fx-text-fill: white");

        pane.getChildren().addAll(namaText, jenisText, jadwalText, lokasiText, clubText, update, view, delete);
        return pane;
    }



    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Profile-Page-Admin.fxml", "Profile Page Admin");
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Personal-Club-Page.fxml", "Personal Club Page");
    }

    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login Page");
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Profile Page");
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

    public void eventDatePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Event-Date-Page.fxml", "Event Date Page");
    }

    public void addUpdateEventPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Add-Update-Event-Page.fxml", "Update Event Page");
    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
