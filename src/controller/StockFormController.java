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
import model.User;
import model.staticType.RefillTableTypes;
import model.staticType.TableTypes;
import model.tableRows.stockWindow.RefillAndItem;
import model.tableRows.stockWindow.StockRefill;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public TextField itemNameTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public TextField sellingPriceTxt;
    public TextField refillQuantityTxt;
    public CheckBox showAllItemsCheckBx;
    public Button previewStockTableBtn;
    public Button nextStockTableBtn;
    public Button previewRefillTableBtn;
    public Button nextRefillTableBtn;
    public ComboBox<String> searchStockCbBx;
    public ComboBox<String> searchRefillCbBx;
    public Button addOrUpdateBtn;
    private final DBConnection dbConnection = DBConnection.getInstance();
    private int stockTableDataCount;
    private int stockRefillTableDataCount;
    private int itemTableDataCount;
    private int loadedRowCountStock = 0;
    private int loadedRowCountStockRefill = 0;
    private int loadedRowCountItems = 0;
    private ArrayList<Stock> stocks;
    private ArrayList<Stock> refillStocks;
    private ArrayList<Item> items;
    private static int userID;
    private int selectedStockId;


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

        searchStockCbBx.setItems(FXCollections.observableArrayList("All", "Stock ID", "User Name", "Item ID",
                "Quantity", "Refill Quantity", "Price", "Selling Price", "Refill Date", "Refill Time"));
        searchStockCbBx.setValue("All");

        searchRefillCbBx.setItems(FXCollections.observableArrayList("All", "Stock ID", "Item ID", "Item Name",
                "Quantity", "Price"));
        searchRefillCbBx.setValue("All");

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

        searchStockCbBx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(searchStockCbBx.getValue() != null) {
                String search = searchStockTxt.getText();
                searchStockTxt.clear();
                searchStockTxt.setText(search);
            }
        });

        // Search stock
        searchStockTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && stocks != null && !stocks.isEmpty()) {
                ObservableList<model.tableRows.stockWindow.Stock> obList = FXCollections.observableArrayList();

                List<Integer> uniqueUserIds = stocks.stream().map(Stock::getUserId).
                        distinct().collect(Collectors.toList());
                ArrayList<User> users = new ArrayList<>();
                uniqueUserIds.forEach(userId -> users.add(new User(userId)));


                for (User u : users) {
                    try {
                        u.setUserName(dbConnection.getUserName(u.getUserId()));
                    } catch (SQLException e) {
                        alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                    }
                }

                for (Stock s : stocks) {
                    String userName = null;

                    for (User u : users) {
                        if(s.getUserId() == u.getUserId()) {
                            userName = u.getUserName();
                        }
                    }

                    switch (searchStockCbBx.getValue()) {
                        case "Stock ID":
                            if(Integer.toString(s.getStockId()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "User Name":
                            if(Objects.requireNonNull(userName).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Item ID":
                            if(Integer.toString(s.getItemId()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Quantity":
                            if(Double.toString(s.getQuantity()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Refill Quantity":
                            if(Double.toString(s.getRefillQuantity()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Price":
                            if(Double.toString(s.getPrice()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Selling Price":
                            if(Double.toString(s.getSellingPrice()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Refill Date":
                            if(String.valueOf(s.getLastRefillDate()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        case "Refill Time":
                            if(String.valueOf(s.getLastRefillTime()).contains(newValue)) {
                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;

                        default:
                            if(Integer.toString(s.getStockId()).contains(newValue) ||
                                    Objects.requireNonNull(userName).contains(newValue) ||
                                    Integer.toString(s.getItemId()).contains(newValue) ||
                                    Double.toString(s.getQuantity()).contains(newValue) ||
                                    Double.toString(s.getRefillQuantity()).contains(newValue) ||
                                    Double.toString(s.getPrice()).contains(newValue) ||
                                    Double.toString(s.getSellingPrice()).contains(newValue) ||
                                    String.valueOf(s.getLastRefillDate()).contains(newValue) ||
                                    String.valueOf(s.getLastRefillTime()).contains(newValue)) {

                                obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                                        s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                                        s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                            }
                            break;
                    }
                }
                stockTbl.setItems(obList);
            }
        });

        searchRefillCbBx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(searchRefillCbBx.getValue() != null) {
                String search = searchRefillTxt.getText();
                searchRefillTxt.clear();
                searchRefillTxt.setText(search);
            }
        });

        // Search refill stock and item
        searchRefillTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && refillStocks != null && !refillStocks.isEmpty()) {
                ObservableList<RefillAndItem> obList = FXCollections.observableArrayList();

                if(!showAllItemsCheckBx.isSelected()) {
                    List<Integer> uniqueItemIds = refillStocks.stream().map(Stock::getItemId).
                            distinct().collect(Collectors.toList());
                    ArrayList<Item> itemsTemp = new ArrayList<>();
                    uniqueItemIds.forEach(itemID ->itemsTemp.add(new Item(itemID)));


                    for (Item i : itemsTemp) {
                        try {
                            i.setItemName(dbConnection.getItemName(i.getItemId()));
                        } catch (SQLException e) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                        }
                    }

                    for (Stock s : refillStocks) {
                        String itemName = null;

                        for (Item i : itemsTemp) {
                            if(s.getItemId() == i.getItemId()) {
                                itemName = i.getItemName();
                            }
                        }

                        switch (searchRefillCbBx.getValue()) {
                            case "Stock ID" :
                                if(Integer.toString(s.getStockId()).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;

                            case "Item ID" :
                                if(Integer.toString(s.getItemId()).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;

                            case "Item Name" :
                                if(Objects.requireNonNull(itemName).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;

                            case "Quantity" :
                                if(Double.toString(s.getQuantity()).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;

                            case "Price" :
                                if(Double.toString(s.getPrice()).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;

                            default:
                                if(Integer.toString(s.getStockId()).contains(newValue) ||
                                        Integer.toString(s.getItemId()).contains(newValue) ||
                                        Objects.requireNonNull(itemName).contains(newValue) ||
                                        Double.toString(s.getQuantity()).contains(newValue) ||
                                        Double.toString(s.getPrice()).contains(newValue)) {

                                    obList.add(new model.tableRows.stockWindow.StockRefill(s.getStockId(), s.getItemId(),
                                            itemName, s.getQuantity(), s.getPrice()));
                                }
                                break;
                        }
                    }

                } else {
                    for (Item i : items) {
                        switch (searchRefillCbBx.getValue()) {
                            case "Item ID" :
                                if(Integer.toString(i.getItemId()).contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.Item(i.getItemId(), i.getItemName()));
                                }
                                break;

                            case "Item Name" :
                                if(i.getItemName().contains(newValue)) {
                                    obList.add(new model.tableRows.stockWindow.Item(i.getItemId(), i.getItemName()));
                                }
                                break;

                            default:
                                if(Integer.toString(i.getItemId()).contains(newValue) ||
                                        i.getItemName().contains(newValue)) {

                                    obList.add(new model.tableRows.stockWindow.Item(i.getItemId(), i.getItemName()));
                                }
                                break;
                        }
                    }
                }

                refillTbl.setItems(obList);
            }
        });

        showAllItemsCheckBx.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue) {
                    stockId2Col.setVisible(false);
                    quantity2Col.setVisible(false);
                    price2Col.setVisible(false);
                    loadedRowCountItems = 0;

                    // Set item table
                    itemTableDataCount = dbConnection.getTableRowCount(TableTypes.ItemTable);
                    nextRefillTableBtn.setDisable(itemTableDataCount < 25 && itemTableDataCount > 0);
                    items = dbConnection.getItemTable(loadedRowCountItems);
                    previewRefillTableBtn.setDisable(true);
                    setDataIntoRefillStockTable(Items);

                    // Combo box
                    searchRefillCbBx.setItems(FXCollections.observableArrayList("All", "Item ID", "Item Name"));
                    searchRefillCbBx.setValue(searchRefillCbBx.getValue() == null ? "All" : searchRefillCbBx.getValue());

                } else {
                    stockId2Col.setVisible(true);
                    quantity2Col.setVisible(true);
                    price2Col.setVisible(true);
                    loadedRowCountStockRefill = 0;

                    // Set refill stock table
                    nextRefillTableBtn.setDisable(stockRefillTableDataCount < 25 && stockRefillTableDataCount > 0);
                    refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
                    previewRefillTableBtn.setDisable(true);
                    setDataIntoRefillStockTable(RefillStock);

                    // Combo box
                    searchRefillCbBx.setItems(FXCollections.observableArrayList("All", "Stock ID", "Item ID",
                            "Item Name", "Quantity", "Price"));
                    searchRefillCbBx.setValue(searchRefillCbBx.getValue());
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        stockTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                try {
                    setRowDataIntoInputs(newValue);
                    selectedStockId = newValue.getStockId();
                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
        });

        refillTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                try {
                    setRowDataIntoInputs(newValue);
                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
        });
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

                    obList.add(new model.tableRows.stockWindow.Stock(s.getStockId(), userName, s.getItemId(), s.getQuantity(),
                            s.getRefillQuantity(), s.getPrice(), s.getSellingPrice(), s.getLastRefillDate(),
                            s.getLastRefillTime(), getDeleteButton(s.getStockId())));
                }

                stockTbl.setItems(obList);
            }
        }
    }

    private Button getDeleteButton(int stockId) {
        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform DELETE");
            alert.setContentText("Are you sure do you want to DELETE this STOCK?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if(dbConnection.deleteStock(stockId)) {
                        try {

                            for (Stock s : stocks) {
                                if(s.getStockId() == stockId) {
                                    stocks.remove(s);
                                    break;
                                }
                            }
                            setDataIntoStockTable();

                            for (Stock s : refillStocks) {
                                if(s.getStockId() == stockId) {
                                    stocks.remove(s);
                                    break;
                                }
                            }

                            if(showAllItemsCheckBx.isSelected()) {
                                setDataIntoRefillStockTable(Items);

                            } else {
                                setDataIntoRefillStockTable(RefillStock);
                            }

                            alert(Alert.AlertType.INFORMATION, "INFORMATION", "Delete Successful",
                                    "Delete successfully stock id = " + stockId);

                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Data Reload Error",
                                    "Cant reload data in table");
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                "Cant delete data in database throw some error");
                    }
                }
            });
        });

        return btn;
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

    private void setRowDataIntoInputs(model.tableRows.stockWindow.Stock newValue) throws SQLException {
        clearInputs();
        addOrUpdateBtn.setText("Update");
        addOrUpdateBtn.setStyle("-fx-background-color: #feca57;");
        itemIDTxt.setText(String.valueOf(newValue.getItemId()));
        itemNameTxt.setText(dbConnection.getItemName(newValue.getItemId()));
        quantityTxt.setText(String.valueOf(newValue.getQuantity()));
        refillQuantityTxt.setText(String.valueOf(newValue.getRefillQuantity()));
        priceTxt.setText(String.valueOf(newValue.getPrice()));
        sellingPriceTxt.setText(String.valueOf(newValue.getSellingPrice()));
    }

    private void setRowDataIntoInputs(model.tableRows.stockWindow.RefillAndItem newValue) throws SQLException {
        clearInputs();

        if(newValue.toString().split("\\.")[3].split("@")[0].equals("StockRefill")) {
            addOrUpdateBtn.setText("Update");
            addOrUpdateBtn.setStyle("-fx-background-color: #feca57;");

            StockRefill stockRefill = (model.tableRows.stockWindow.StockRefill) newValue;
            selectedStockId = stockRefill.getStockId();

            for (Stock s : refillStocks) {
                if(stockRefill.getStockId() == s.getStockId()) {
                    s = dbConnection.getRefillQuantityAndSellingPrice(s);
                    itemIDTxt.setText(String.valueOf(s.getItemId()));
                    itemNameTxt.setText(dbConnection.getItemName(s.getItemId()));
                    quantityTxt.setText(String.valueOf(s.getQuantity()));
                    refillQuantityTxt.setText(String.valueOf(s.getRefillQuantity()));
                    priceTxt.setText(String.valueOf(s.getPrice()));
                    sellingPriceTxt.setText(String.valueOf(s.getSellingPrice()));
                }
            }

        } else if (newValue.toString().split("\\.")[3].split("@")[0].equals("Item")) {
            model.tableRows.stockWindow.Item item = (model.tableRows.stockWindow.Item) newValue;

            for (Item i : items) {
                if(item.getItemId() == i.getItemId()) {
                    itemIDTxt.setText(String.valueOf(i.getItemId()));
                    itemNameTxt.setText(i.getItemName());
                }
            }
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void refreshStockOnAction(ActionEvent actionEvent) {
        searchStockCbBx.setValue("All");
        searchStockTxt.clear();
        loadedRowCountStock = 0;

        try {
            // Set stock table
            stockTableDataCount = dbConnection.getTableRowCount(TableTypes.StockTable);
            nextStockTableBtn.setDisable(stockTableDataCount < 25 && stockTableDataCount > 0);
            stocks = dbConnection.getStockTable(loadedRowCountStock);
            previewStockTableBtn.setDisable(true);
            setDataIntoStockTable();

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void refreshRefillOnAction(ActionEvent actionEvent) {
        searchRefillTxt.clear();

        try {
            if(!stockId2Col.isVisible()) {
                // Set item table
                loadedRowCountItems = 0;
                itemTableDataCount = dbConnection.getTableRowCount(TableTypes.ItemTable);
                nextRefillTableBtn.setDisable(itemTableDataCount < 25 && itemTableDataCount > 0);
                items = dbConnection.getItemTable(loadedRowCountItems);
                previewRefillTableBtn.setDisable(true);
                setDataIntoRefillStockTable(Items);

            } else {
                // Set refill stock table
                loadedRowCountStockRefill = 0;
                stockRefillTableDataCount = dbConnection.getTableRowCount(TableTypes.StockRefillTable);
                nextRefillTableBtn.setDisable(stockRefillTableDataCount < 25 && stockRefillTableDataCount > 0);
                refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
                previewRefillTableBtn.setDisable(true);
                setDataIntoRefillStockTable(RefillStock);
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        clearInputs();
    }

    private void clearInputs() {
        itemIDTxt.clear();
        itemNameTxt.clear();
        quantityTxt.clear();
        refillQuantityTxt.clear();
        priceTxt.clear();
        sellingPriceTxt.clear();
        addOrUpdateBtn.setText("Add");
        addOrUpdateBtn.setStyle("-fx-background-color:  #1dd1a1;");
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
        int itemId;
        int quantity;
        int refillQuantity;
        double price;
        double sellingPrice;

        try {
            if(itemIDTxt != null && !itemIDTxt.getText().isEmpty() &&
                    itemNameTxt != null && !itemNameTxt.getText().isEmpty() &&
                    quantityTxt != null && !quantityTxt.getText().isEmpty() &&
                    refillQuantityTxt != null && !refillQuantityTxt.getText().isEmpty() &&
                    priceTxt != null && !priceTxt.getText().isEmpty() &&
                    sellingPriceTxt != null && !sellingPriceTxt.getText().isEmpty()) {

                itemId = Integer.parseInt(itemIDTxt.getText());
                quantity = Integer.parseInt(quantityTxt.getText());
                refillQuantity = Integer.parseInt(refillQuantityTxt.getText());
                price = Double.parseDouble(priceTxt.getText());
                sellingPrice = Double.parseDouble(sellingPriceTxt.getText());

                if(quantity >= 0 && refillQuantity >= 0 && price >= 0 && sellingPrice >= 0){
                    if(quantity >= refillQuantity) {
                        if(price <= sellingPrice) {

                            if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {
                                if(dbConnection.checkItemAndPrice(itemId, price)) {
                                    if (dbConnection.addStock(new Stock(0, userID, itemId, quantity,
                                            refillQuantity, price, sellingPrice,
                                            new Date(Calendar.getInstance().getTime().getTime()),
                                            new Time(Calendar.getInstance().getTime().getTime())))) {

                                        reloadTables();
                                        alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                                "Successfully Added Stock",
                                                "Successfully Added stock into database");

                                    } else {
                                        throw new SQLException("Cant save data in database throw some error");
                                    }

                                } else {
                                    throw new NumberFormatException("Already exist this item stock in this price");
                                }

                            } else if (addOrUpdateBtn.getText().equalsIgnoreCase("update")) {
                                if(dbConnection.checkItemAndPrice(itemId, price)) {
                                    if(dbConnection.updateStock(new Stock(selectedStockId, userID, itemId,
                                            quantity, refillQuantity, price, sellingPrice,
                                            new Date(Calendar.getInstance().getTime().getTime()),
                                            new Time(Calendar.getInstance().getTime().getTime())))) {

                                        reloadTables();
                                        alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                                "Successfully Update Stock",
                                                "Successfully Update stock into database");

                                    } else {
                                        throw new SQLException("Cant save data in database throw some error");
                                    }

                                } else {
                                    if(dbConnection.getStockId(itemId, price) == selectedStockId) {
                                        if(dbConnection.updateStock(new Stock(selectedStockId, userID, itemId,
                                                quantity, refillQuantity, price, sellingPrice,
                                                new Date(Calendar.getInstance().getTime().getTime()),
                                                new Time(Calendar.getInstance().getTime().getTime())))) {

                                            reloadTables();
                                            alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                                    "Successfully Update Stock",
                                                    "Successfully Update stock into database");

                                        } else {
                                            throw new SQLException("Cant save data in database throw some error");
                                        }

                                    }else {
                                        throw new NumberFormatException("Already exist this item stock in this price");
                                    }
                                }
                            }

                        } else {
                            throw new NumberFormatException("Price greater than selling price");
                        }

                    } else {
                        throw new NumberFormatException("Refill quantity greater than quantity");
                    }

                } else {
                    throw new NumberFormatException("Dont enter minus values");
                }

            } else {
                throw new NumberFormatException("You didn't enter input,\nEnter input and try again");
            }

        } catch (NumberFormatException e) {
            alert(Alert.AlertType.WARNING, "WARNING", "Enter Inputs Correctly", e.getMessage());

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());

        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Method Error", e.getMessage());
        }
    }

    private void reloadTables() throws SQLException {
        clearInputs();
        stocks = dbConnection.getStockTable(loadedRowCountStock);
        refillStocks = dbConnection.getRefillStockTable(loadedRowCountStockRefill);
        setDataIntoStockTable();
        if(showAllItemsCheckBx.isSelected()) {
            setDataIntoRefillStockTable(Items);
        } else {
            setDataIntoRefillStockTable(RefillStock);
        }
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
        if(!searchStockTxt.getText().isEmpty()) {
            String search = searchStockTxt.getText();
            searchStockTxt.clear();
            searchStockTxt.setText(search);
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
        if(!searchStockTxt.getText().isEmpty()) {
            String search = searchStockTxt.getText();
            searchStockTxt.clear();
            searchStockTxt.setText(search);
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        StockFormController.userID = userID;
    }
}
