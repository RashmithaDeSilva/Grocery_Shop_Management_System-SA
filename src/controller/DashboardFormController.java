package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Window;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class DashboardFormController extends Window {
    public AnchorPane contextDashboardForm;
    public Label userNameTxt;
    public Label incomeTxt;


    public void initialize() throws SQLException {
        super.context = contextDashboardForm;
        userNameTxt.setText(getUserName());
        double income = dbConnection.getIncome(String.format(new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date(System.currentTimeMillis()))));
        incomeTxt.setText(String.valueOf(income >= 0 ? income : 0));
    }

    public void sellOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellForm");
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 0 || getUserRoll() == 1) {
            setUI("ItemForm");

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void stockOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 0 || getUserRoll() == 1) {
                setUI("StockForm");

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void lockerOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 0) {
                setUI("LockerForm");

        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Super admin can only accuses");
        }
    }

    public void userAccOnAction(MouseEvent mouseEvent) {
        if(getUserRoll() == 0 || getUserRoll() == 1) {


        } else {
            alert(Alert.AlertType.ERROR, "ERROR", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void logoutOnAction(ActionEvent actionEvent) throws SQLException {
        DBConnection.getInstance().closeConnection();
        System.exit(0);
    }

}
