package clubApp.General;

import DAO.ClubDAO;
import DAO.KeanggotaanDAO;
import DAO.MahasiswaDAO;
import Model.club;
import Model.keanggotaan;
import Model.mahasiswa;
import currentUser.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    private List<Integer> userJoinedClubsIds;
    @FXML
    public void initialize() {
        userJoinedClubsIds = new ArrayList<>();
        String nrp = SessionManager.getCurrentUser().getNrp();

        try{
            userJoinedClubsIds = KeanggotaanDAO.getClubIdsByNrp(nrp);
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
                KeanggotaanDAO keanggotaanDAO = new KeanggotaanDAO();

                MahasiswaDAO mahasiswaDAO = new MahasiswaDAO(); // Perlu cara yang lebih baik untuk mendapatkan koneksi
                mahasiswa mhs = mahasiswaDAO.getMahasiswaByNrp(nrp);

                ClubDAO clubDAO = new ClubDAO();
                club targetClub = clubDAO.getClubById(club.getClubId());

                if (mhs != null && targetClub != null) {
                    // Mendapatkan ID keanggotaan berikutnya
                    int newKeanggotaanId = keanggotaanDAO.getNextKeanggotaanId();
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

                    // Tambahkan club ID ke list lokal dan muat ulang tampilan
                    userJoinedClubsIds.add(club.getClubId());
                    loadAndDisplayClubs(nrp);

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

    @FXML
    private void loadAndDisplayClubs(String nrp) {
        List<club> allClubs = new ArrayList<>();
        try {
            ClubDAO clubDAO = new ClubDAO();
            allClubs = clubDAO.getAllClubs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (allClubContainer != null) {
            allClubContainer.getChildren().clear();
        } else {
            return;
        }

        // Posisi awal dari kartu pertama (sesuai FXML)
        double startX = 23.0;
        double startY = 24.0;

        // Ukuran dan jarak
        double cardWidth = 378.0;
        double cardHeight = 200.0;
        double hGap = 20.0; // jarak horizontal antar card
        double vGap = 20.0; // jarak vertikal antar card
        int cardsPerRow = 2;

        int index = 0;
        for (club cl : allClubs) {
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

        // Optional: otomatis tinggi Pane disesuaikan jika perlu
        int totalRows = (int) Math.ceil((double) index / cardsPerRow);
        double totalHeight = startY + totalRows * (cardHeight + vGap);
        allClubContainer.setPrefHeight(totalHeight);
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
    }
    @FXML
    public void profilePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Profile-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
    }

    @FXML
    public void allClubPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/All-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("All Club Page");
        stage.show();
    }

    @FXML
    public void myClubPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/My-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("My Club Page");
        stage.show();
    }

    @FXML
    public void eventPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Event-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Event Page");
        stage.show();
    }

    @FXML
    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        SessionManager.clearSession();
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
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
