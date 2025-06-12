package clubApp.Admin;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AddUpdateNewClubPage {

    @FXML private TextField namaClub;
    @FXML private TextField namaPendiriClub;
    @FXML private TextField idPendiriClub;
    @FXML private TextField organisasiProfessional;
    @FXML private TextField idOrganisasiProfessional;
    @FXML private TextField kategoriClub;
    @FXML private TextField tahunBerdiriClub;
    @FXML private TextArea deskripsiClub;
    @FXML private ComboBox<Integer> pilihanKe;

    int tempPilihan = 1;
    int clubIdCurrent = 0;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 2; i++) {
            pilihanKe.getItems().add(i);
        }
        pilihanKe.setOnAction(e -> {
            tempPilihan = pilihanKe.getValue();
            loadClubByIndex(tempPilihan);
        });
        loadClubByIndex(tempPilihan);
    }

    public void loadClubByIndex(int index) {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM data_club d " +
                    "JOIN keanggotaan k ON d.club_id = k.club_id " +
                    "JOIN kategori_club ka ON d.kategori_id = ka.kategori_id " +
                    "JOIN pendiri_club p ON d.pendiri_club_id = p.pendiri_club_id " +
                    "LEFT JOIN organisasi_professional o ON p.organisasi_professional_id = o.organisasi_professional_id " +
                    "WHERE k.peran = 'Admin' AND k.nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();
            int count = 1;
            while (rs.next()) {
                if (count == index) {
                    namaClub.setText(rs.getString("nama_club"));
                    namaPendiriClub.setText(rs.getString("nama_pendiri_club"));
                    idPendiriClub.setText(rs.getString("pendiri_club_id"));
                    organisasiProfessional.setText(rs.getString("nama_organisasi_professional"));
                    idOrganisasiProfessional.setText(rs.getString("organisasi_professional_id"));
                    kategoriClub.setText(rs.getString("nama_kategori"));
                    tahunBerdiriClub.setText(String.valueOf(rs.getInt("tahun_berdiri_club")));
                    deskripsiClub.setText(rs.getString("deskripsi_club"));
                    clubIdCurrent = rs.getInt("club_id");
                    break;
                }
                count++;
            }
        } catch (SQLException e) {}
    }

    @FXML
    public void doneButton() {
        String tempNamaClub = namaClub.getText();
        String tempIdPendiriClub = idPendiriClub.getText();
        String tempNamaPendiriClub = namaPendiriClub.getText();

        if (tempNamaClub.isEmpty() || tempIdPendiriClub.isEmpty() || tempNamaPendiriClub.isEmpty()) {
            return;
        }

        boolean dataBaru = false;
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM data_club WHERE club_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, clubIdCurrent);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                dataBaru = true;
            }
        } catch (SQLException e) {}

        if (dataBaru) {
            int maxId = 0;
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT MAX(club_id) AS max_id FROM data_club";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) maxId = rs.getInt("max_id");
            } catch (SQLException e) {}
            maxId++;

            int maxKategori = 0;
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT MAX(kategori_id) AS max_kat FROM kategori_club";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) maxKategori = rs.getInt("max_kat");
            } catch (SQLException e) {}
            maxKategori++;

            try (Connection conn = DBConnector.connect()) {
                String query = "INSERT INTO data_club VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, maxId);
                stmt.setString(2, tempNamaClub);
                stmt.setString(3, deskripsiClub.getText());
                stmt.setInt(4, Integer.parseInt(tahunBerdiriClub.getText()));
                stmt.setInt(5, maxKategori);
                stmt.setString(6, tempIdPendiriClub);
                stmt.executeUpdate();
            } catch (SQLException e) {}

            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM pendiri_club WHERE nama_pendiri_club = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaPendiriClub);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    try (Connection connn = DBConnector.connect()) {
                        String queryy = "INSERT INTO pendiri_club VALUES (?, ?, ?)";
                        PreparedStatement stmtt = connn.prepareStatement(queryy);
                        stmtt.setString(1, tempIdPendiriClub);
                        stmtt.setString(2, tempNamaPendiriClub);
                        stmtt.setString(3, idOrganisasiProfessional.getText());
                        stmtt.executeUpdate();
                    } catch (SQLException e) {}
                }
            } catch (SQLException e) {}

        } else {
            int kategoriId = 0;
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM kategori_club WHERE nama_kategori = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, kategoriClub.getText());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    kategoriId = rs.getInt("kategori_id");
                }
            } catch (SQLException e) {}

            try (Connection conn = DBConnector.connect()) {
                String query = "UPDATE data_club SET nama_club = ?, deskripsi_club = ?, tahun_berdiri_club = ?, kategori_id = ?, pendiri_club_id = ? WHERE club_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaClub);
                stmt.setString(2, deskripsiClub.getText());
                stmt.setInt(3, Integer.parseInt(tahunBerdiriClub.getText()));
                stmt.setInt(4, kategoriId);
                stmt.setString(5, tempIdPendiriClub);
                stmt.setInt(6, clubIdCurrent);
                stmt.executeUpdate();
            } catch (SQLException e) {}
        }
    }

    // Navigasi halaman

    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Personal-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Personal Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        SessionManager.clearSession();
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Home-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page Admin");
        stage.show();
        stage.centerOnScreen();
    }
}
