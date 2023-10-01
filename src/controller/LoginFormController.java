package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginFormController {
    public TextField userNameTxt;
    public PasswordField passwordTxt;
    public AnchorPane contextLoginForm;

    public void loginOnActonBtn(ActionEvent actionEvent) {
        DBConnection dbConnection = DBConnection.getInstance();

        try {
            if (dbConnection.checkUserLogin(userNameTxt.getText(), passwordTxt.getText())) {
                new DashboardFormController().setUserName(userNameTxt.getText());
                new DashboardFormController().setUserRoll(dbConnection.getUserRoll(userNameTxt.getText()));
                setUI("DashboardForm");
                alert(Alert.AlertType.INFORMATION, "INFORMATION", "Login Successful",
                        "Welcome " + userNameTxt.getText());

            } else {
                alert(Alert.AlertType.ERROR, "ERROR", "Can't Logging",
                        "Wrong User or Password");
            }

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());

        }  catch (IOException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Dashboard Loading Error", e.getMessage());
        }
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextLoginForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    private void alert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}
