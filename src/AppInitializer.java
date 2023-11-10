import DB_Connection.DBConnection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.staticType.LogTypes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.SortedMap;


public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);

//        System.out.println(getNewId("B"));
//        System.out.println(getNewId("S"));
    }


    private static String getNewId(String oldId) {
        if(oldId.charAt(0) == 'B') {
            return "B" + generateId(oldId);

        } else if (oldId.charAt(0) == 'S') {
            return "S" + generateId(oldId);

        }

        return null;
    }

    private static String generateId(String oldId) {
        String date = new SimpleDateFormat("yyMMdd").format(new Date());

        if (oldId.length() > 7) {
            return date + String.format("%03d", (Integer.parseInt(oldId.substring(7)) + 1));

        } else {
            return date + "001";
        }
    }






    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load
                (Objects.requireNonNull(getClass().getResource("./view/DashboardForm.fxml")))
        ));
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
