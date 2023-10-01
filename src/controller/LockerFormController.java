package controller;

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

import java.io.IOException;
import java.util.Objects;

public class LockerFormController {
    public AnchorPane contextLocker;
    public TextField addMoneyTxt;
    public TextField forWhatTxt;
    public Button addMoneyOnAction;
    public TextField withdrawTxt;
    public TextField stockValueTxt;
    public TextField lockerMoneyTxt;
    public TextField todayIncomeTxt;
    public TextField lastWeekIncomeTxt;
    public TextField lastMonthIncomeTxt;
    public CheckBox payBillChBx;


    public void withdrawMoneyOnAction(ActionEvent actionEvent) {
    }

    public void lockerLogOnAction(ActionEvent actionEvent) {
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
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
