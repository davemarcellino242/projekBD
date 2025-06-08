package clubApp;

import currentUser.CurrentUser;
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

public class CheckFollowedClubActivity {
    @FXML
    private TextArea data;
    String temp = "";
    int count = 1;

    public void initialize(){
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_registrasi_kegiatan d\n" +
                    "JOIN kegiatan_club k ON (d.kegiatan_id = k.kegiatan_id)\n" +
                    "JOIN jadwal j ON (k.kegiatan_id = j.kegiatan_id) WHERE d.nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, CurrentUser.getCurrentUserNRP());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += count +". "+rs.getString("nama_kegiatan")+"\n";
                temp += "Tanggal : "+rs.getDate("tanggal")+"\n";
                temp += "====================================================\n";
                count++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        data.setText(temp);
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
