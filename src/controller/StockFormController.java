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
import model.Stock;
import model.tableRows.stockWindow.StockRefill;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

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
    public TableView<StockRefill> refillTbl;
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
    private int stockTableDataCount;
    private int loadedRowCountStock = 0;
    private int loadedRowCountStockRefill = 0;
    private ArrayList<Stock> stocks;


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

        try {
            stockTableDataCount = dbConnection.getTableRowCount("stock");

            if(stockTableDataCount < 25 && stockTableDataCount > 0) {
                stocks = dbConnection.getStockTable(stockTableDataCount);
                loadedRowCountStock = stockTableDataCount;
                previewStockTableBtn.setDisable(true);
                nextStockTableBtn.setDisable(true);

            } else {
                stocks = dbConnection.getStockTable(loadedRowCountStock);
                previewStockTableBtn.setDisable(true);
            }

            setDataIntoTable();

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }


    }

    private void setDataIntoTable() throws SQLException {
        ObservableList<model.tableRows.stockWindow.Stock> obList = FXCollections.observableArrayList();
        ArrayList<String> userIdAndNames = new ArrayList<>();

        if(stocks != null) {
            if(!stocks.isEmpty()) {
                userIdAndNames.add(stocks.get(0).getUserId() + "-");

                for (model.Stock ss : stocks) {
                    for (String s : userIdAndNames) {
                        if(ss.getUserId() != Integer.parseInt(s.split("-")[0])){
                            userIdAndNames.add(stocks.get(0).getUserId() + "-");
                        }
                    }
                }

                for (int i=0; i<userIdAndNames.size(); i++) {
                    userIdAndNames.set(i, userIdAndNames.get(i) +
                            dbConnection.getUserName(Integer.parseInt(userIdAndNames.get(i).split("-")[0])));
                }

                for (model.Stock s : stocks) {
                    String userName = null;
                    for (String ss : userIdAndNames) {
                        if(s.getUserId() == Integer.parseInt(ss.split("-")[0])) {
                            userName = ss.split("-")[1];
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
                    setDataIntoTable();
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
                    setDataIntoTable();
                    previewStockTableBtn.setDisable(false);

                    if((loadedRowCountStock + 25) < stockTableDataCount) {
                        nextStockTableBtn.setDisable(false);
                    }
                }
            }
        }
    }

    public void previewRefillTableOnAction(ActionEvent actionEvent) throws SQLException {

    }

    public void nextRefillTableOnAction(ActionEvent actionEvent) {
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
