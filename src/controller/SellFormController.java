package controller;

import DB_Connection.DBConnection;
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
import model.staticType.SellFillterTypes;

import java.io.IOException;
import java.sql.SQLException;
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
    public TableView itemTbl;
    public TableColumn idCol2;
    public TableColumn nameCol2;
    private String searchText = "";
    private DBConnection dbConnection = DBConnection.getInstance();


    public void initialize() {

        idTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue.toLowerCase();
            try {
                fillOtherInputs(SellFillterTypes.ID);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        nameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue.toLowerCase();
            try {
                fillOtherInputs(SellFillterTypes.NAME);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void fillOtherInputs(SellFillterTypes sellFillterTypes) throws SQLException {
        switch(sellFillterTypes) {
            case ID:
                nameTxt.setText(dbConnection.getItemName(searchText));
                break;
            case NAME:
                System.out.println(searchText);
                break;
        }
    }

    public void addOnAction(ActionEvent actionEvent) {

    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellOnAction(ActionEvent actionEvent) {
    }

    public void printInvoiceOnAction(ActionEvent actionEvent) {
    }
    
    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextSellForm.getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }
}
