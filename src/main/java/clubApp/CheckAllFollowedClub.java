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

public class CheckAllFollowedClub {
    @FXML
    private TextArea data;

    public void initialize(){
        int count = 1;
        String temp = "";
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT  *\n" + "FROM keanggotaan k \n" +
                    "JOIN data_club d ON (k.club_id = d.club_id)\n" +
                    "WHERE k.nrp = ? ";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,CurrentUser.getCurrentUserNRP());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += String.valueOf(count)+". "+rs.getString("nama_club")+"\n";
                temp += "Peran : "+rs.getString("peran")+"\n";
                temp += "Status : "+rs.getString("status")+"\n";
                temp += "Tanggal bergabung : "+rs.getString("tanggal_bergabung")+"\n";
                temp += "==============================================\n";
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
