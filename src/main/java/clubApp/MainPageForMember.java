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

public class MainPageForMember {
    @FXML
    public void checkBiodata(ActionEvent event) throws IOException {
        switchToCheckBiodata(event);
    }

    @FXML
    public void checkAllClub(ActionEvent event) throws IOException{
        switchToCheckAllClub(event);
    }

    @FXML
    public void checkAllFollowedClub(ActionEvent event) throws  IOException{
        switchToCheckAllFollowedClub(event);
    }

    @FXML
    public void registerClub(ActionEvent event)throws IOException{
        switchToRegisterClub(event);
    }

    @FXML
    public void registerClubEvent(ActionEvent event)throws IOException{
        switchToRegisterClubEvent(event);
    }

    @FXML
    public void checkSertificate(ActionEvent event)throws IOException{
        switchToCheckSertificate(event);
    }

    @FXML
    public void checkFollowedClubActivity(ActionEvent event) throws IOException{
        switchToCheckFollowedClubActivity(event);
    }

    @FXML
    public void switchToCheckBiodata(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Check_biodata.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cek Biodata");
        stage.show();
    }

    @FXML
    public void switchToCheckAllClub(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Check_all_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cek semua club");
        stage.show();
    }

    @FXML
    public void switchToCheckAllFollowedClub(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Check_all_followed_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cek semua club yang diikuti");
        stage.show();
    }

    @FXML
    public void switchToRegisterClub(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register_club.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Daftar club");
        stage.show();
    }

    @FXML
    public void switchToRegisterClubEvent(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registrasi_kegiatan.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Daftar kegiatan club");
        stage.show();
    }

    @FXML
    public void switchToCheckSertificate(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Check_sertificate.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cek sertifikat");
        stage.show();
    }

    @FXML
    public void switchToCheckFollowedClubActivity(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Check_followed_club_activity.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Kegiatan club yang diikuti");
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
