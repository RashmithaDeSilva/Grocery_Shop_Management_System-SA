package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Window;

import java.io.IOException;
import java.sql.SQLException;


public class LoginFormController extends Window{
    public TextField userNameTxt;
    public PasswordField passwordTxt;
    public AnchorPane contextLoginForm;

    public void initialize() {
        super.context = contextLoginForm;
    }

    public void loginOnActonBtn(ActionEvent actionEvent) {
        DBConnection dbConnection = DBConnection.getInstance();

        try {
            if (dbConnection.checkUserLogin(userNameTxt.getText(), passwordTxt.getText())) {
                new Window().setUserName(userNameTxt.getText());
                new Window().setUserId(dbConnection.getUserId(userNameTxt.getText()));
                new Window().setUserRoll(dbConnection.getUserRoll(userNameTxt.getText()));
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
}
