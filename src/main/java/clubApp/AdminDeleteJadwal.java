package clubApp;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminDeleteJadwal {

    @FXML
    private ComboBox<Integer> jadwalId;
    Integer selectedJadwalId;
    @FXML
    private ComboBox<Date> tanggal;
    Date selectedTanggal;
    @FXML
    private ComboBox<String> kegiatanId;
    String selectedKegiatanId;
    @FXML
    private TextArea data;

    @FXML
    private Label notification;

    public void initialize() {
        jadwalId.getItems().clear();
        tanggal.getItems().clear();
        kegiatanId.getItems().clear();
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM jadwal";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            StringBuilder temp = new StringBuilder();
            while (rs.next()) {
                temp.append("Jadwal ID : ").append(rs.getInt("jadwal_id")).append("\n");
                temp.append("Tanggal : ").append(rs.getDate("tanggal")).append("\n");
                temp.append("Kegiatan Club ID : ").append(rs.getString("kegiatan_id")).append("\n");
                temp.append("======================================================\n");
                int id = rs.getInt("jadwal_id");
                Date tgl = rs.getDate("tanggal");
                String kegiatan = rs.getString("kegiatan_id");
                if (!jadwalId.getItems().contains(id)) {
                    jadwalId.getItems().add(id);
                }
                if (!tanggal.getItems().contains(tgl)) {
                    tanggal.getItems().add(tgl);
                }
                if (!kegiatanId.getItems().contains(kegiatan)) {
                    kegiatanId.getItems().add(kegiatan);
                }
            }
            data.setText(temp.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        jadwalId.setOnAction(e ->{
            selectedJadwalId = jadwalId.getValue();
        });
        tanggal.setOnAction(e ->{
            selectedTanggal = tanggal.getValue();
        });
        kegiatanId.setOnAction(e ->{
            selectedKegiatanId = kegiatanId.getValue();
        });
    }

    @FXML
    public void submit(){
        ArrayList<String>condition = new ArrayList<>();
        ArrayList<Object>values = new ArrayList<>();
        if (selectedJadwalId!=null){
            condition.add("jadwal_id = ?");
            values.add(selectedJadwalId);
        }
        if (selectedTanggal!=null){
            condition.add("tanggal = ?");
            values.add(selectedTanggal);
        }
        if (selectedKegiatanId!=null && !selectedKegiatanId.isEmpty()){
            condition.add("kegiatan_id = ?");
            values.add(selectedKegiatanId);
        }
        if (condition.isEmpty()) {
            notification.setText("Harap pilih minimal satu kriteria untuk menghapus.");
            return;
        }
        String query = "DELETE FROM jadwal WHERE ";
        query += String.join(" AND ", condition);
        try(Connection conn = DBConnector.connect()){
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                notification.setText("Berhasil menghapus " + affectedRows + " baris.");
                StringBuilder temp = new StringBuilder();
                try(Connection connn = DBConnector.connect()){
                    String queryy = "SELECT * FROM jadwal";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    ResultSet rs = stmtt.executeQuery();
                    while (rs.next()){
                        temp.append("Jadwal ID : ").append(rs.getInt("jadwal_id")).append("\n");
                        temp.append("Tanggal : ").append(rs.getDate("tanggal")).append("\n");
                        temp.append("Kegiatan Club ID : ").append(rs.getString("kegiatan_id")).append("\n");
                        temp.append("======================================================\n");
                    }
                    data.setText(temp.toString());
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
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
