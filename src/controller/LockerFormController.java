package controller;

import DB_Connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Log;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

public class LockerFormController {
    public AnchorPane contextLocker;
    public TextField addMoneyTxt;
    public TextField forWhatTxt;
    public TextField withdrawTxt;
    public TextField stockValueTxt;
    public TextField lockerMoneyTxt;
    public TextField todayIncomeTxt;
    public TextField lastWeekIncomeTxt;
    public TextField lastMonthIncomeTxt;
    public CheckBox payBillChBx;
    public ComboBox<String> todayCmb;
    public ComboBox<String> lastWeekCmb;
    public ComboBox<String> lastMonthCmb;
    private DBConnection dbConnection = DBConnection.getInstance();
    private static int userId = -1;


    public void initialize() {
        ObservableList<String> comboBoxSelections =
                FXCollections.observableArrayList("All", "Income", "Expenses");

        todayCmb.setItems(comboBoxSelections);
        todayCmb.setValue(comboBoxSelections.get(0));

        lastWeekCmb.setItems(comboBoxSelections);
        lastWeekCmb.setValue(comboBoxSelections.get(0));

        lastMonthCmb.setItems(comboBoxSelections);
        lastMonthCmb.setValue(comboBoxSelections.get(0));

        try {
            stockValueTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getAllStockadeValue()) + " Rs");
            lockerMoneyTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getLockerMoney()) + " Rs");

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void withdrawMoneyOnAction(ActionEvent actionEvent) {
        try {
            double amount = Double.parseDouble(withdrawTxt.getText());

            if(forWhatTxt != null && !forWhatTxt.getText().isEmpty()) {
                if(withdrawTxt != null && !withdrawTxt.getText().isEmpty()) {
                    if(amount > 0) {
                        if( dbConnection.addLog(new Log(userId, "Withdraw money Rs: " + amount,
                                3, new Date(Calendar.getInstance().getTime().getTime()),
                                new Time(Calendar.getInstance().getTime().getTime()), amount,
                                payBillChBx.isSelected() ? 5 : 4))) {

                            forWhatTxt.clear();
                            payBillChBx.setSelected(false);
                            withdrawTxt.clear();
                            alert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Successfully Withdraw",
                                    "Successfully withdraw money in locker");

                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Database Connection Error",
                                    "Try again");
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Invalid Input",
                                "Try again");
                    }

                } else {
                    alert(Alert.AlertType.ERROR, "Error", "Invalid Input",
                            "Try again");
                }

            } else {
                alert(Alert.AlertType.ERROR, "Error", "Enter Input", "Enter withdraw reason");
            }

        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());
        }

    }

    public void lockerLogOnAction(ActionEvent actionEvent) {
    }

    public void addMoneyOnAction(ActionEvent actionEvent) {
        if(addMoneyTxt != null && !addMoneyTxt.getText().isEmpty()) {
            try {
                double amount = Double.parseDouble(addMoneyTxt.getText());

                if(amount > 0) {
                    if( dbConnection.addLog(new Log(userId, "Add money Rs: " + amount,
                            3, new Date(Calendar.getInstance().getTime().getTime()),
                            new Time(Calendar.getInstance().getTime().getTime()), amount, 2))) {

                        addMoneyTxt.clear();
                        alert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Successfully Added",
                                "Successfully added money into locker");

                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Database Connection Error",
                                "Try again");
                    }

                } else {
                    alert(Alert.AlertType.ERROR, "Error", "Invalid Input",
                            "Try again");
                }

            } catch (NumberFormatException e) {
                alert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());
            }
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        LockerFormController.userId = userId;
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextLocker.getScene().getWindow();
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
