package clubApp.General;

import DAO.ClubDAO;
import DAO.KeanggotaanDAO;
import DAO.MahasiswaDAO;
import Model.club;
import Model.keanggotaan;
import Model.mahasiswa;
import currentUser.SessionManager;
import currentUser.SwitchPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.awt.Color.*;

public class AllClubPage {
    @FXML
    Button profileAllClubPage, homeAllClubPage, allClubPage, myClubAllClubPage, eventAllClubPage, logOutAllClubPage;

    @FXML
    private Pane allClubContainer;
    @FXML
    private ComboBox<Integer> pageSelector;

    private List<club> allClubs = new ArrayList<>();
    private static final int CLUBS_PER_PAGE = 4;
    private int currentPage = 1;
    private List<Integer> userJoinedClubsIds;

    @FXML
    public void initialize() {
        userJoinedClubsIds = new ArrayList<>();
        String nrp = SessionManager.getCurrentUser().getNrp();

        try {
            userJoinedClubsIds = KeanggotaanDAO.getClubIdsByNrp(nrp);
            ClubDAO clubDAO = new ClubDAO();
            allClubs = clubDAO.getAllClubs();

            int totalPages = (int) Math.ceil((double) allClubs.size() / CLUBS_PER_PAGE);
            for (int i = 1; i <= totalPages; i++) {
                pageSelector.getItems().add(i);
            }
            pageSelector.setValue(1); // default halaman 1
            pageSelector.setOnAction(e -> {
                currentPage = pageSelector.getValue();
                loadAndDisplayClubs(nrp);
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        loadAndDisplayClubs(nrp);
    }

    private Pane createClubCard(club club, String nrp) {
        Pane pane = new Pane();
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(378.0);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        pane.setLayoutX(23.0);
        pane.setLayoutY(21.0);

        // UI elements creation based on your FXML (Labels, TextFields, TextArea)
        TextField nameField = new TextField();
        nameField.setLayoutX(30.0);
        nameField.setLayoutY(14.0);
        nameField.setPrefHeight(25.0);
        nameField.setPrefWidth(327.0);
        nameField.setText(club.getNamaClub());
        nameField.setEditable(false);
        pane.getChildren().add(nameField);

        Label tahunBerdiriLabel = new Label("Tahun Berdiri : ");
        tahunBerdiriLabel.setLayoutX(30.0);
        tahunBerdiriLabel.setLayoutY(51.0);
        pane.getChildren().add(tahunBerdiriLabel);

        TextField tahunBerdiriField = new TextField();
        tahunBerdiriField.setLayoutX(119.0);
        tahunBerdiriField.setLayoutY(47.0);
        tahunBerdiriField.setPrefHeight(25.0);
        tahunBerdiriField.setPrefWidth(90.0);
        tahunBerdiriField.setText(String.valueOf(club.getTahunBerdiriClub()));
        tahunBerdiriField.setEditable(false);
        pane.getChildren().add(tahunBerdiriField);

        Label pendiriLabel = new Label("Pendiri :");
        pendiriLabel.setLayoutX(30.0);
        pendiriLabel.setLayoutY(82.0);
        pane.getChildren().add(pendiriLabel);

        TextField pendiriField = new TextField();
        pendiriField.setLayoutX(119.0);
        pendiriField.setLayoutY(78.0);
        pendiriField.setPrefHeight(25.0);
        pendiriField.setPrefWidth(90.0);
        pendiriField.setText(club.getPendiriClubId());
        pendiriField.setEditable(false);
        pane.getChildren().add(pendiriField);

        Label kategoriLabel = new Label("Kategori :");
        kategoriLabel.setLayoutX(30.0);
        kategoriLabel.setLayoutY(114.0);
        pane.getChildren().add(kategoriLabel);

        TextField kategoriField = new TextField();
        kategoriField.setLayoutX(119.0);
        kategoriField.setLayoutY(110.0);
        kategoriField.setPrefHeight(25.0);
        kategoriField.setPrefWidth(90.0);
        kategoriField.setText(club.getNamaKategori());
        kategoriField.setEditable(false);
        pane.getChildren().add(kategoriField);

        Label deskripsiLabel = new Label("Deskripsi :");
        deskripsiLabel.setLayoutX(221.0); deskripsiLabel.setLayoutY(47.0);
        pane.getChildren().add(deskripsiLabel);

        TextArea deskripsiArea = new TextArea();
        deskripsiArea.setLayoutX(221.0);
        deskripsiArea.setLayoutY(70.0);
        deskripsiArea.setPrefHeight(67.0);
        deskripsiArea.setPrefWidth(134.0);
        deskripsiArea.setText(club.getDeskripsiClub());
        deskripsiArea.setEditable(false);
        deskripsiArea.setWrapText(true);
        pane.getChildren().add(deskripsiArea);

        // Join Club Button
        Button joinButton = new Button("► Join Club");
        joinButton.setLayoutX(19.0);
        joinButton.setLayoutY(158.0);
        joinButton.setMnemonicParsing(false);
        joinButton.setPrefHeight(28.0);
        joinButton.setPrefWidth(341.0);
        joinButton.setStyle("-fx-background-color: linear-gradient(to bottom, #0D47A1, #1565C0, #42A5F5);");
        joinButton.setTextFill(Color.WHITE);
        joinButton.setFont(new Font("Berlin Sans FB", 15.0));

        joinButton.setOnAction(event -> {
            try {
                if (userJoinedClubsIds.size() >= 4) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("Batas Klub Tercapai");
                    alert.setHeaderText("Maksimal 4 Klub");
                    alert.setContentText("Anda sudah bergabung dengan 4 klub. Tidak bisa bergabung lagi.");
                    alert.showAndWait();
                    return;
                }

                KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();
                MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
                mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);
                ClubDAO clubDAO = new ClubDAO();
                club targetClub = clubDAO.getClubById(club.getClubId());

                if (mhs != null && targetClub != null) {
                    int newKeanggotaanId = 1;
                    List<Integer> existingIds = keanggotaanDAO.getAllKeanggotaanIds();

                    while (existingIds.contains(newKeanggotaanId)) {
                        newKeanggotaanId++;
                    }
                    String defaultPeran = "Member";
                    String defaultStatus = "Active";
                    LocalDate tanggalBergabung = LocalDate.now();

                    keanggotaan newKeanggotaan = new keanggotaan(
                            newKeanggotaanId,
                            defaultPeran,
                            defaultStatus,
                            tanggalBergabung,
                            mhs,
                            targetClub
                    );

                    keanggotaanDAO.insertKeanggotaan(newKeanggotaan);

                    // Update tombol, tanpa reload semua pane
                    joinButton.setText("✓ Joined");
                    joinButton.setDisable(true);
                    joinButton.setStyle("-fx-background-color: grey;");
                    userJoinedClubsIds.add(club.getClubId());

                    System.out.println("Berhasil bergabung dengan klub: " + club.getNamaClub());
                } else {
                    System.err.println("Gagal bergabung: Mahasiswa atau Club tidak ditemukan.");
                }

            } catch (SQLException e) {
                System.err.println("Error saat bergabung dengan klub: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Terjadi error tidak terduga: " + e.getMessage());
            }
        });
        pane.getChildren().add(joinButton);

        return pane;
    }

    private void loadAndDisplayClubs(String nrp) {
        if (allClubContainer != null) {
            allClubContainer.getChildren().clear();
        } else {
            return;
        }

        int startIndex = (currentPage - 1) * CLUBS_PER_PAGE;
        int endIndex = Math.min(startIndex + CLUBS_PER_PAGE, allClubs.size());

        double startX = 23.0;
        double startY = 24.0;
        double cardWidth = 378.0;
        double cardHeight = 200.0;
        double hGap = 20.0;
        double vGap = 20.0;
        int cardsPerRow = 2;

        int index = 0;
        for (int i = startIndex; i < endIndex; i++) {
            club cl = allClubs.get(i);
            if (!userJoinedClubsIds.contains(cl.getClubId())) {
                Pane clubCard = createClubCard(cl, nrp);

                int col = index % cardsPerRow;
                int row = index / cardsPerRow;

                double x = startX + col * (cardWidth + hGap);
                double y = startY + row * (cardHeight + vGap);

                clubCard.setLayoutX(x);
                clubCard.setLayoutY(y);

                allClubContainer.getChildren().add(clubCard);
                index++;
            }
        }
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
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login page");
    }

    public void homePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

}
