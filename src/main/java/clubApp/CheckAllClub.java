package clubApp;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckAllClub {
    @FXML
    private TextArea data;

    String temp = "";
    public void initialize(){
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT \n" +
                    "d.club_id, d.nama_club, d.deskripsi_club, d.tahun_berdiri_club, k.nama_kategori, p.nama_pendiri_club, o.nama_organisasi_professional\n" +
                    "FROM data_club d\n" +
                    "LEFT JOIN kategori_club k ON (d.kategori_id = k.kategori_id)\n" +
                    "LEFT JOIN pendiri_club p ON (d.pendiri_club_id = p.pendiri_club_id)\n" +
                    "LEFT JOIN organisasi_professional o ON (o.organisasi_professional_id = p.organisasi_professional_id)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                temp += "Club id : "+rs.getString("club_id")+"\n";
                temp += "Nama club : "+rs.getString("nama_club")+"\n";
                temp += "Deskripsi club : "+rs.getString("deskripsi_club")+"\n";
                temp += "Tahun berdiri club : "+rs.getInt("tahun_berdiri_club")+"\n";
                temp += "Kategori : "+rs.getString("nama_kategori")+"\n";
                temp += "Pendiri : "+rs.getString("nama_pendiri_club")+"\n";
                temp += "Nama organisasi : "+rs.getString("nama_organisasi_professional")+"\n";
                temp += "------------------------------------\n";
            }
            data.setText(temp);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void back(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main_page_for_member.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Member Main Page");
        stage.show();
    }

}
