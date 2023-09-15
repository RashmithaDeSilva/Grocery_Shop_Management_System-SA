package controller;

import DB_Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class StockFormController {
    public AnchorPane contextStock;
    public TableView stockTbl;
    public TableColumn<Object, String> stockIdCol;
    public TableColumn<Object, String> userNameCol;
    public TableColumn<Object, String> itemIdCol;
    public TableColumn<Object, String> quantityCol;
    public TableColumn<Object, String> refillQuantityCol;
    public TableColumn<Object, String> priceCol;
    public TableColumn<Object, String> sellingPriceCol;
    public TableColumn<Object, String> refillDateCol;
    public TableColumn<Object, String> refillTimeCol;
    public TableColumn<Object, String> deleteCol;
    public TextField searchStockTxt;
    public TableView refillTbl;
    public TableColumn<Object, String> stockId2Col;
    public TableColumn<Object, String> itemId2Col;
    public TableColumn<Object, String> itemNameCol;
    public TableColumn<Object, String> quantity2Col;
    public TableColumn<Object, String> price2Col;
    public TextField searchRefillTxt;
    public TextField itemIDTxt;
    public TextField ItemNameTxt;
    public TextField QuantityTxt;
    public TextField priceTxt;
    public TextField sellingPriceTxt;
    public TextField refillQuantityTxt;
    public CheckBox showAllItemsCheckBx;
    private final DBConnection dbConnection = DBConnection.getInstance();


    public void initialize() {
        stockIdCol.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        refillQuantityCol.setCellValueFactory(new PropertyValueFactory<>("refillQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sellingPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        refillDateCol.setCellValueFactory(new PropertyValueFactory<>("lastRefillDate"));
        refillTimeCol.setCellValueFactory(new PropertyValueFactory<>("lastRefillTime"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void backOnAction(ActionEvent actionEvent) {
    }

    public void refreshStockOnAction(ActionEvent actionEvent) {
    }

    public void refreshRefillOnAction(ActionEvent actionEvent) {
    }

    public void resetOnAction(ActionEvent actionEvent) {
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
    }
}
