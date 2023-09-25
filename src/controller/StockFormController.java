package controller;

import DB_Connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Item;
import model.Stock;
import model.staticType.RefillTableTypes;
import model.staticType.TableTypes;
import model.tableRows.stockWindow.RefillAndItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static model.staticType.RefillTableTypes.*;

public class StockFormController {
    public AnchorPane contextStock;
    public TableView<model.tableRows.stockWindow.Stock> stockTbl;
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
    public TableView<RefillAndItem> refillTbl;
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
    public Button previewStockTableBtn;
    public Button nextStockTableBtn;
    private final DBConnection dbConnection = DBConnection.getInstance();
    public Button previewRefillTableBtn;
    public Button nextRefillTableBtn;
    private int stockTableDataCount;
    private int stockRefillTableDataCount;
    private int itemTableDataCount;
    private int loadedRowCountStock = 0;
    private int loadedRowCountStockRefill = 0;
    private int loadedRowCountItems = 0;
    private ArrayList<Stock> stocks;
    private ArrayList<Stock> refillStocks;
    private ArrayList<Item> items;


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

        stockId2Col.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        itemId2Col.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantity2Col.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price2Col.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            stockTableDataCount = dbConnection.getTableRowCount(TableTypes.StockTable);
            stockRefillTableDataCount = dbConnection.getTableRowCount(TableTypes.StockRefillTable);

            // Set stock table
            if(stockTableDataCount < 25 && stockTableDataCount > 0) {
                nextStockTableBtn.setDisable(true);
            }
            stocks = dbConnection.getStockTable(loadedRowCountStock);
            previewStockTableBtn.setDisable(true);

            // Set refill stock table
            if(stockRefillTableDataCount < 25 && stockRefillTableDataCount > 0) {
                nextRefillTableBtn.setDisable(true);
            }
            refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
            previewRefillTableBtn.setDisable(true);

            setDataIntoStockTable();
            setDataIntoRefillStockTable(RefillStock);

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        showAllItemsCheckBx.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                stockId2Col.setVisible(false);
                quantity2Col.setVisible(false);
                price2Col.setVisible(false);
                loadedRowCountItems = 0;

                try {
                    itemTableDataCount = dbConnection.getTableRowCount(TableTypes.ItemTable);

                    // Set item table
                    if(itemTableDataCount < 25 && itemTableDataCount > 0) {
                        nextRefillTableBtn.setDisable(true);

                    } else {
                        nextRefillTableBtn.setDisable(false);
                    }

                    items = dbConnection.getItemTable(loadedRowCountItems);
                    previewRefillTableBtn.setDisable(true);
                    setDataIntoRefillStockTable(Items);

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }

            } else {
                stockId2Col.setVisible(true);
                quantity2Col.setVisible(true);
                price2Col.setVisible(true);
                loadedRowCountStockRefill = 0;

                try {
                    // Set refill stock table
                    if(stockRefillTableDataCount < 25 && stockRefillTableDataCount > 0) {
                        nextRefillTableBtn.setDisable(true);

                    } else {
                        nextRefillTableBtn.setDisable(false);
                    }

                    refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
                    previewRefillTableBtn.setDisable(true);
                    setDataIntoRefillStockTable(RefillStock);

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
        });


