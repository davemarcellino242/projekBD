package clubApp.Admin;

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
        SwitchPage.navigate(event, "/Admin/Profile-Page-Admin.fxml", "Profile Page Admin");
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Personal-Club-Page.fxml", "Personal Club Page");
    }

    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login Page");
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Profile Page");
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

    public void eventDatePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Event-Date-Page.fxml", "Event Date Page");
    }


}
