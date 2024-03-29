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

    public void loginOnActonBtn(ActionEvent actionEvent) throws IOException {
        DBConnection dbConnection = DBConnection.getInstance();

        try {
            if (dbConnection.checkUserLogin(userNameTxt.getText(), passwordTxt.getText())) {
                int roll = dbConnection.getUserRoll(userNameTxt.getText().trim().toLowerCase(),
                        passwordTxt.getText().trim());

                if(roll > 0 && roll < 3) {
                    new Window().setUserName(userNameTxt.getText());
                    new Window().setUserId(dbConnection.getUserId(userNameTxt.getText().trim().toLowerCase()));
                    new Window().setUserRoll(roll);
                    setUI("DashboardForm");

                    alert(Alert.AlertType.INFORMATION, "INFORMATION", "Login Successful",
                            "Welcome " + userNameTxt.getText());

                } else if (roll == 0) {
                    System.out.println("DeveloperForm");

                } else {
                    alert(Alert.AlertType.WARNING, "WARNING", "Can't Accesses",
                            "You are Banned in this system, you can't accesses");
                }


            } else {
                alert(Alert.AlertType.ERROR, "ERROR", "Can't Logging",
                        "Wrong User or Password or you are Banded from system");
            }

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }
}
