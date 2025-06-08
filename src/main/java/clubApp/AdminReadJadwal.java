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

public class AdminReadJadwal {
    @FXML
    private TextArea data;
    String temp = "";
    int count = 1;

    public void initialize(){
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM jadwal j\n" +
                    "JOIN kegiatan_club k ON (j.kegiatan_id = k.kegiatan_id)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += count +". \n";
                temp += "Jadwal ID : "+rs.getInt("jadwal_id")+"\n";
                temp += "Tanggal : "+rs.getDate("tanggal")+"\n";
                temp += "Kegiatan ID : "+rs.getString("kegiatan_id")+"\n";
                temp += "Nama Kegiatan : "+rs.getString("nama_kegiatan")+"\n";
                temp += "====================================================\n";
                count++;
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
