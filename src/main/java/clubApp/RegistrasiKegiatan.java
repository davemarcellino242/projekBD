package clubApp;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;

public class RegistrasiKegiatan {
    @FXML
    private TextArea data;
    ArrayList<String>kodeTersedia = new ArrayList<>();

    @FXML
    private ComboBox<String> kodeKegiatan;
    String kodePilih;


    @FXML
    private Label notification;

    public void initialize(){
        String temp = "";
        try(Connection conn = DBConnector.connect()){
            int count = 1;
            String query = "SELECT * \n" +
                    "FROM jadwal j\n" +
                    "JOIN kegiatan_club k ON j.kegiatan_id = k.kegiatan_id\n" +
                    "WHERE j.kegiatan_id NOT IN (\n" +
                    "    SELECT kegiatan_id \n" +
                    "    FROM data_registrasi_kegiatan\n" +
                    "    WHERE nrp = ? \n" +
                    ")\n" +
                    "AND k.club_id IN (\n" +
                    "    SELECT d.club_id\n" +
                    "    FROM data_club d\n" +
                    "    JOIN keanggotaan k ON d.club_id = k.club_id\n" +
                    "    WHERE k.nrp = ? \n" +
                    ");\n";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += count+". "+rs.getString("nama_kegiatan")+"\n";
                temp += "ID : "+rs.getString("kegiatan_id")+"\n";
                temp += "Tanggal : "+rs.getDate("tanggal")+"\n";
                temp += "======================================================\n";
                count++;
                kodeTersedia.add(rs.getString("kegiatan_id"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        data.setText(temp);
        kodeKegiatan.getItems().addAll(kodeTersedia);
        kodeKegiatan.setOnAction(e -> {
            kodePilih = kodeKegiatan.getValue();
        });
    }

    public void submitHandle(){
        int count = 1;
        String temp = "";
        int max = 0;
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_registrasi_kegiatan";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                max = rs.getInt("registrasi_id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        max++;

        boolean cek = false;
        for (int i = 0; i<kodeTersedia.size(); i++){
            if (kodePilih.equalsIgnoreCase(kodeTersedia.get(i))){
                cek = true;
                break;
            }
        }

        try (Connection conn = DBConnector.connect()){
            String query = "INSERT INTO data_registrasi_kegiatan VALUES(?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,"Verified");
            stmt.setDate(2,java.sql.Date.valueOf(LocalDate.now()));

            stmt.setString(4,kodePilih);
            stmt.setInt(5,max);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted>0){
                notification.setText("Data berhasil ditambahkan");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * \n" +
                    "FROM jadwal j\n" +
                    "JOIN kegiatan_club k ON j.kegiatan_id = k.kegiatan_id\n" +
                    "WHERE j.kegiatan_id NOT IN (\n" +
                    "    SELECT kegiatan_id \n" +
                    "    FROM data_registrasi_kegiatan\n" +
                    "    WHERE nrp = ? \n" +
                    ")\n" +
                    "AND k.club_id IN (\n" +
                    "    SELECT d.club_id\n" +
                    "    FROM data_club d\n" +
                    "    JOIN keanggotaan k ON d.club_id = k.club_id\n" +
                    "    WHERE k.nrp = ? \n" +
                    ");\n";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp += count+". "+rs.getString("nama_kegiatan")+"\n";
                temp += "ID : "+rs.getString("kegiatan_id")+"\n";
                temp += "Tanggal : "+rs.getDate("tanggal")+"\n";
                temp += "======================================================\n";
                count++;
                kodeTersedia.add(rs.getString("kegiatan_id"));
            }
        }
        catch (SQLException e){
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
