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
import java.util.ArrayList;

public class AdminDeleteDataClub {
    @FXML
    private ComboBox<String> clubId;
    String selectedClubId;
    @FXML
    private ComboBox<String>namaClub;
    String selectedNamaClub;
    @FXML
    private ComboBox<String>deskripsiClub;
    String selectedDeskripsiClub;
    @FXML
    private ComboBox<Integer>tahunBerdiriClub;
    int selectedTahunBerdiriClub;
    @FXML
    private ComboBox<String>kategoriId;
    String selectedKategoriId;
    @FXML
    private ComboBox<String>pendiriClubId;
    String selectedPendiriClubId;

    @FXML
    private TextArea data;
    String temp = "";
    @FXML
    private Label notification;

    public void initialize(){
        try (Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                clubId.getItems().add(rs.getString("club_id"));
                temp += "Club ID : "+rs.getString("club_id")+"\n";
                namaClub.getItems().add(rs.getString("nama_club"));
                temp += "Nama Club : "+rs.getString("nama_club")+"\n";
                deskripsiClub.getItems().add(rs.getString("deskripsi_club"));
                temp += "Deskripsi Club : "+rs.getString("deskripsi_club")+"\n";
                tahunBerdiriClub.getItems().add(rs.getInt("tahun_berdiri_club"));
                temp += "Tahun Berdiri Club : "+rs.getInt("tahun_berdiri_club")+"\n";
                kategoriId.getItems().add(rs.getString("kategori_id"));
                temp += "Kategori ID : "+rs.getString("kategori_id")+"\n";
                pendiriClubId.getItems().add(rs.getString("pendiri_club_id"));
                temp += "Pendiri Club ID : "+rs.getString("pendiri_club_id")+"\n";
                temp += "=========================================================\n";
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        data.setText(temp);

        clubId.setOnAction(e ->{
            selectedClubId = clubId.getValue();
        });
        namaClub.setOnAction(e ->{
            selectedNamaClub = namaClub.getValue();
        });
        deskripsiClub.setOnAction(e -> {
            selectedDeskripsiClub = deskripsiClub.getValue();
        });
        tahunBerdiriClub.setOnAction(e ->{
            selectedTahunBerdiriClub = tahunBerdiriClub.getValue();
        });
        kategoriId.setOnAction(e ->{
            selectedKategoriId = kategoriId.getValue();
        });
        pendiriClubId.setOnAction(e ->{
            selectedPendiriClubId = pendiriClubId.getValue();
        });
    }

    @FXML
    public void handleSubmit() {
        ArrayList<String> conditions = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        if (selectedClubId != null && !selectedClubId.isEmpty()) {
            conditions.add("club_id = ?");
            values.add(selectedClubId);
        }

        if (selectedNamaClub != null && !selectedNamaClub.isEmpty()) {
            conditions.add("nama_club = ?");
            values.add(selectedNamaClub);
        }

        if (selectedDeskripsiClub != null && !selectedDeskripsiClub.isEmpty()) {
            conditions.add("deskripsi_club = ?");
            values.add(selectedDeskripsiClub);
        }

        if (selectedTahunBerdiriClub != 0) {
            conditions.add("tahun_berdiri_club = ?");
            values.add(selectedTahunBerdiriClub);
        }

        if (selectedKategoriId != null && !selectedKategoriId.isEmpty()) {
            conditions.add("kategori_id = ?");
            values.add(selectedKategoriId);
        }

        if (selectedPendiriClubId != null && !selectedPendiriClubId.isEmpty()) {
            conditions.add("pendiri_club_id = ?");
            values.add(selectedPendiriClubId);
        }

        if (conditions.isEmpty()) {
            System.out.println("Harap isi minimal satu field untuk menghapus data.");
            return;
        }

        String query = "DELETE FROM data_club WHERE ";
        query += String.join(" AND ", conditions);

        try (Connection conn = DBConnector.connect()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                notification.setText("Berhasil menghapus " + affectedRows + " baris.");
                String temp = "";
                try(Connection connn = DBConnector.connect()){
                    String queryy = "SELECT * FROM data_club";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    ResultSet rs = stmtt.executeQuery();
                    while (rs.next()){
                        temp += "Club ID : "+rs.getString("club_id")+"\n";
                        temp += "Nama Club : "+rs.getString("nama_club")+"\n";
                        temp += "Deskripsi Club : "+rs.getString("deskripsi_club")+"\n";
                        temp += "Tahun Berdiri Club : "+rs.getInt("tahun_berdiri_club")+"\n";
                        temp += "Kategori ID : "+rs.getString("kategori_id")+"\n";
                        temp += "Pendiri Club ID : "+rs.getString("pendiri_club_id")+"\n";
                        temp += "=========================================================\n";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                data.setText(temp);


            } else {
                notification.setText("Tidak ada data yang cocok untuk dihapus.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat menghapus data.");
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