//        .getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (null != newValue) {
//                        setDataIntoInputs(newValue);
//                    }
//                });


    }

    private void setDataIntoStockTable() throws SQLException {
        ObservableList<model.tableRows.stockWindow.Stock> obList = FXCollections.observableArrayList();
        ArrayList<String> userIdAndNames = new ArrayList<>();

        if(stocks != null) {
            if(!stocks.isEmpty()) {
                userIdAndNames.add(stocks.get(0).getUserId() + "-");

                for (Stock ss : stocks) {
                    for (String s : userIdAndNames) {
                        if(ss.getUserId() != Integer.parseInt(s.split("-")[0])){
                            userIdAndNames.add(ss.getUserId() + "-");
                            break;
                        }
                    }
                }

                for (int i=0; i<userIdAndNames.size(); i++) {
                    userIdAndNames.set(i, userIdAndNames.get(i) +
                            dbConnection.getUserName(Integer.parseInt(userIdAndNames.get(i).split("-")[0])));
                }

                for (Stock s : stocks) {
                    String userName = null;
                    for (String ss : userIdAndNames) {
                        if(s.getUserId() == Integer.parseInt(ss.split("-")[0])) {
                            userName = ss.split("-")[1];
                            break;
                        }
                    }

                    Button btn = new Button("Delete");

                    obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                            s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                            s.getLastRefillTime(), btn));
                }

                stockTbl.setItems(obList);
            }
        }
    }

    private void setDataIntoRefillStockTable(RefillTableTypes type) throws SQLException {
        ObservableList<RefillAndItem> obList = FXCollections.observableArrayList();

        if(type == RefillStock) {
            ArrayList<String> itemIdAndNames = new ArrayList<>();

            if(refillStocks != null && !refillStocks.isEmpty()) {
                itemIdAndNames.add(refillStocks.get(0).getItemId() + "-");

                for (Stock ss : refillStocks) {
                    for (String s : itemIdAndNames) {
                        if (ss.getItemId() != Integer.parseInt(s.split("-")[0])) {
                            itemIdAndNames.add(ss.getItemId() + "-");
                            break;
                        }
                    }
                }

                for (int i=0; i<itemIdAndNames.size(); i++) {
                    itemIdAndNames.set(i, itemIdAndNames.get(i) +
                            dbConnection.getItemName(Integer.parseInt(itemIdAndNames.get(i).split("-")[0])));
                }

                for (Stock s : refillStocks) {
                    String itemName = null;
                    for (String ss : itemIdAndNames) {
                        if(s.getItemId() == Integer.parseInt(ss.split("-")[0])) {
                            itemName = ss.split("-")[1];
                            break;
                        }
                    }

                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                            itemName, s.getQuantity(), s.getPrice()));
                }
            }

        } else if(type == Items) {
            if(items != null && !items.isEmpty()){
                for (Item i : items) {
                    obList.add(new model.tableRows.stockWindow.Item(i.getItemId(), i.getItemName()));
                }
            }
        }

        refillTbl.setItems(obList);
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void refreshStockOnAction(ActionEvent actionEvent) {
    }

    public void refreshRefillOnAction(ActionEvent actionEvent) {
    }

    public void resetOnAction(ActionEvent actionEvent) { 
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
    }

    public void previewStockTableOnAction(ActionEvent actionEvent) throws SQLException {
        previewStockTableBtn.setDisable(true);
        if(stocks != null) {
            if(!stocks.isEmpty()) {
                if((loadedRowCountStock - 25) >= 0) {
                    loadedRowCountStock -= 25;
                    stocks = dbConnection.getStockTable(loadedRowCountStock);
                    setDataIntoStockTable();
                    nextStockTableBtn.setDisable(false);

                    if((loadedRowCountStock - 25) >= 0) {
                        previewStockTableBtn.setDisable(false);
                    }
                }
            }
        }
    }

    public void nextStockTableOnAction(ActionEvent actionEvent) throws SQLException {
        nextStockTableBtn.setDisable(true);
        if(stocks != null) {
            if(!stocks.isEmpty()) {
                if((loadedRowCountStock + 25) < stockTableDataCount) {
                    loadedRowCountStock += 25;
                    stocks = dbConnection.getStockTable(loadedRowCountStock);
                    setDataIntoStockTable();
                    previewStockTableBtn.setDisable(false);

                    if((loadedRowCountStock + 25) < stockTableDataCount) {
                        nextStockTableBtn.setDisable(false);
                    }
                }
            }
        }
    }

    public void previewRefillTableOnAction(ActionEvent actionEvent) throws SQLException {
        previewRefillTableBtn.setDisable(true);

        if(stockId2Col.isVisible()) {
            if(refillStocks != null && !refillStocks.isEmpty()) {
                if((loadedRowCountStockRefill - 25) >= 0) {
                    loadedRowCountStockRefill -= 25;
                    refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
                    setDataIntoRefillStockTable(RefillStock);
                    nextRefillTableBtn.setDisable(false);

                    if((loadedRowCountStockRefill - 25) >= 0) {
                        previewRefillTableBtn.setDisable(false);
                    }
                }
            }

        } else {
            if(items != null && !items.isEmpty()) {
                if((loadedRowCountItems - 25) >= 0) {
                    loadedRowCountItems -= 25;
                    items = dbConnection.getItemTable(loadedRowCountItems);
                    setDataIntoRefillStockTable(Items);
                    nextRefillTableBtn.setDisable(false);

                    if((loadedRowCountItems - 25) >= 0) {
                        previewRefillTableBtn.setDisable(false);
                    }
                }
            }
        }

    }

    public void nextRefillTableOnAction(ActionEvent actionEvent) throws SQLException {
        nextRefillTableBtn.setDisable(true);

        if(stockId2Col.isVisible()) {
            if(refillStocks != null && !refillStocks.isEmpty()) {
                if((loadedRowCountStockRefill + 25) < stockRefillTableDataCount) {
                    loadedRowCountStockRefill += 25;
                    refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
                    setDataIntoRefillStockTable(RefillStock);
                    previewRefillTableBtn.setDisable(false);

                    if((loadedRowCountStockRefill + 25) < stockRefillTableDataCount) {
                        nextRefillTableBtn.setDisable(false);
                    }
                }
            }

        } else {
            if(items != null && !items.isEmpty()) {
                if((loadedRowCountItems + 25) < itemTableDataCount) {
                    loadedRowCountItems += 25;
                    items = dbConnection.getItemTable(loadedRowCountItems);
                    setDataIntoRefillStockTable(Items);
                    previewRefillTableBtn.setDisable(false);

                    if((loadedRowCountItems + 25) < itemTableDataCount) {
                        nextRefillTableBtn.setDisable(false);
                    }
                }
            }
        }
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextStock.getScene().getWindow();
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
