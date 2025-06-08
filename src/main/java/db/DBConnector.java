package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    //Variabel yang menampung data untuk masuk ke pg admin
    private static final String URL = "jdbc:postgresql://localhost:5432/Project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Davemarcellino";

    //Method untuk menyambungkan java fx ke pg admin
    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
