package clubApp.General;

import DAO.MahasiswaDAO;
import Model.mahasiswa;
import currentUser.SessionManager;
import currentUser.SwitchPage;
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
    public void onHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
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
    public void sertiPage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Sertifikat-Page.fxml", "Sertifikat Page");
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
