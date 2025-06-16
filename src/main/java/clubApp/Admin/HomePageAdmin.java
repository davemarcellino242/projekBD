package clubApp.Admin;

import java.sql.Connection;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HomePageAdmin {
    @FXML
    private Connection conn;
    @FXML
    private Text WelcomeText;
    public void initialize(){
        try{
            conn = DBConnector.connect();
            MahasiswaDAO dao = new MahasiswaDAO();

            String currentUserNrp = SessionManager.getCurrentUser().getNrp();
            mahasiswa mhs = dao.getMahasiswaByNrp(currentUserNrp);

            String username = mhs.getNama();
            WelcomeText.setText("Welcome " + username + " to our admin page");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addNewClub(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Add-Update-New-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Add / Update New Club");
        stage.show();
        stage.centerOnScreen();
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
