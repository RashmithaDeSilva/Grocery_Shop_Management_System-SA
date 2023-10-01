import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
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
//        primaryStage.getIcons().add(new Image("src/assets/imagers/pos-system.png"));
        primaryStage.show();
    }
}
