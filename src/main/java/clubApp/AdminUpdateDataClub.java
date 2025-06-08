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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminUpdateDataClub {
    @FXML
    private ComboBox<String>idClub;
    ArrayList<String>temp = new ArrayList<>();
    String selectedId = "";

    @FXML
    private TextField namaClub;
    @FXML
    private TextField deskripsi;
    @FXML
    private TextField tahunBerdiri;

    @FXML
    private Label notification;

    @FXML
    private ComboBox<String>kategoriNama;
    String selectedKategoriId = "";
    String selectedKategori = "";

    @FXML
    private TextArea data;


    public void initialize(){
        ArrayList<String>x = new ArrayList<>();
        ArrayList<String>namaKategori = new ArrayList<>();
        String temp1 = "";
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club d JOIN kategori_club k ON (d.kategori_id = k.kategori_id)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                temp.add(rs.getString("club_id"));
                temp1 += "ID : "+rs.getString("club_id")+"\n";
                temp1 += "Nama Club : "+rs.getString("nama_club")+"\n";
                temp1 += "Deskripsi Club : "+rs.getString("deskripsi_club")+"\n";
                temp1 += "Tahun berdiri : "+rs.getString("tahun_berdiri_club")+"\n";
                temp1 += "Kategori ID : "+rs.getString("kategori_id")+"\n";
                temp1 += "Nama Kategori : "+rs.getString("nama_kategori")+"\n";
                temp1 += "Pendiri Club ID : "+rs.getString("pendiri_club_id")+"\n";
                temp1 += "===========================================================\n";
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        data.setText(temp1);

        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM kategori_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                if (!x.contains(rs.getString("kategori_id"))){
                    x.add(rs.getString("kategori_id"));
                    namaKategori.add(rs.getString("nama_kategori"));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        idClub.getItems().addAll(temp);
        idClub.setOnAction(e->{
            selectedId = idClub.getValue();
        });

        kategoriNama.getItems().addAll(namaKategori);
        kategoriNama.setOnAction(e ->{
            selectedKategori = kategoriNama.getValue();
            for (int i = 0; i<namaKategori.size(); i++){
                if (selectedKategori.equalsIgnoreCase(namaKategori.get(i))){
                    selectedKategoriId = x.get(i);
                }
            }
        });

    }

    @FXML
    public void handleSubmit(){
        ArrayList<String>x = new ArrayList<>();
        if (selectedId == null || selectedId.isEmpty()) {
            notification.setText("Silakan pilih ID club yang ingin diperbarui.");
            return;
        }

        String namaCl = namaClub.getText().trim();
        String desk = deskripsi.getText().trim();
        String tahunBerdiriStr = tahunBerdiri.getText().trim();

        ArrayList<String> elemen = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        if (!namaCl.isEmpty()) {
            elemen.add("nama_club = ?");
            values.add(namaCl);
        }

        if (!desk.isEmpty()) {
            elemen.add("deskripsi_club = ?");
            values.add(desk);
        }

        if (!tahunBerdiriStr.isEmpty()) {
            try {
                int tahunberdiri = Integer.parseInt(tahunBerdiriStr);
                elemen.add("tahun_berdiri_club = ?");
                values.add(tahunberdiri);
            } catch (NumberFormatException e) {
                notification.setText("Tahun berdiri harus berupa angka.");
                return;
            }
        }

        if (selectedKategoriId != null && !selectedKategoriId.isEmpty()) {
            elemen.add("kategori_id = ?");
            values.add(selectedKategoriId);
        }

        if (elemen.isEmpty()) {
            notification.setText("Tidak ada data yang diubah.");
            return;
        }

        String query = "UPDATE data_club SET " + String.join(", ", elemen) + " WHERE club_id = ?";

        try (Connection conn = DBConnector.connect()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.setString(values.size() + 1, selectedId);

            int result = stmt.executeUpdate();
            if (result > 0) {
                notification.setText("Data berhasil diperbarui.");
                String temp1 = "";
                try(Connection connn = DBConnector.connect()){
                    String queryy = "SELECT * FROM data_club d JOIN kategori_club k ON (d.kategori_id = k.kategori_id)";
                    PreparedStatement stmtt = conn.prepareStatement(queryy);
                    ResultSet rs = stmtt.executeQuery();
                    while (rs.next()){
                        temp.add(rs.getString("club_id"));
                        if (!x.contains(rs.getString("kategori_id"))){
                            x.add(rs.getString("kategori_id"));
                        }
                        temp1 += "ID : "+rs.getString("club_id")+"\n";
                        temp1 += "Nama Club : "+rs.getString("nama_club")+"\n";
                        temp1 += "Deskripsi Club : "+rs.getString("deskripsi_club")+"\n";
                        temp1 += "Tahun berdiri : "+rs.getString("tahun_berdiri_club")+"\n";
                        temp1 += "Kategori ID : "+rs.getString("kategori_id")+"\n";
                        temp1 += "Nama Kategori : "+rs.getString("nama_kategori")+"\n";
                        temp1 += "Pendiri Club ID : "+rs.getString("pendiri_club_id")+"\n";
                        temp1 += "===========================================================\n";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                data.setText(temp1);
            } else {
                notification.setText("Update gagal. Periksa kembali ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            notification.setText("Terjadi kesalahan saat update.");
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
