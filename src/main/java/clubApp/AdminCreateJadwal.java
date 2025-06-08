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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminCreateJadwal {
    @FXML
    private TextField namaKegiatan;
    String selectedKegiatanId;
    String selectedKegiatan;

    @FXML
    private ComboBox<String> namaClub;
    String selectedClub;
    String selectedClubId;

    @FXML
    private TextField lokasi;
    String selectedLokasi;
    int selectedLokasiId;

    @FXML
    private ComboBox<String> jenisKegiatan;
    String selectedJenisKegiatan;
    int selectedJenisKegiatanId;
    ArrayList<String>nama = new ArrayList<>();

    @FXML
    private Label lokasiNotification;
    @FXML
    private Label namaKegiatanNotification;
    @FXML
    private Label jenisKegiatanNotification;
    @FXML
    private Label namaClubNotification;
    @FXML
    private Label notificationHari;
    @FXML
    private ComboBox<Integer> comboTanggal;
    @FXML
    private ComboBox<Integer> comboBulan;
    @FXML
    private ComboBox<Integer> comboTahun;
    @FXML
    private Label notification;

    Integer selectedTanggal;
    Integer selectedBulan;
    Integer selectedTahun;
    public void initialize(){
        //jenis kegiatan
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM jenis_kegiatan";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                jenisKegiatan.getItems().add(rs.getString("nama_jenis_kegiatan"));
                nama.add(rs.getString("nama_jenis_kegiatan"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        jenisKegiatan.setOnAction(e->{
            selectedJenisKegiatan = jenisKegiatan.getValue();
            int index = 1;
            for (int i = 0; i<nama.size(); i++){
                if (jenisKegiatan.getValue().equalsIgnoreCase(nama.get(i))){
                    index += i;
                    selectedJenisKegiatanId = index;
                    break;
                }
            }
        });

        //club
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM data_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                namaClub.getItems().add(rs.getString("nama_club"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        namaClub.setOnAction(e ->{
            selectedClub = namaClub.getValue();
            try(Connection conn = DBConnector.connect()){
                String query = "SELECT * FROM data_club";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    if (selectedClub.equalsIgnoreCase(rs.getString("nama_club"))){
                        selectedClubId = rs.getString("club_id");
                    }
                }
            }
            catch (SQLException f){
                f.printStackTrace();
            }
        });

        for (int i = 1; i <= 27; i++) {
            comboTanggal.getItems().add(i);
        }
        for (int i = 1; i <= 12; i++) {
            comboBulan.getItems().add(i);
        }
        for (int i = 2025; i <= 2100; i++) {
            comboTahun.getItems().add(i);
        }
        comboTanggal.setOnAction(e -> {
            selectedTanggal = comboTanggal.getValue();
        });
        comboBulan.setOnAction(e->{
            selectedBulan = comboBulan.getValue();
        });
        comboTahun.setOnAction(e ->{
            selectedTahun = comboTahun.getValue();
        });


    }

    @FXML
    public void handleSubmit(){
        LocalDate localDate;
        if (selectedTanggal == 0 || selectedBulan ==0 || selectedTahun == 0){
            notificationHari.setText("Jadwal harus diisi semua");
            return;
        }
        else {
            localDate = LocalDate.of(selectedTahun,selectedBulan, selectedTanggal);
        }
        if (selectedJenisKegiatan.isEmpty()){
            jenisKegiatanNotification.setText("Jenis kegiatan harus diisi");
            return;
        }
        else if (selectedClub.isEmpty()) {
            namaClubNotification.setText("Club harus diisi");
            return;
        }

        //lokasi
        selectedLokasi = lokasi.getText();
        if (selectedLokasi.isEmpty()){
            lokasiNotification.setText("Lokasi harus diisi");
            return;
        }
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM lokasi WHERE nama_lokasi = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,selectedLokasi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                selectedLokasiId = rs.getInt("lokasi_id");
            }
            else {
                //mencari yang terbesar
                try(Connection connn = DBConnector.connect()){
                    String queryy = "SELECT * FROM lokasi";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    ResultSet rss = stmtt.executeQuery();
                    while (rss.next()){
                        selectedLokasiId = rss.getInt("lokasi_id");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                selectedLokasiId++;
                try(Connection connn = DBConnector.connect()){
                    String queryy = "INSERT INTO lokasi VALUES(?,?)";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    stmtt.setInt(1,selectedLokasiId);
                    stmtt.setString(2,selectedLokasi);
                    int rowsInserted = stmtt.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //kegiatan
        selectedKegiatan = namaKegiatan.getText();
        if (selectedKegiatan.isEmpty()){
            namaKegiatanNotification.setText("Nama kegiatan harus diisi");
            return;
        }
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM kegiatan_club WHERE nama_kegiatan = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,selectedKegiatan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                selectedKegiatanId = rs.getString("kegiatan_id");
            }
            else {
                selectedKegiatan = namaKegiatan.getText().strip();
                Random ran = new Random();
                String temp = "";
                for (int i = 0; i<2; i++){
                    int index = ran.nextInt(0,selectedKegiatan.length()-1);
                    temp += selectedKegiatan.substring(index,index+1);
                }
                selectedKegiatanId = temp.toUpperCase();
                try(Connection connn = DBConnector.connect()){
                    String queryy = "INSERT INTO kegiatan_club VALUES(?,?,?,?,?)";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    stmtt.setString(1,selectedKegiatanId);
                    stmtt.setString(2,namaKegiatan.getText());
                    stmtt.setString(3,selectedClubId);
                    stmtt.setInt(4,selectedLokasiId);
                    stmtt.setInt(5,selectedJenisKegiatanId);
                    int rowsInserted = stmtt.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        int max = 0;
        try(Connection conn = DBConnector.connect()){
            String query = "SELECT * FROM jadwal";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                max = rs.getInt("jadwal_id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        max++;

        try(Connection conn = DBConnector.connect()){
            String query = "INSERT INTO jadwal VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,max);
            stmt.setDate(2,java.sql.Date.valueOf(localDate));
            stmt.setString(3,selectedKegiatanId);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted>0){
                notification.setText("Jadwal berhasil ditambahkan");
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
