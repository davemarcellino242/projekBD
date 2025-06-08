package clubApp.General;

import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpController {

    @FXML
    private TextField nrp;

    @FXML
    private TextField namaLengkap;

    @FXML
    private TextField tanggalLahir;

    @FXML
    private ComboBox<String> fakultas;

    @FXML
    private ComboBox<String> prodi;

    @FXML
    private ComboBox<String> program;

    @FXML
    private Button SignUpLinked;

    @FXML
    private Button backSignUp;

    private final Map<String, List<String>> prodiMap = new HashMap<>();
    private final Map<String, List<String>> programMap = new HashMap<>();

    @FXML
    public void initialize() {
        prodiMap.put("FTI", List.of("IF"));
        programMap.put("IF", List.of("INF", "SIB", "DSA"));

        fakultas.getItems().addAll(prodiMap.keySet());

        fakultas.setOnAction(event -> {
            String selectedFakultas = fakultas.getValue();
            List<String> prodiList = prodiMap.getOrDefault(selectedFakultas, List.of());
            prodi.getItems().setAll(prodiList);
            prodi.getSelectionModel().clearSelection();
            program.getItems().clear();
        });

        prodi.getItems().addAll(programMap.keySet());

        prodi.setOnAction(event -> {
            String selectedProdi = prodi.getValue();
            List<String> programList = programMap.getOrDefault(selectedProdi, List.of());
            program.getItems().setAll(programList);
            program.getSelectionModel().clearSelection();
        });
    }

    @FXML
    public void handleSignUp(ActionEvent event) {
        String inputNRP = nrp.getText().toLowerCase();
        String inputNama = namaLengkap.getText().toUpperCase();
        String inputTanggalLahir = tanggalLahir.getText();
        String inputFakultas = fakultas.getValue();
        String inputProdi = prodi.getValue();
        String inputProgram = program.getValue();

        if (inputNRP.isEmpty() || inputNama.isEmpty() || inputTanggalLahir.isEmpty()
                || inputFakultas == null || inputProdi == null || inputProgram == null) {
            showAlert(AlertType.WARNING, "Pendaftaran Gagal", "Semua kolom harus diisi!");
            return;
        }

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM data_mahasiswa WHERE nrp = ? AND nama = ? AND tanggal_lahir = ? AND program_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, inputNRP);
            stmt.setString(2, inputNama);
            stmt.setDate(3, Date.valueOf(inputTanggalLahir));
            stmt.setString(4, inputProgram);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("signup");
                if ("yes".equalsIgnoreCase(status)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sign Up Gagal", "Kamu sudah pernah sign up.");
                } else {
                    String updateQuery = "UPDATE data_mahasiswa SET signup = 'Yes' WHERE nrp = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, inputNRP);
                    updateStmt.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Sign Up Berhasil", "Akun berhasil didaftarkan!");
                    goToLoginPage(event);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Data Tidak Cocok", "Data tidak ditemukan, pastikan NRP, nama, tanggal lahir, dan program benar.");
            }
        }catch(SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Gagal melakukan proses sign up.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Log in Page");
        stage.show();
    }

    public void back(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Main Page");
        stage.show();
    }

}

