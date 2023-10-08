package model;

import DB_Connection.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class Window {
    public AnchorPane context;
    private static int userId = 1;
    private static String userName = "Admin";
    private static int userRoll = 0;
    public DBConnection dbConnection = DBConnection.getInstance();


    protected void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    protected void alert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.initOwner(this.context.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    public int getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(int userRoll) {
        Window.userRoll = userRoll;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        Window.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        Window.userName = userName;
    }
}
