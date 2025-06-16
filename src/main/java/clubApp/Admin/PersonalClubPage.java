package clubApp.Admin;

import currentUser.SessionManager;
import currentUser.SwitchPage;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonalClubPage {

    @FXML
    Button profilePageAdmin, personalClubPage;

    @FXML
    private AnchorPane rootPane;

    private final List<Integer> clubIds = new ArrayList<>();

    @FXML
    public void initialize() {
        loadUserClubs();
    }

    @FXML
    private void viewClubInfo(int clubId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/View-Info-Personal-Club.fxml"));
            Parent root = loader.load();

            ViewInfoPersonalClub controller = loader.getController();
            controller.setClubId(clubId);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Club Info");
            stage.show();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadUserClubs() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT d.club_id, d.nama_club FROM data_club d " +
                    "JOIN keanggotaan k ON d.club_id = k.club_id " +
                    "WHERE k.peran = 'Admin' AND k.nrp = ? ORDER BY d.club_id";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();

            int layoutX = 270;
            while (rs.next()) {
                int clubId = rs.getInt("club_id");
                String namaClub = rs.getString("nama_club");
                clubIds.add(clubId);

                Pane clubPane = createClubPane(clubId, namaClub, layoutX);
                rootPane.getChildren().add(clubPane);
                layoutX += 420;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Pane createClubPane(int clubId, String namaClub, int layoutX) {
        Pane pane = new Pane();
        pane.setLayoutX(layoutX);
        pane.setLayoutY(264);
        pane.setPrefSize(405, 185);
        pane.setStyle("-fx-background-color: linear-gradient(to bottom, #0D47A1, #1565C0, #42A5F5); -fx-background-radius: 20;");

        Button updateButton = new Button("✎ Update Data Club");
        updateButton.setLayoutX(16);
        updateButton.setLayoutY(132);
        updateButton.setPrefSize(375, 28);
        updateButton.setStyle("-fx-background-color: linear-gradient(to bottom, #B9FBC0, #34D399, #059669);");
        updateButton.setOnAction(e -> goToUpdatePage(clubId));

        Button viewInfo = new Button("👁 View Info");
        viewInfo.setLayoutX(248);
        viewInfo.setLayoutY(93);
        viewInfo.setPrefSize(143, 28);
        viewInfo.setStyle("-fx-background-color: white;");
        viewInfo.setOnAction(e -> viewClubInfo(clubId));

        Button deleteButton = new Button("⊠ Delete Club");
        deleteButton.setLayoutX(248);
        deleteButton.setLayoutY(14);
        deleteButton.setPrefSize(143, 28);
        deleteButton.setStyle("-fx-background-color: linear-gradient(to bottom, #FF6B6B, #FF0000, #990000);");
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Are you sure you want to delete this club?");
            alert.setContentText("Club ID: " + clubId);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteClub(clubId);
            }
        });

        javafx.scene.text.Text text = new javafx.scene.text.Text(namaClub);
        text.setLayoutX(235);
        text.setLayoutY(73);
        text.setStyle("-fx-fill: white; -fx-font-size: 26px; -fx-font-family: 'Berlin Sans FB';");

        Pane imageBox = new Pane();
        imageBox.setLayoutX(16);
        imageBox.setLayoutY(15);
        imageBox.setPrefSize(200, 103);
        imageBox.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        pane.getChildren().addAll(updateButton, viewInfo, deleteButton, text, imageBox);
        return pane;
    }

    private void goToUpdatePage(int clubId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Add-Update-New-Club-Page.fxml"));
            Parent root = loader.load();

            AddUpdateNewClubPage controller = loader.getController();
            int index = clubIds.indexOf(clubId);
            if (index != -1) {
                controller.pilihanKe(index + 1);
            }

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add or Update Club");
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteClub(int clubId) {
        try (Connection conn = DBConnector.connect()) {
            // Hapus dari tabel utama
            PreparedStatement deleteClub = conn.prepareStatement("DELETE FROM data_club WHERE club_id = ?");
            deleteClub.setInt(1, clubId);
            deleteClub.executeUpdate();

            // Refresh halaman (dengan load ulang scene)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Personal-Club-Page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Personal Club Page");
            stage.show();
            stage.centerOnScreen();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
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

}
