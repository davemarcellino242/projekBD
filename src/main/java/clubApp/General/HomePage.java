package clubApp.General;

import currentUser.SessionManager;
import currentUser.SwitchPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HomePage {

    @FXML
    Button profilePageHomePage, allClubHomePage, myClubHomePage, eventHomePage, logOutHomePage;


    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Profile Page");
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
    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login page");
    }

    public void homePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }


}
