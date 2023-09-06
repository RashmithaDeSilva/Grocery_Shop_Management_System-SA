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

    public void loginOnActonBtn(ActionEvent actionEvent) throws SQLException, IOException {
        DBConnection dbConnection = DBConnection.getInstance();

        if (dbConnection.checkUserLogin(userNameTxt.getText(), passwordTxt.getText())) {
            new DashboardFormController().setUserName(userNameTxt.getText());
            setUI("DashboardForm");
            new Alert(Alert.AlertType.INFORMATION, "Welcome " + userNameTxt.getText()).show();

        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong User or Password").show();
        }

    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextLoginForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
}
