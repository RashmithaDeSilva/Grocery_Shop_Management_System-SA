package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashboardFormController {
    public AnchorPane contextDashboardForm;
    public Label userNameTxt;
    public Label incomeTxt;

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextDashboardForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void sellOnActionBtn(ActionEvent actionEvent) {
    }

    public void itemOnActionBtn(ActionEvent actionEvent) {
    }

    public void stockOnActionBtn(ActionEvent actionEvent) {
    }

    public void incomeOnActionBtn(ActionEvent actionEvent) {
    }
}
