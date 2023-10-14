package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Window;
import model.staticType.TableTypes;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class DashboardFormController extends Window {
    public AnchorPane contextDashboardForm;
    public Label incomeLbl;
    public Button userNameBtn;


    public void initialize() throws IOException {
        super.context = contextDashboardForm;

        try {
            if (dbConnection.getTableRowCount(TableTypes.USER_TABLE) > super.getMaximumUserCount()) {
                dbConnection.deleteIllegalUsers(super.getMaximumUserCount());
                if(!dbConnection.userIdIsAvailable(super.getUserId())) {
                    System.exit(0);
                }
            }

            userNameBtn.setText(getUserName());
            double income = dbConnection.getIncome(String.format(new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(System.currentTimeMillis()))));
            incomeLbl.setText("Rs: " + (income >= 0 ? income : 0));

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void sellOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellForm");
    }

    public void itemOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 1 || getUserRoll() == 2) {
            setUI("ItemForm");

        } else {
            alert(Alert.AlertType.WARNING, "WARNING", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void stockOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 1 || getUserRoll() == 2) {
                setUI("StockForm");

        } else {
            alert(Alert.AlertType.WARNING, "WARNING", "You Can't Accuses",
                    "Admins accuses only");
        }
    }

    public void lockerOnAction(ActionEvent actionEvent) throws IOException {
        if(getUserRoll() == 1) {
            setUI("LockerForm");

        } else {
            alert(Alert.AlertType.WARNING, "WARNING", "You Can't Accuses",
                    "Super admin can only accuses");
        }
    }

    public void userAccOnAction(ActionEvent actionEvent) throws IOException {
        setUI("UserForm");
    }

    public void logoutOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        setUI("LoginForm");
    }

}
