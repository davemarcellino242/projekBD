package clubApp.Admin;

import DAO.KeanggotaanDAO;
import Model.*;
import currentUser.SessionManager;
import currentUser.SwitchPage;
import db.DBConnector;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewInfoPersonalClub {

    @FXML private TextField namaClub;
    @FXML private TextField namaPendiriClub;
    @FXML private TextField idPendiriClub;
    @FXML private TextField organisasiProfessional;
    @FXML private TextField idOrganisasiProfessional;
    @FXML private TextField kategoriClub;
    @FXML private TextField tahunBerdiriClub;
    @FXML private TextArea deskripsiClub;
    @FXML private TableView<anggotaTable> anggota;
    @FXML private TableColumn<anggotaTable, String> nrpColumn;
    @FXML private TableColumn<anggotaTable, String> nameColumn;
    @FXML private TableColumn<anggotaTable, String> programColumn;
    @FXML private TableColumn<anggotaTable, String> statusColumn;
    @FXML private TableColumn<anggotaTable, String> peranColumn;

    @FXML private int currentClubId = -1;

    public void initialize() {
        anggota.setEditable(true);

        nrpColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMhs().getNrp()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMhs().getNama()));
        programColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMhs().getNamaProgram()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        peranColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPeran()));

        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Active", "Inactive"));
        statusColumn.setOnEditCommit(event -> {
            anggotaTable selected = event.getRowValue();
            String newValue = event.getNewValue();
            selected.setStatus(newValue); // update tampilan tabel
            selected.getKg().setStatus(newValue); // update objek DAO
            try {
                new KeanggotaanDAO().updateStatusKeanggotaan(
                        selected.getKg().getKeanggotaanId(),
                        newValue
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        peranColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Admin", "Member"));
        peranColumn.setOnEditCommit(event -> {
            anggotaTable selected = event.getRowValue();
            String newValue = event.getNewValue();
            String oldValue = selected.getPeran(); // untuk validasi

            String nrp = selected.getMhs().getNrp();

            if ("Admin".equals(newValue)) {
                if (isAlreadyAdminInTwoClubs(nrp)) {
                    showAlert("Peringatan", "Member ini sudah menjadi admin pada 2 club");
                    anggota.refresh();
                    return;
                }
            }

            if ("Admin".equals(oldValue) && "Member".equals(newValue)) {
                // Cek apakah ini adalah satu-satunya admin di club
                if (isOnlyOneAdminInClub(currentClubId)) {
                    showAlert("Peringatan", "Setiap club harus memiliki minimal 1 admin");
                    anggota.refresh();
                    return;
                }
            }

            // Update peran di tampilan dan database
            selected.setPeran(newValue);
            selected.getKg().setPeran(newValue);
            try {
                new KeanggotaanDAO().updatePeranKeanggotaan(
                        selected.getKg().getKeanggotaanId(),
                        newValue
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void setClubId(int clubId){
        this.currentClubId = clubId;
        loadClubInfo(clubId);
        loadAnggotaClub(clubId);
    }

    private void loadAnggotaClub(int clubId) {
        ObservableList<anggotaTable> anggotaList = FXCollections.observableArrayList();

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT k.keanggotaan_id, k.status, k.peran, k.tanggal_bergabung, " +
                    "k.club_id, m.nrp, m.nama, p.nama_program " +
                    "FROM keanggotaan k " +
                    "JOIN data_mahasiswa m ON k.nrp = m.nrp " +
                    "JOIN program p ON m.program_id = p.program_id " +
                    "WHERE k.club_id = ? " +
                    "ORDER BY CASE WHEN k.peran = 'Admin' THEN 0 " +
                    "WHEN k.peran = 'Member' THEN 1 ELSE 2 END";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                mahasiswa mhs = new mahasiswa(
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        "", null,
                        new program("", rs.getString("nama_program"), null),
                        false
                );

                club clb = new club(rs.getInt("club_id"), "", "",
                        0, null, null);

                keanggotaan kg = new keanggotaan(
                        rs.getInt("keanggotaan_id"),
                        rs.getString("peran"),
                        rs.getString("status"),
                        rs.getDate("tanggal_bergabung").toLocalDate(),
                        mhs,
                        clb
                );
                anggotaList.add(new anggotaTable(kg));
            }

            anggota.setItems(anggotaList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadClubInfo(int clubId) {
        try (Connection conn = db.DBConnector.connect()) {
            String query = "SELECT d.nama_club, p.nama_pendiri_club, p.pendiri_club_id, " +
                    "o.organisasi_professional, o.organisasi_professional_id, " +
                    "ka.nama_kategori, d.tahun_berdiri_club, d.deskripsi_club " +
                    "FROM data_club d " +
                    "JOIN pendiri_club p ON d.pendiri_club_id = p.pendiri_club_id " +
                    "LEFT JOIN organisasi_professional o ON p.organisasi_professional_id = o.organisasi_professional_id " +
                    "JOIN kategori_club ka ON d.kategori_id = ka.kategori_id " +
                    "WHERE d.club_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                namaClub.setText(rs.getString("nama_club"));
                namaPendiriClub.setText(rs.getString("nama_pendiri_club"));
                idPendiriClub.setText(rs.getString("pendiri_club_id"));
                organisasiProfessional.setText(rs.getString("organisasi_professional"));
                idOrganisasiProfessional.setText(rs.getString("organisasi_professional_id"));
                kategoriClub.setText(rs.getString("nama_kategori"));
                tahunBerdiriClub.setText(String.valueOf(rs.getInt("tahun_berdiri_club")));
                deskripsiClub.setText(rs.getString("deskripsi_club"));
            }

            // Buat semua field tidak bisa diedit
            namaClub.setEditable(false);
            namaPendiriClub.setEditable(false);
            idPendiriClub.setEditable(false);
            organisasiProfessional.setEditable(false);
            idOrganisasiProfessional.setEditable(false);
            kategoriClub.setEditable(false);
            tahunBerdiriClub.setEditable(false);
            deskripsiClub.setEditable(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isOnlyOneAdminInClub(int clubId) {
        String query = "SELECT COUNT(*) AS total FROM keanggotaan WHERE club_id = ? AND peran = 'Admin'";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isAlreadyAdminInTwoClubs(String nrp) {
        String query = "SELECT COUNT(*) AS total FROM keanggotaan WHERE nrp = ? AND peran = 'Admin'";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nrp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") >= 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Profile-Page-Admin.fxml", "Profile Page Admin");
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Personal-Club-Page.fxml", "Personal Club Page");
    }

    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        SwitchPage.navigate(event, "/General/Log_in.fxml", "Login Page");
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/General/Home-Page.fxml", "Profile Page");
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

    public void eventDatePage(ActionEvent event) throws IOException {
        SwitchPage.navigate(event, "/Admin/Event-Date-Page.fxml", "Event Date Page");
    }

}
