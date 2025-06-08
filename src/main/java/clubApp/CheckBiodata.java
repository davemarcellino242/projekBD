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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckBiodata {

    @FXML
    private Label nrp;

    @FXML
    private Label nama;

    @FXML
    private Label email;

    @FXML
    private Label tanggalLahir;

    @FXML
    private Label program;

    @FXML
    private Label programStudi;

    @FXML
    private Label fakultas;

    String programId;
    String programStudiId;
    String fakultasId;
    public void initialize() {

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM data_mahasiswa WHERE nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,CurrentUser.getCurrentUserNRP());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                nrp.setText("NRP : "+rs.getString("nrp"));
                nama.setText("Nama : "+rs.getString("nama"));
                email.setText("Email : "+rs.getString("email"));
                programId = rs.getString("program_id");
                tanggalLahir.setText("Tanggal lahir : "+rs.getString("tanggal_lahir"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM program WHERE program_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,programId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                program.setText("Program : "+rs.getString("nama_program"));
                programStudiId = rs.getString("program_studi_id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM program_studi WHERE program_studi_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,programStudiId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                programStudi.setText("Program Studi : "+rs.getString("nama_program_studi"));
                fakultasId = rs.getString("fakultas_id");
            }
        }catch (SQLException e){
            e.printStackTrace();;
        }

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM fakultas WHERE fakultas_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,fakultasId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                fakultas.setText("Fakultas : "+rs.getString("nama_fakultas"));
            }
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
