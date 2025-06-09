package clubApp.Admin;

import DAO.MahasiswaDAO;
import Model.mahasiswa;
import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ProfilePageAdmin {

    @FXML
    private TextField nrpField, namaField, emailField, tanggalLahirField, fakultasField, prodiField, programField;

    @FXML
    private Connection conn;
    @FXML
    public void initialize(){
        try{
            conn = DBConnector.connect();
            MahasiswaDAO dao = new MahasiswaDAO();

            String currentUserNrp = SessionManager.getCurrentUser().getNrp();
            mahasiswa mhs = dao.getMahasiswaByNrp(currentUserNrp);

            if(mhs != null){
                nrpField.setText(mhs.getNrp());
                nrpField.setEditable(false);
                namaField.setText(mhs.getNama());
                namaField.setEditable(false);
                emailField.setText(mhs.getEmail());
                emailField.setEditable(false);
                tanggalLahirField.setText(mhs.getTanggalLahir().toString());
                tanggalLahirField.setEditable(false);
                fakultasField.setText(mhs.getNamaFakultas());
                fakultasField.setEditable(false);
                prodiField.setText(mhs.getNamaProgramStudi());
                prodiField.setEditable(false);
                programField.setText(mhs.getNamaProgram());
                programField.setEditable(false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Personal-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Personal Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        SessionManager.clearSession();
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Home-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

}
