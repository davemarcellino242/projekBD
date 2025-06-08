package clubApp.General;

import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogInController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void initialize() {

    }
    @FXML
    public void logInButton(ActionEvent event) {
        String nrp = usernameField.getText().toLowerCase();
        String inputPassword = passwordField.getText();

        if (nrp.isEmpty() || inputPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Gagal", "NRP dan password harus diisi.");
            return;
        }

        String namaMhs = null;
        try(Connection conn = DBConnector.connect()){
            String query2  = "SELECT nama FROM data_mahasiswa WHERE nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query2);
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                namaMhs = rs.getString("nama");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT tanggal_lahir, signup FROM data_mahasiswa WHERE nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String signUpStatus = rs.getString("signup");

                if (!"yes".equalsIgnoreCase(signUpStatus)) {
                    showAlert(Alert.AlertType.WARNING, "Akun Belum Terdaftar", "Akun belum tedaftar, silakan lakukan sign up terlebih dahulu.");
                    return;
                }

                LocalDate tanggalLahir = rs.getDate("tanggal_lahir").toLocalDate();
                String expectedPassword = tanggalLahir.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                if (expectedPassword.equals(inputPassword)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang " +  namaMhs + "!");
                    switchToHomePage(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Gagal", "Password salah!");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "NRP tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Terjadi kesalahan koneksi ke database.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // bisa diisi jika mau
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void switchToHomePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home page");
        stage.show();
    }

    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Sign_Up.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Sign up");
        stage.show();
    }
}
