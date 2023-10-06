package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Log;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
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
    private DBConnection dbConnection = DBConnection.getInstance();
    private static int userId = -1;


    public void withdrawMoneyOnAction(ActionEvent actionEvent) {
    }

    public void lockerLogOnAction(ActionEvent actionEvent) {
    }

    public void addMoneyOnAction(ActionEvent actionEvent) {
        if(addMoneyTxt != null && !addMoneyTxt.getText().isEmpty()) {
            try {
                double amount = Double.parseDouble(addMoneyTxt.getText());

               if( dbConnection.addMoney(new Log(userId, "Add money Rs: " + addMoneyTxt.getText(), 3,
                       new Date(Calendar.getInstance().getTime().getTime()),
                       new Time(Calendar.getInstance().getTime().getTime()),
                       amount, true)) && amount > 0) {

                   addMoneyTxt.clear();
                   alert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "Successfully Added",
                           "Successfully added money into locker");

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
