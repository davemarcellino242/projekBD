module com.example.p1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jdi;
    requires java.desktop;

    exports db;
    opens db to javafx.fxml;
    exports currentUser;
    opens currentUser to javafx.fxml;
    exports clubApp.General;
    opens clubApp.General to javafx.fxml;
    exports clubApp.Admin;
    opens clubApp.Admin to javafx.fxml;
    opens Model to javafx.base;
}