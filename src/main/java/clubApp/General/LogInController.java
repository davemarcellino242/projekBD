package clubApp.General;

import currentUser.SessionManager;
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

        try (Connection conn = DBConnector.connect()) {
            String query = """
            SELECT m.nrp, m.nama, m.email, m.tanggal_lahir, m.signup,
                   p.program_id, p.nama_program,
                   ps.program_studi_id, ps.nama_program_studi,
                   f.fakultas_id, f.nama_fakultas
            FROM data_mahasiswa m
            JOIN program p ON m.program_id = p.program_id
            JOIN program_studi ps ON p.program_studi_id = ps.program_studi_id
            JOIN fakultas f ON ps.fakultas_id = f.fakultas_id
            WHERE m.nrp = ?
        """;

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String signup = rs.getString("signup");
                if (!"yes".equalsIgnoreCase(signup)) {
                    showAlert(Alert.AlertType.WARNING, "Akun Belum Terdaftar", "Silakan sign up terlebih dahulu.");
                    return;
                }

                LocalDate tanggalLahir = rs.getDate("tanggal_lahir").toLocalDate();
                String expectedPassword = tanggalLahir.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                if (!inputPassword.equals(expectedPassword)) {
                    showAlert(Alert.AlertType.ERROR, "Login Gagal", "Password salah!");
                    return;
                }

                // Bangun objek dari tabel berelasi
                Model.fakultas f = new Model.fakultas(
                        rs.getString("fakultas_id"),
                        rs.getString("nama_fakultas")
                );
                Model.programStudi ps = new Model.programStudi(
                        rs.getString("program_studi_id"),
                        rs.getString("nama_program_studi"),
                        f
                );
                Model.program p = new Model.program(
                        rs.getString("program_id"),
                        rs.getString("nama_program"),
                        ps
                );

                Model.mahasiswa mhs = new Model.mahasiswa(
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        tanggalLahir,
                        p,
                        true
                );

                // Simpan user ke session
                SessionManager.setCurrentUser(mhs);

                // Lanjut ke halaman berikut
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang " + mhs.getNama() + "!");
                switchToHomePage(event);

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "NRP tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Terjadi kesalahan pada koneksi database.");
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
        stage.centerOnScreen();
    }

    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Sign_Up.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Sign up");
        stage.show();
        stage.centerOnScreen();
    }
}
