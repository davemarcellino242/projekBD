package clubApp.General;

import DAO.ClubDAO;
import DAO.KeanggotaanDAO;
import Model.club;
import Model.keanggotaan;
import currentUser.SessionManager;
import currentUser.SwitchPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class myClubPage {
    @FXML
    Button profileMyClubPage, homeMyClubPage, allClubMyClubPage, myClubPage, eventMyClubPage, logOutMyClubPage;

    @FXML
    private Pane clubContainer;
    private KeanggotaanDAO keanggotaanDAO;
    private ClubDAO clubDAO;
    private Connection conn;

    @FXML
    public void initialize() {
        String nrp = SessionManager.getCurrentUser().getNrp();
        loadClubsForUser(nrp);
    }

    @FXML
    private void loadClubsForUser(String nrp) {
        try {
            keanggotaanDAO = new KeanggotaanDAO();
            List<keanggotaan> keanggotaanList = keanggotaanDAO.getKeanggotaanByNRP(nrp);
            clubDAO = new ClubDAO();

            int column = 0;
            int row = 0;
            double spacingX = 400;
            double spacingY = 240;

            for (int i = 0; i < keanggotaanList.size(); i++) {
                keanggotaan k = keanggotaanList.get(i);
                club club = clubDAO.getClubById(k.getClubId());

                Pane card = createClubCard(club, k.getKeanggotaanId());

                double x = column * spacingX + 20;
                double y = row * spacingY + 20;

                card.setLayoutX(x);
                card.setLayoutY(y);

                clubContainer.getChildren().add(card);

                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Pane createClubCard(club club, int keanggotaanId) {
        Pane card = new Pane();
        card.setPrefSize(378, 200);
        card.setStyle("-fx-background-color: gray;");

        Label nama = new Label(club.getNamaClub());
        nama.setLayoutX(27);
        nama.setLayoutY(14);

        Label tahun = new Label("Tahun Berdiri : " + club.getTahunBerdiriClub());
        tahun.setLayoutX(27);
        tahun.setLayoutY(57);

        Label pendiri = new Label("Pendiri : " + club.getPendiriClubId());
        pendiri.setLayoutX(27);
        pendiri.setLayoutY(100);

        Label orgPro = new Label("Kategori : " + club.getNamaKategori());
        orgPro.setLayoutX(27);
        orgPro.setLayoutY(143);

        Label deskripsi = new Label("Deskripsi :");
        deskripsi.setLayoutX(192);
        deskripsi.setLayoutY(57);

        TextArea textDeskripsi = new TextArea(club.getDeskripsiClub());
        textDeskripsi.setLayoutX(192);
        textDeskripsi.setLayoutY(85);
        textDeskripsi.setPrefSize(160, 85);
        textDeskripsi.setWrapText(true);
        textDeskripsi.setEditable(false);

        Button leaveButton = new Button("Leave");
        leaveButton.setLayoutX(290);
        leaveButton.setLayoutY(14);
        leaveButton.setOnAction(e -> {
            try {
                leaveClubById(keanggotaanId);
                clubContainer.getChildren().remove(card);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        leaveButton.setStyle("-fx-background-color: black; -fx-text-fill: white");

        card.getChildren().addAll(nama, tahun, pendiri, orgPro, deskripsi, textDeskripsi, leaveButton);
        return card;
    }

    private void leaveClubById(int keanggotaanId) throws SQLException {
        keanggotaanDAO.deleteKeanggotaan(keanggotaanId);
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Profile Page");
    }

    @FXML
    public void profilePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Profile-Page.fxml", "Profile Page");
    }

    @FXML
    public void allClubPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/All-Club-Page.fxml", "All Club Page");
    }

    @FXML
    public void myClubPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/My-Club-Page.fxml", "My Club Page");
    }

    @FXML
    public void eventPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Event-Page.fxml", "Event Page");
    }

    @FXML
    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login Page");
    }

    public void homePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

}
