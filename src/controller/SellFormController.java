package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SellFormController {
    public AnchorPane contextSellForm;
    public TableView quotationTbl;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn quantityCol;
    public TableColumn discountCol;
    public TableColumn priceCol;
    public TableColumn deleteCol;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField discountTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public Label totalLbl;

    public void addOnAction(ActionEvent actionEvent) {
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellOnAction(ActionEvent actionEvent) {
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextSellForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
}
