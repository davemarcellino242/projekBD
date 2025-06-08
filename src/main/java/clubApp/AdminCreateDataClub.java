package clubApp;

import db.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class AdminCreateDataClub {
    private ArrayList<String> listIdOrganisasi = new ArrayList<>();
    private ArrayList<String> listIdPendiriClub = new ArrayList<>();
    private ArrayList<String> listIdKategoriClub = new ArrayList<>();
    private ArrayList<String> listIdClub = new ArrayList<>();

    public void initialize() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM organisasi_professional";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listIdOrganisasi.add(rs.getString("organisasi_professional_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM pendiri_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listIdPendiriClub.add(rs.getString("pendiri_club_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM kategori_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listIdKategoriClub.add(rs.getString("kategori_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM data_club";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listIdClub.add(rs.getString("club_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField namaOrganisasiProfessional;
    @FXML
    private TextField namaPendiriClub;
    @FXML
    private TextField namaKategori;
    @FXML
    private TextField namaClub;
    @FXML
    private TextField deskripsi;
    @FXML
    private Label notificationPendiri;
    @FXML
    private Label notificationKategori;
    @FXML
    private Label notificationClub;

    @FXML
    public void handleSubmit() {
        Random ran = new Random();
        String tempNamaOrganisasiProfessional = namaOrganisasiProfessional.getText().trim();
        String tempIdOrganisasiProfessional = "";
        String tempNamaPendiriClub = namaPendiriClub.getText().trim();
        String tempIdPendiriClub = "";
        String tempNamaKategori = namaKategori.getText().trim();
        String tempIdKategori = "";
        String tempNamaClub = namaClub.getText().trim();
        String tempIdClub = "";
        String tempDeskripsi = deskripsi.getText().trim();

        // organisasi
        if (tempNamaOrganisasiProfessional.isBlank()) {
            tempIdOrganisasiProfessional = null;
        } else {
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM organisasi_professional WHERE nama_organisasi_professional = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaOrganisasiProfessional);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tempIdOrganisasiProfessional = rs.getString("organisasi_professional_id");
                } else {
                    while (true) {
                        String temp = "";
                        for (int i = 0; i < 2; i++) {
                            int x = ran.nextInt(tempNamaOrganisasiProfessional.length());
                            temp += tempNamaOrganisasiProfessional.substring(x, x + 1);
                        }
                        if (!listIdOrganisasi.contains(temp.toUpperCase())) {
                            tempIdOrganisasiProfessional = temp.toUpperCase();
                            listIdOrganisasi.add(tempIdOrganisasiProfessional);
                            break;
                        }
                    }
                    try (Connection connn = DBConnector.connect()) {
                        String queryy = "INSERT INTO organisasi_professional VALUES(?,?)";
                        PreparedStatement stmtt = connn.prepareStatement(queryy);
                        stmtt.setString(1, tempIdOrganisasiProfessional);
                        stmtt.setString(2, tempNamaOrganisasiProfessional);
                        stmtt.executeUpdate();
                        System.out.println("Organisasi ditambahkan.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // kategori
        if (tempNamaKategori.isBlank()) {
            notificationKategori.setText("Kategori harus diisi");
        } else {
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM kategori_club WHERE nama_kategori = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaKategori);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tempIdKategori = rs.getString("kategori_id");
                }
                else {
                    while (true) {
                        String temp = "";
                        for (int i = 0; i < 2; i++) {
                            int x = ran.nextInt(tempNamaKategori.length());
                            temp += tempNamaKategori.charAt(x);
                        }
                        if (!listIdKategoriClub.contains(temp.toUpperCase())) {
                            tempIdKategori = temp.toUpperCase();
                            listIdKategoriClub.add(tempIdKategori);
                            break;
                        }
                    }
                    try (Connection connn = DBConnector.connect()) {
                        String queryy = "INSERT INTO kategori_club VALUES(?,?)";
                        PreparedStatement stmtt = connn.prepareStatement(queryy);
                        stmtt.setString(1, tempIdKategori);
                        stmtt.setString(2, tempNamaKategori);
                        stmtt.executeUpdate();
                        System.out.println("Kategori ditambahkan.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (tempNamaPendiriClub.isBlank()) {
            notificationPendiri.setText("Nama pendiri club harus diisi");
        } else {
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM pendiri_club WHERE nama_pendiri_club = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaPendiriClub);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tempIdPendiriClub = rs.getString("pendiri_club_id");
                } else {
                    while (true) {
                        StringBuilder temp = new StringBuilder();
                        for (int i = 0; i < 2; i++) {
                            int x = ran.nextInt(tempNamaPendiriClub.length());
                            temp.append(tempNamaPendiriClub.charAt(x));
                        }
                        String generatedId = temp.toString().toUpperCase();
                        if (!listIdPendiriClub.contains(generatedId)) {
                            tempIdPendiriClub = generatedId;
                            listIdPendiriClub.add(tempIdPendiriClub);
                            break;
                        }
                    }

                    String insertQuery = "INSERT INTO pendiri_club (pendiri_club_id, nama_pendiri_club, organisasi_professional_id) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, tempIdPendiriClub);
                        insertStmt.setString(2, tempNamaPendiriClub);
                        if (tempIdOrganisasiProfessional == null || tempIdOrganisasiProfessional.isBlank()) {
                            insertStmt.setNull(3, Types.VARCHAR);
                        } else {
                            insertStmt.setString(3, tempIdOrganisasiProfessional);
                        }
                        insertStmt.executeUpdate();
                        System.out.println("Pendiri baru berhasil ditambahkan.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // club
        boolean dataLengkap = !tempNamaClub.isBlank() && !tempNamaKategori.isBlank() && !tempNamaPendiriClub.isBlank()
                && tempIdPendiriClub != null && tempIdKategori != null;

        if (dataLengkap) {
            try (Connection conn = DBConnector.connect()) {
                String query = "SELECT * FROM data_club WHERE nama_club = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, tempNamaClub);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    while (true) {
                        String temp = "";
                        for (int i = 0; i < 2; i++) {
                            int x = ran.nextInt(tempNamaClub.length());
                            temp += tempNamaClub.substring(x, x + 1);
                        }
                        if (!listIdClub.contains(temp.toUpperCase())) {
                            tempIdClub = temp.toUpperCase();
                            listIdClub.add(tempIdClub);
                            break;
                        }
                    }

                    String insertQuery = "INSERT INTO data_club VALUES(?,?,?,?,?,?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, tempIdClub);
                        insertStmt.setString(2, tempNamaClub);
                        if (tempDeskripsi.isBlank()) {
                            insertStmt.setNull(3, Types.VARCHAR);
                        } else {
                            insertStmt.setString(3, tempDeskripsi);
                        }
                        insertStmt.setInt(4, 2025); // Tahun bisa kamu buat dinamis kalau mau
                        insertStmt.setString(5, tempIdKategori);
                        insertStmt.setString(6, tempIdPendiriClub);
                        insertStmt.executeUpdate();
                        notificationClub.setText("Club berhasil ditambahkan");
                    }
                } else {
                    notificationClub.setText("Nama club sudah digunakan");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            notificationClub.setText("Data belum lengkap");
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
