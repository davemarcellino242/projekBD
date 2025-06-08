package clubApp;

import db.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BaseController {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void switchScenes(String file, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        Parent root = loader.load();

        BaseController controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle(title);
        stage.setScene(new Scene(root));
        DatabaseConnection.getConnection();
        stage.show();
        stage.centerOnScreen();
    }

}
