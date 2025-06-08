package clubApp.General;

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

public class ProfilePage {
    @FXML
    Button profilePage, homeProfilePage, allClubProfilePage, myClubProfilePage, eventProfilePage, logOutProfilePage;

    @FXML
    private TextField nrpField, namaField, emailField, tanggalLahirField, fakultasField, prodiField, programField;

    @FXML
    private Connection conn;
    @FXML
    public void initialize(){
        try{
            conn = DBConnector.connect();
            MahasiswaDAO dao = new MahasiswaDAO(conn);

            String currentUserNrp = SessionManager.getCurrentUser().getNrp();
            mahasiswa mhs = dao.getMahasiswaByNrp(currentUserNrp);

            if(mhs != null){
                nrpField.setText(mhs.getNrp());
                namaField.setText(mhs.getNama());
                emailField.setText(mhs.getEmail());
                tanggalLahirField.setText(mhs.getTanggalLahir().toString());
                fakultasField.setText(mhs.getNamaFakultas());
                prodiField.setText(mhs.getNamaProgramStudi());
                programField.setText(mhs.getNamaProgram());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

}
