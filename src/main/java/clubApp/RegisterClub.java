package clubApp;

import currentUser.CurrentUser;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RegisterClub {
    @FXML
    private TextArea data;
    String temp = "";
    int count = 1;

    @FXML
    private TextField kodeClubDaftar;

    @FXML
    private Label notification;

    public void initialize(){
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club d\n" +
                    "LEFT JOIN keanggotaan k ON (d.club_id = k.club_id)\n" +
                    "WHERE d.club_id NOT IN (\n" +
                    "\tSELECT\n" +
                    "\tclub_id\n" +
                    "\tFROM keanggotaan\n" +
                    "\tWHERE nrp = ? " +
                    ")";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,CurrentUser.getCurrentUserNRP());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += count +". "+rs.getString("club_id")+"\n";
                temp += "Nama club : "+rs.getString("nama_club")+"\n";
                temp += "Deskripsi club : "+rs.getString("deskripsi_club")+"\n";
                temp += "Deskripsi : "+rs.getString("deskripsi_club")+"\n";
                temp += "=========================================================\n";
                count++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        data.setText(temp);
    }

    @FXML
    public void handleSubmit(){
        boolean cek = false;
        String kode = kodeClubDaftar.getText().toUpperCase();
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club WHERE club_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,kode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                //benar
                cek = true;
            }
            else {
                notification.setText("Kode Club tidak dikenal");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        int max = 0;
        String password = "";
        if (cek){
            try(Connection conn = DBConnector.connect()){
                String query = "SELECT * FROM keanggotaan";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    max = rs.getInt("keanggotaan_id");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            try(Connection conn = DBConnector.connect()){
                String query = "SELECT * FROM keanggotaan WHERE nrp = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1,CurrentUser.getCurrentUserNRP());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    password = rs.getString("password");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            max++;
        }

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM keanggotaan WHERE nrp = ? AND club_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,CurrentUser.getCurrentUserNRP());
            stmt.setString(2,kode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                cek = false;
                notification.setText("Anda sudah terdaftar");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        if (cek){
            try(Connection conn = DBConnector.connect()){
                String query = "INSERT INTO keanggotaan VALUES(?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                stmt.setString(2,"Active");
                stmt.setString(3,"Member");
                stmt.setString(4,CurrentUser.getCurrentUserNRP());
                stmt.setString(5,kode);
                stmt.setString(6,password);
                stmt.setInt(7,max);
                int rowInserted = stmt.executeUpdate();
                if (rowInserted>0){
                    notification.setText("Data berhasil ditambahkan");
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

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
