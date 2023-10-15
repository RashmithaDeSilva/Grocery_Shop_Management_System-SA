package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Window;

import java.io.IOException;

public class SellLogFormController extends Window {
    public AnchorPane contextSellLogForm;
    public TableColumn billNumberCol;
    public TableColumn userNameCol;
    public TableColumn discountBillTableCol;
    public TableColumn priceBillTableCol;
    public TableColumn dateCol;
    public TableColumn timeCol;
    public TableColumn deleteBillTableCol;
    public TableView sellsTbl;
    public TableColumn sellIdCol;
    public TableColumn itemNameCol;
    public TableColumn quantityCol;
    public TableColumn discountSellTableCol;
    public TableColumn priceSellTableCol;
    public TableColumn deleteSellTableCol;
    public TextField sellIdTxt;
    public TextField discountTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public TextField ItemNameTxt;
    public ComboBox searchCbBx;
    public TextField searchTxt;


    public void initialize() {
        super.context = contextSellLogForm;
    }
    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellForm");
    }

    public void billTbl(SortEvent<TableView> tableViewSortEvent) {
    }

    public void previewBillTableOnAction(ActionEvent actionEvent) {
    }

    public void nextBillTableOnAction(ActionEvent actionEvent) {
    }

    public void previewSellTableOnAction(ActionEvent actionEvent) {
    }

    public void nextSellTableOnAction(ActionEvent actionEvent) {
    }

    public void resetOnAction(ActionEvent actionEvent) {
    }

    public void updateOnAction(ActionEvent actionEvent) {
    }

    public void editBillOnAction(ActionEvent actionEvent) {
    }

    public void refreshOnAction(ActionEvent actionEvent) {
    }
}
