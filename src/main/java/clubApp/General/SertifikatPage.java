package clubApp.General;

import currentUser.SessionManager;
import currentUser.SwitchPage;
import db.DBConnector;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SertifikatPage {

    @FXML
    private Pane sertiContainer;

    @FXML
    private ComboBox<Integer> pageSelector;

    private List<Pane> semuaKartu = new ArrayList<>();
    private static final int KARTU_PER_HALAMAN = 4;

    @FXML
    public void initialize() {
        loadDataFromDatabase();
        setupPagination();
        tampilkanHalaman(1);
    }

    private void loadDataFromDatabase() {
        semuaKartu.clear();



        try (Connection conn = DBConnector.connect()){
            String query = """
            SELECT k.nama_kegiatan, c.nama_club, p.status_hadir, r.tanggal_registrasi
            FROM data_registrasi_kegiatan r
            JOIN peserta_kegiatan p ON r.registrasi_id = p.registrasi_id
            JOIN kegiatan_club k ON r.kegiatan_id = k.kegiatan_id
            JOIN data_club c ON k.club_id = c.club_id
            WHERE r.nrp = ? AND p.sertifikat_peserta = 'YES'
        """;

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String namaKegiatan = rs.getString("nama_kegiatan");
                String namaClub = rs.getString("nama_club");
                String statusHadir = rs.getString("status_hadir");
                String tanggal = rs.getDate("tanggal_registrasi").toString();

                System.out.println("> " + namaKegiatan + " - " + namaClub + " - " + statusHadir + " - " + tanggal);

                Pane card = createSertiCardPane(namaKegiatan, namaClub, statusHadir, tanggal);
                semuaKartu.add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPagination() {
        int totalPage = (int) Math.ceil((double) semuaKartu.size() / KARTU_PER_HALAMAN);
        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            pages.add(i);
        }
        pageSelector.setItems(FXCollections.observableArrayList(pages));
        pageSelector.setOnAction(e -> tampilkanHalaman(pageSelector.getValue()));
    }

    private void tampilkanHalaman(int halaman) {

        if (sertiContainer == null) {
            System.err.println("sertiContainer is null!");
            return;
        }

        sertiContainer.getChildren().clear();
        int startIndex = (halaman - 1) * KARTU_PER_HALAMAN;
        int endIndex = Math.min(startIndex + KARTU_PER_HALAMAN, semuaKartu.size());

        double startX = 22.0;
        double startY = 24.0;
        double cardWidth = 378.0;
        double cardHeight = 200.0;
        double hGap = 20.0;
        double vGap = 20.0;
        int cardsPerRow = 2;

        int index = 0;
        for (int i = startIndex; i < endIndex; i++) {
            Pane card = semuaKartu.get(i);

            int col = index % cardsPerRow;
            int row = index / cardsPerRow;

            double x = startX + col * (cardWidth + hGap);
            double y = startY + row * (cardHeight + vGap);

            card.setLayoutX(x);
            card.setLayoutY(y);

            sertiContainer.getChildren().add(card);
            index++;
        }
    }

    private Pane createSertiCardPane(String namaKegiatan, String namaClub, String statusHadir, String tanggal) {
        Pane pane = new Pane();
        pane.setPrefSize(378, 200);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        TextField tfNamaKegiatan = new TextField(namaKegiatan);
        tfNamaKegiatan.setLayoutX(30); tfNamaKegiatan.setLayoutY(14);
        tfNamaKegiatan.setPrefSize(327, 25);
        tfNamaKegiatan.setEditable(false);

        Label clubLabel = new Label("Club :");
        clubLabel.setLayoutX(30); clubLabel.setLayoutY(51);

        TextField tfNamaClub = new TextField(namaClub);
        tfNamaClub.setLayoutX(119); tfNamaClub.setLayoutY(47);
        tfNamaClub.setPrefSize(90, 25);
        tfNamaClub.setEditable(false);

        Label statusLabel = new Label("Status Hadir : ");
        statusLabel.setLayoutX(30); statusLabel.setLayoutY(82);

        TextField tfStatus = new TextField(statusHadir);
        tfStatus.setLayoutX(119); tfStatus.setLayoutY(78);
        tfStatus.setPrefSize(90, 25);
        tfStatus.setEditable(false);

        Label tanggalLabel = new Label("Tanggal :");
        tanggalLabel.setLayoutX(30); tanggalLabel.setLayoutY(114);

        TextField tfTanggal = new TextField(tanggal);
        tfTanggal.setLayoutX(119); tfTanggal.setLayoutY(110);
        tfTanggal.setPrefSize(90, 25);
        tfTanggal.setEditable(false);

        pane.getChildren().addAll(tfNamaKegiatan, clubLabel, tfNamaClub, statusLabel, tfStatus, tanggalLabel, tfTanggal);
        return pane;
    }

    // Navigasi halaman lain
    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Home Page");
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
    public void homePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

    @FXML
    public void sertiPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Sertifikat-Page.fxml", "Sertifikat Page");
    }

    @FXML
    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login Page");
    }


}
