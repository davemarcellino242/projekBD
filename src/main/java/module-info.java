module com.example.p1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jdi;
    requires java.desktop;


    opens clubApp to javafx.fxml;
    exports clubApp;
    exports db;
    opens db to javafx.fxml;
    exports currentUser;
    opens currentUser to javafx.fxml;
    exports clubApp.General;
    opens clubApp.General to javafx.fxml;
}