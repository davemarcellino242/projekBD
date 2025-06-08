package clubApp;

import currentUser.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageForAdmin {
    @FXML
    public void createDataClub(ActionEvent event)throws IOException{
        switchToAdminCreateDataClub(event);
    }
    @FXML
    public void switchToAdminCreateDataClub(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_create_data_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin create page");
        stage.show();
    }

    @FXML
    public void readDataClub(ActionEvent event)throws IOException{
        switchToAdminReadDataClub(event);
    }
    @FXML
    public void switchToAdminReadDataClub(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_read_data_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin read page");
        stage.show();
    }

    @FXML
    public void updateDataClub(ActionEvent event)throws IOException{
        switchToAdminUpdateDataClub(event);
    }
    @FXML
    public void switchToAdminUpdateDataClub(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_update_data_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin update page");
        stage.show();
    }

    @FXML
    public void deleteDataClub(ActionEvent event)throws IOException{
        switchToAdminDeleteDataClub(event);
    }
    @FXML
    public void switchToAdminDeleteDataClub(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_delete_data_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin delete page");
        stage.show();
    }


    @FXML
    public void createJadwal(ActionEvent event)throws IOException{
        switchToAdminCreateJadwal(event);
    }
    @FXML
    public void switchToAdminCreateJadwal(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_create_jadwal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin create page");
        stage.show();
    }

    @FXML
    public void readJadwal(ActionEvent event)throws IOException{
        switchToAdminReadJadwal(event);
    }
    @FXML
    public void switchToAdminReadJadwal(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_read_jadwal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin read page");
        stage.show();
    }

    @FXML
    public void updateJadwal(ActionEvent event)throws IOException{
        switchToAdminUpdateJadwal(event);
    }
    @FXML
    public void switchToAdminUpdateJadwal(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_update_jadwal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin update page");
        stage.show();
    }

    @FXML
    public void deleteJadwal(ActionEvent event)throws IOException{
        switchToAdminDeleteJadwal(event);
    }
    @FXML
    public void switchToAdminDeleteJadwal(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin_delete_jadwal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin delete page");
        stage.show();
    }

    @FXML
    public void back(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Log In Page");

        stage.show();
    }
}
