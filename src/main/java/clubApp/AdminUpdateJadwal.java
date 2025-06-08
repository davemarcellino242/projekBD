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
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminUpdateJadwal {
    @FXML
    private ComboBox<Integer> jadwalId;
    private ArrayList<Integer> tempJadwalId = new ArrayList<>();
    private Integer selectedJadwalId;

    @FXML
    private ComboBox<Integer> comboTanggal;
    @FXML
    private ComboBox<Integer> comboBulan;
    @FXML
    private ComboBox<Integer> comboTahun;
    private Integer selectedTanggal;
    private Integer selectedBulan;
    private Integer selectedTahun;

    @FXML
    private ComboBox<String> kegiatanClub;
    private ArrayList<String> tempKegiatanClubId = new ArrayList<>();
    private String selectedKegiatanClubId;
    ArrayList<String>namaKegiatanClub = new ArrayList<>();
    String selectedNamaKegiatanClub;

    @FXML
    private Label notification;
    @FXML
    private Label notificationTanggal;
    @FXML
    private TextArea data;

    @FXML
    public void initialize() {
        // Load Jadwal data
        StringBuilder temp = new StringBuilder();
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM jadwal j JOIN kegiatan_club k ON (j.kegiatan_id = k.kegiatan_id) JOIN data_club d ON (k.club_id = d.club_id)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                temp.append("Jadwal ID : ").append(rs.getInt("jadwal_id")).append("\n");
                temp.append("Tanggal : ").append(rs.getDate("tanggal")).append("\n");
                temp.append("Kegiatan Club ID : ").append(rs.getString("kegiatan_id")).append("\n");
                temp.append("Nama kegiatan : ").append(rs.getString("nama_kegiatan")).append("\n");
                temp.append("Nama Club : ").append(rs.getString("nama_club")).append("\n");
                temp.append("======================================================\n");
                tempJadwalId.add(rs.getInt("jadwal_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.setText(temp.toString());
        jadwalId.getItems().addAll(tempJadwalId);
        jadwalId.setOnAction(e -> selectedJadwalId = jadwalId.getValue());

        // Combo Tanggal
        for (int i = 1; i <= 31; i++) {
            comboTanggal.getItems().add(i);
        }
        for (int i = 1; i <= 12; i++) {
            comboBulan.getItems().add(i);
        }
        for (int i = 2025; i <= 2100; i++) {
            comboTahun.getItems().add(i);
        }

        comboTanggal.setOnAction(e -> selectedTanggal = comboTanggal.getValue());
        comboBulan.setOnAction(e -> selectedBulan = comboBulan.getValue());
        comboTahun.setOnAction(e -> selectedTahun = comboTahun.getValue());

        // Load kegiatan_club
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM kegiatan_club k JOIN data_club d ON (k.club_id = d.club_id)";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tempKegiatanClubId.add(rs.getString("kegiatan_id"));
                namaKegiatanClub.add(rs.getString("nama_kegiatan")+" ("+rs.getString("nama_club")+")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        kegiatanClub.getItems().addAll(namaKegiatanClub);
        kegiatanClub.setOnAction(e -> {
            selectedNamaKegiatanClub = kegiatanClub.getValue();
            for (int i = 0; i<namaKegiatanClub.size(); i++){
                if (selectedNamaKegiatanClub.equalsIgnoreCase(namaKegiatanClub.get(i))){
                    selectedKegiatanClubId = tempKegiatanClubId.get(i);
                }
            }
        });
    }

    @FXML
    public void submit() {
        if (selectedJadwalId == null) {
            notification.setText("ID Jadwal harus diisi");
            return;
        }

        ArrayList<String> condition = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        // Validasi tanggal
        int count = 0;
        if (selectedTanggal != null) count++;
        if (selectedBulan != null) count++;
        if (selectedTahun != null) count++;

        if (count >= 1 && count <= 2) {
            notificationTanggal.setText("Lengkapi tanggal yang ingin diubah");
            return;
        } else if (count == 3) {
            LocalDate localDate = LocalDate.of(selectedTahun, selectedBulan, selectedTanggal);
            condition.add("tanggal = ?");
            values.add(localDate);
        }

        // Validasi kegiatan
        if (selectedKegiatanClubId != null && !selectedKegiatanClubId.isEmpty()) {
            condition.add("kegiatan_id = ?");
            values.add(selectedKegiatanClubId);
        }

        if (condition.isEmpty()) {
            notification.setText("Tidak ada perubahan data");
            return;
        }

        String query = "UPDATE jadwal SET " + String.join(", ", condition) + " WHERE jadwal_id = ?";
        values.add(selectedJadwalId); // terakhir untuk WHERE clause

        try (Connection conn = DBConnector.connect()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            int result = stmt.executeUpdate();
            if (result > 0) {
                notification.setText("Data berhasil diubah");

                // Refresh data
                StringBuilder temp = new StringBuilder();
                try (Connection connn = DBConnector.connect()) {
                    String queryy = "SELECT * FROM jadwal j JOIN kegiatan_club k ON (j.kegiatan_id = k.kegiatan_id) JOIN data_club d ON (k.club_id = d.club_id)";
                    PreparedStatement stmtt = connn.prepareStatement(queryy);
                    ResultSet rs = stmtt.executeQuery();
                    while (rs.next()) {
                        temp.append("Jadwal ID : ").append(rs.getInt("jadwal_id")).append("\n");
                        temp.append("Tanggal : ").append(rs.getDate("tanggal")).append("\n");
                        temp.append("Kegiatan Club ID : ").append(rs.getString("kegiatan_id")).append("\n");
                        temp.append("Nama kegiatan : ").append(rs.getString("nama_kegiatan")).append("\n");
                        temp.append("Nama Club : ").append(rs.getString("nama_club")).append("\n");
                        temp.append("======================================================\n");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                data.setText(temp.toString());
            } else {
                notification.setText("Tidak ada data yang sesuai");
            }
        } catch (SQLException e) {
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
