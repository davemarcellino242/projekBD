package clubApp.Admin;

import Model.pesertaEvent;
import currentUser.SessionManager;
import db.DBConnector;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewInfoEventPage {

    @FXML private TableView<pesertaEvent> table;
    @FXML private TableColumn<pesertaEvent, String> nrpColumn;
    @FXML private TableColumn<pesertaEvent, String> namaColumn;
    @FXML private TableColumn<pesertaEvent, String> programColumn;
    @FXML private TableColumn<pesertaEvent, String> statusColumn;
    @FXML private TableColumn<pesertaEvent, String> tanggalRegistColumn;
    @FXML private TableColumn<pesertaEvent, String> sertiColumn;

    private ObservableList<pesertaEvent> dataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        table.setEditable(true);

        nrpColumn.setCellValueFactory(new PropertyValueFactory<>("nrp"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        tanggalRegistColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalRegist"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusHadir"));
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn("HADIR", "TIDAK HADIR"));
        statusColumn.setOnEditCommit(event -> {
            pesertaEvent peserta = event.getRowValue();
            peserta.setStatusHadir(event.getNewValue());
            updateStatusInDatabase(peserta.getNrp(), event.getNewValue(), "status_hadir");
        });

        sertiColumn.setCellValueFactory(new PropertyValueFactory<>("sertifikat"));
        sertiColumn.setCellFactory(ComboBoxTableCell.forTableColumn("YES", "NO"));
        sertiColumn.setOnEditCommit(event -> {
            pesertaEvent peserta = event.getRowValue();
            peserta.setSertifikat(event.getNewValue());
            updateStatusInDatabase(peserta.getNrp(), event.getNewValue(), "sertifikat_peserta");
        });
    }

    public void loadPesertaData(int kegiatanId) {
        dataList.clear();
        String query = """
                SELECT d.nrp, d.nama, pr.nama_program, r.status_registrasi, r.tanggal_registrasi, p.status_hadir, p.sertifikat_peserta
                        FROM data_registrasi_kegiatan r
                        JOIN peserta_kegiatan p ON r.registrasi_id = p.registrasi_id
                        JOIN data_mahasiswa d ON d.nrp = r.nrp
                		JOIN program pr ON d.program_id = pr.program_id
                        WHERE r.kegiatan_id = ?
        """;

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, kegiatanId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dataList.add(new pesertaEvent(
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("nama_program"),
                        rs.getString("status_hadir"),
                        rs.getDate("tanggal_registrasi").toString(),
                        rs.getString("sertifikat_peserta")
                ));
            }

            table.setItems(dataList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatusInDatabase(String nrp, String newValue, String field) {
        String sql = "UPDATE peserta_kegiatan SET " + field + " = ? " +
                "WHERE registrasi_id = (SELECT registrasi_id FROM data_registrasi_kegiatan WHERE nrp = ?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, nrp);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void profilePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page Admin");
        stage.show();
        stage.centerOnScreen();
    }

    public void personalClubPageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Personal-Club-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Personal Club Page");
        stage.show();
        stage.centerOnScreen();
    }

    public void logOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Log_in.fxml"));
        SessionManager.clearSession();
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/General/Home-Page.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Profile Page");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    public void onHomePageAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Home-Page-Admin.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Home Page Admin");
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
