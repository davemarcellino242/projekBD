package clubApp.Admin;

import currentUser.SessionManager;
import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddUpdateNewClubPage {

    @FXML private TextField namaClub;
    @FXML private TextField namaPendiriClub;
    @FXML private TextField idPendiriClub;
    @FXML private TextField organisasiProfessional;
    @FXML private TextField idOrganisasiProfessional;
    @FXML private TextField tahunBerdiriClub;
    @FXML private TextArea deskripsiClub;
    @FXML private ComboBox<Integer> pilihanKe;
    @FXML private ComboBox<String> kategoriClub;


    private final List<Integer> clubIds = new ArrayList<>();
    private int clubIdCurrent = 0;

    @FXML
    public void initialize() {
        loadUserClubs();
        loadKategoriList();
        pilihanKe.getItems().addAll(1, 2);

        pilihanKe.setOnAction(e -> {
            int selectedIndex = pilihanKe.getValue() - 1;
            if (selectedIndex < clubIds.size()) {
                loadClubById(clubIds.get(selectedIndex));
            } else {
                clearAllFields();
            }
        });

        pilihanKe.setValue(1);
        if (!clubIds.isEmpty()) {
            loadClubById(clubIds.get(0));
        } else {
            clearAllFields();
        }
    }

    private void loadKategoriList() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT nama_kategori FROM kategori_club ORDER BY nama_kategori";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            kategoriClub.getItems().clear();
            while (rs.next()) {
                kategoriClub.getItems().add(rs.getString("nama_kategori"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserClubs() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT d.club_id FROM data_club d " +
                    "JOIN keanggotaan k ON d.club_id = k.club_id " +
                    "WHERE k.peran = 'Admin' AND k.nrp = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, SessionManager.getCurrentUser().getNrp());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clubIds.add(rs.getInt("club_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClubById(int clubId) {
        try (Connection conn = DBConnector.connect()) {
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
                kategoriClub.setValue(rs.getString("nama_kategori"));
                tahunBerdiriClub.setText(String.valueOf(rs.getInt("tahun_berdiri_club")));
                deskripsiClub.setText(rs.getString("deskripsi_club"));
                clubIdCurrent = clubId;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearAllFields() {
        namaClub.clear();
        namaPendiriClub.clear();
        idPendiriClub.clear();
        organisasiProfessional.clear();
        idOrganisasiProfessional.clear();
        tahunBerdiriClub.clear();
        deskripsiClub.clear();
        clubIdCurrent = 0;
    }

    @FXML
    public void doneButton() {
        if (namaClub.getText().isEmpty() || namaPendiriClub.getText().isEmpty() || idPendiriClub.getText().isEmpty()) {
            return;
        }

        if (pilihanKe.getValue() > clubIds.size()) {
            createNewClub();
        } else {
            updateExistingClub();
        }
    }

    private void createNewClub() {
        try (Connection conn = DBConnector.connect()) {
            Set<Integer> usedIds = new HashSet<>();
            PreparedStatement usedStmt = conn.prepareStatement("SELECT club_id FROM data_club");
            ResultSet usedRs = usedStmt.executeQuery();
            while (usedRs.next()) {
                usedIds.add(usedRs.getInt("club_id"));
            }
            int newClubId = 1;
            while (usedIds.contains(newClubId)) {
                newClubId++;
            }

            updatePendiriIfNeeded(conn, idPendiriClub.getText(), namaPendiriClub.getText(), idOrganisasiProfessional.getText());

            int kategoriId = getKategoriId(conn, kategoriClub.getValue());
            if (kategoriId == -1) {
                PreparedStatement getMaxKat = conn.prepareStatement("SELECT MAX(kategori_id) AS max_kat FROM kategori_club");
                ResultSet rs2 = getMaxKat.executeQuery();
                if (rs2.next()) kategoriId = rs2.getInt("max_kat") + 1;

                PreparedStatement insertKat = conn.prepareStatement("INSERT INTO kategori_club VALUES (?, ?)");
                insertKat.setInt(1, kategoriId);
                insertKat.setString(2, kategoriClub.getValue());
                insertKat.executeUpdate();
            }

            PreparedStatement insertClub = conn.prepareStatement("INSERT INTO data_club VALUES (?, ?, ?, ?, ?, ?)");
            insertClub.setInt(1, newClubId);
            insertClub.setString(2, namaClub.getText());
            insertClub.setString(3, deskripsiClub.getText());
            insertClub.setInt(4, Integer.parseInt(tahunBerdiriClub.getText()));
            insertClub.setInt(5, kategoriId);
            insertClub.setString(6, idPendiriClub.getText());
            insertClub.executeUpdate();

            Set<Integer> usedKeanggotaanIds = new HashSet<>();
            PreparedStatement getUsedIds = conn.prepareStatement("SELECT keanggotaan_id FROM keanggotaan");
            ResultSet rsUsed = getUsedIds.executeQuery();
            while (rsUsed.next()) {
                usedKeanggotaanIds.add(rsUsed.getInt("keanggotaan_id"));
            }
            int newKeanggotaanId = 1;
            while (usedKeanggotaanIds.contains(newKeanggotaanId)) {
                newKeanggotaanId++;
            }


            PreparedStatement insertKeanggotaan = conn.prepareStatement(
                    "INSERT INTO keanggotaan (keanggotaan_id, peran, status, tanggal_bergabung, nrp, club_id) VALUES (?, 'Admin', 'Active', ?, ?, ?)"
            );
            insertKeanggotaan.setInt(1, newKeanggotaanId); //
            insertKeanggotaan.setDate(2, Date.valueOf(LocalDate.now())); // tanggal_bergabung
            insertKeanggotaan.setString(3, SessionManager.getCurrentUser().getNrp()); // nrp
            insertKeanggotaan.setInt(4, newClubId);
            insertKeanggotaan.executeUpdate();

            clubIds.add(newClubId);
            clubIdCurrent = newClubId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pilihanKe(int index) {
        pilihanKe.setValue(index);
        if (pilihanKe.getOnAction() != null) {
            pilihanKe.getOnAction().handle(null);
        }
    }

    private void updateExistingClub() {
        try (Connection conn = DBConnector.connect()) {
            int kategoriId = getKategoriId(conn, kategoriClub.getValue());
            if (kategoriId == -1) return;
            
            updatePendiriIfNeeded(conn, idPendiriClub.getText(), namaPendiriClub.getText(), idOrganisasiProfessional.getText());
            PreparedStatement update = conn.prepareStatement(
                    "UPDATE data_club SET nama_club = ?, deskripsi_club = ?, tahun_berdiri_club = ?, kategori_id = ?, pendiri_club_id = ? WHERE club_id = ?"
            );
            update.setString(1, namaClub.getText());
            update.setString(2, deskripsiClub.getText());
            update.setInt(3, Integer.parseInt(tahunBerdiriClub.getText()));
            update.setInt(4, kategoriId);
            update.setString(5, idPendiriClub.getText());
            update.setInt(6, clubIdCurrent);
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePendiriIfNeeded(Connection conn, String idPendiri, String namaPendiri, String orgProfId) throws SQLException {
        // Cek apakah organisasi_professional_id sudah ada
        String checkOrgSQL = "SELECT 1 FROM organisasi_professional WHERE organisasi_professional_id = ?";
        try (PreparedStatement checkOrg = conn.prepareStatement(checkOrgSQL)) {
            checkOrg.setString(1, orgProfId);
            ResultSet rs = checkOrg.executeQuery();

            if (!rs.next()) {
                // Jika belum ada, insert organisasi baru
                String insertOrgSQL = "INSERT INTO organisasi_professional (organisasi_professional_id, organisasi_professional) VALUES (?, ?)";
                try (PreparedStatement insertOrg = conn.prepareStatement(insertOrgSQL)) {
                    insertOrg.setString(1, orgProfId);
                    insertOrg.setString(2, organisasiProfessional.getText()); // Atau ganti dengan parameter kalau mau
                    insertOrg.executeUpdate();
                    System.out.println("Organisasi baru ditambahkan: " + orgProfId);
                }
            }
        }

        // Cek apakah pendiri_club sudah ada
        String checkPendiriSQL = "SELECT 1 FROM pendiri_club WHERE pendiri_club_id = ?";
        try (PreparedStatement checkPendiri = conn.prepareStatement(checkPendiriSQL)) {
            checkPendiri.setString(1, idPendiri);
            ResultSet rs2 = checkPendiri.executeQuery();

            if (!rs2.next()) {

                String insertPendiriSQL = "INSERT INTO pendiri_club (pendiri_club_id, nama_pendiri_club, organisasi_professional_id) VALUES (?, ?, ?)";
                try (PreparedStatement insertPendiri = conn.prepareStatement(insertPendiriSQL)) {
                    insertPendiri.setString(1, idPendiri);
                    insertPendiri.setString(2, namaPendiri);
                    insertPendiri.setString(3, orgProfId);
                    insertPendiri.executeUpdate();
                    System.out.println("Pendiri baru ditambahkan: " + idPendiri);
                }
            }
        }
    }



    private int getKategoriId(Connection conn, String kategoriNama) throws SQLException {
        String query = "SELECT kategori_id FROM kategori_club WHERE nama_kategori = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, kategoriNama);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("kategori_id") : -1;
    }

    // Navigasi
    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        navigate(event, "/Admin/Profile-Page-Admin.fxml", "Profile Page Admin");
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        navigate(event, "/Admin/Personal-Club-Page.fxml", "Personal Club Page");
    }

    public void logOutPage(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        navigate(event, "/General/Log_in.fxml", "Login Page");
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException {
        navigate(event, "/General/Home-Page.fxml", "Home Page");
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        navigate(event, "/Admin/Home-Page-Admin.fxml", "Home Page Admin");
    }

    private void navigate(ActionEvent event, String path, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
        stage.centerOnScreen();
    }

    public void eventDatePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Event-Date-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Event Date Page");
        stage.show();
        stage.centerOnScreen();
    }
}
