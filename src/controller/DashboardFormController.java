package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        setUI("ItemForm");
    }

    public void stockOnAction(ActionEvent actionEvent) throws IOException {
        setUI("StockForm");
    }

    public void lockerOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LockerForm");
    }

    public void userAccOnAction(MouseEvent mouseEvent) {
    }

    public void setUserName(String userName) {
        DashboardFormController.userName = userName;
    }

    public void logoutOnAction(ActionEvent actionEvent) throws SQLException {
        DBConnection.getInstance().closeConnection();
        System.exit(0);
    }
}
