package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class DashboardFormController {
    public AnchorPane contextDashboardForm;
    public Label userNameTxt;
    public Label incomeTxt;
    private static String userName = "Admin";
    private static int userRoll = 0;
    private final DBConnection dbConnection = DBConnection.getInstance();


    public void initialize() {
        userNameTxt.setText(userName);
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextDashboardForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void sellOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellForm");
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        if(userRoll == 0 || userRoll == 1) {
            setUI("ItemForm");

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void stockOnAction(ActionEvent actionEvent) throws IOException {
        if(userRoll == 0 || userRoll == 1) {
            try {
                new StockFormController().setUserID(dbConnection.getUserId(userName));
                setUI("StockForm");

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void lockerOnAction(ActionEvent actionEvent) throws IOException {
        if(userRoll == 0) {
            setUI("LockerForm");

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Super admin can only accuses");
        }
    }

    public void userAccOnAction(MouseEvent mouseEvent) {
        if(userRoll == 0 || userRoll == 1) {


        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void setUserName(String userName) {
        DashboardFormController.userName = userName;
    }

    public int getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(int userRoll) {
        DashboardFormController.userRoll = userRoll;
    }

    public void logoutOnAction(ActionEvent actionEvent) throws SQLException {
        DBConnection.getInstance().closeConnection();
        System.exit(0);
    }

    private void alert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}
