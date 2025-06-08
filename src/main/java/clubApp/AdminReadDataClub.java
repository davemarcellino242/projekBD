package clubApp;

import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminReadDataClub {
    @FXML
    private TextArea data;
    String temp = "";
    public void initialize(){
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += "Club ID : "+rs.getString("club_id")+"\n";
                temp += "Nama Club : "+rs.getString("nama_club")+"\n";
                temp += "Deskripsi Club : "+rs.getString("deskripsi_club")+"\n";
                temp += "Tahun berdiri Club : "+rs.getInt("tahun_berdiri_club")+"\n";
                temp += "Kategori ID : "+rs.getString("kategori_id")+"\n";
                temp += "Pendiri Club ID : "+rs.getString("pendiri_club_id")+"\n";
                temp += "==================================================================";
            }
            data.setText(temp);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_page_for_admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Main Page");
        stage.show();
    }
}
