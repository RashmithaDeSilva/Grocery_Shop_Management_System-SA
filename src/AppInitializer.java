import DB_Connection.DBConnection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load
                (Objects.requireNonNull(getClass().getResource("./view/DashboardForm.fxml")))
        ));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.setTitle("POS System");
        primaryStage.setOnCloseRequest(event -> {
            try {
                DBConnection.getInstance().closeConnection();

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });
//        primaryStage.getIcons().add(new Image("src/assets/imagers/pos-system.png"));
        primaryStage.show();
    }

    private void alert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}
