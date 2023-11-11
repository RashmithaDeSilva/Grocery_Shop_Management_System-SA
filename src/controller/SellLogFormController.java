package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Bill;
import model.Log;
import model.Sell;
import model.Window;
import model.staticType.IncomeOrExpenseLogTypes;
import model.staticType.LogTypes;
import model.staticType.TableTypes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SellLogFormController extends Window {
    public AnchorPane contextSellLogForm;
    public TableView<model.tableRows.sellLogWindow.Bill> billTbl;
    public TableColumn<Object, String> billNumberCol;
    public TableColumn<Object, String> userNameCol;
    public TableColumn<Object, String> discountBillTableCol;
    public TableColumn<Object, String> priceBillTableCol;
    public TableColumn<Object, String> dateCol;
    public TableColumn<Object, String> timeCol;
    public TableColumn<Object, String> returnBillTableCol;
    public TableView<model.tableRows.sellLogWindow.Sell> sellsTbl;
    public TableColumn<Object, String> sellIdCol;
    public TableColumn<Object, String> itemNameCol;
    public TableColumn<Object, String> quantityCol;
    public TableColumn<Object, String> discountSellTableCol;
    public TableColumn<Object, String> priceSellTableCol;
    public TableColumn<Object, String> returnSellTableCol;
    public TextField sellIdTxt;
    public TextField discountTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public TextField ItemNameTxt;
    public ComboBox<String> searchCbBx;
    public TextField searchTxt;
    public Button previewBillTableBtn;
    public Button nextBillTableBtn;
    private int billTableDataCount;
    private ArrayList<Bill> bills;
    private int loadedRowCountBills = 0;
    private String searchText = "";


    public void initialize() {
        super.context = contextSellLogForm;

        billNumberCol.setCellValueFactory(new PropertyValueFactory<>("billNumber"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        discountBillTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceBillTableCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        returnBillTableCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        sellIdCol.setCellValueFactory(new PropertyValueFactory<>("sellId"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        discountSellTableCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceSellTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        returnSellTableCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCbBx.setItems(FXCollections.observableArrayList("All", "Bill Number", "User Name",
                "Discount", "Price", "Date", "Time", "Return", "Not Return"));
        searchCbBx.setValue("All");

        try {
            billTableDataCount = dbConnection.getTableRowCount(TableTypes.BILL_TABLE);

            // Set stock table
            if (billTableDataCount < 25 && billTableDataCount >= 0) {
                nextBillTableBtn.setDisable(true);
            }

            bills = dbConnection.getBillTableDesc(loadedRowCountBills);
            previewBillTableBtn.setDisable(true);

            setDataIntoBillTable();
            if(bills != null && !bills.isEmpty()) {
                setDataIntoSellTable(bills.get(0).getBillNumber());
            }

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        searchCbBx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                searchText = searchTxt.getText();
                setDataIntoBillTable();
            }
        });

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                searchText = newValue;
                setDataIntoBillTable();
            }
        });

        billTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                setDataIntoSellTable(newValue.getBillNumber());
            }
        });

    }

    private void setDataIntoSellTable(String billNumber) {
        try {
            ArrayList<Sell> sells = dbConnection.getSells(billNumber);
            if(sells != null && !sells.isEmpty()) {
                ObservableList<model.tableRows.sellLogWindow.Sell> obList = FXCollections.observableArrayList();

                for (Sell s : sells) {
                    Button btn = new Button("Return");
                    btn.setStyle("-fx-background-color:  #ff6b6b;");
                    btn.setDisable(true);

                    obList.add(new model.tableRows.sellLogWindow.Sell(s.getSellId(),
                            dbConnection.getItemName(s.getItemId()), s.getQuantity(), s.getDiscount(),
                            s.getPrice(), s.isReturns() ? btn : getSellReturnButton(s.getSellId())));
                }
                sellsTbl.setItems(obList);
            }


        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    private void setDataIntoBillTable() {
        if(bills != null && !bills.isEmpty()) {
            ObservableList<model.tableRows.sellLogWindow.Bill> obList = FXCollections.observableArrayList();

            for (Bill b : bills) {
                model.tableRows.sellLogWindow.Bill bill = null;
                String userName = null;

                try {
                    bill = getBillForTable(b);
                    userName = dbConnection.getUserName(b.getUserId());

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                            e.getMessage());
                }

                switch (searchCbBx.getValue()) {
                    case "Bill Number":
                        if(b.getBillNumber().toLowerCase().contains(searchText.toLowerCase())) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "User Name":
                        if(Objects.requireNonNull(userName).toLowerCase().contains(searchText.toLowerCase())) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Discount":
                        if(Double.toString(b.getDiscount()).contains(searchText)) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Price":
                        if(Double.toString(b.getPrice()).contains(searchText)) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Date":
                        if(String.valueOf(b.getDate()).contains(searchText)) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Time":
                        if(String.valueOf(b.getTime()).contains(searchText)) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Return":
                        if(b.getBillNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                                Objects.requireNonNull(userName).toLowerCase().contains(searchText.toLowerCase()) ||
                                Double.toString(b.getDiscount()).contains(searchText) ||
                                Double.toString(b.getPrice()).contains(searchText) ||
                                String.valueOf(b.getDate()).contains(searchText) ||
                                String.valueOf(b.getTime()).contains(searchText)) {
                            if(bill != null && b.isReturns()) {
                                obList.add(bill);
                            }
                        }
                        break;

                    case "Not Return":
                        if(b.getBillNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                                Objects.requireNonNull(userName).toLowerCase().contains(searchText.toLowerCase()) ||
                                Double.toString(b.getDiscount()).contains(searchText) ||
                                Double.toString(b.getPrice()).contains(searchText) ||
                                String.valueOf(b.getDate()).contains(searchText) ||
                                String.valueOf(b.getTime()).contains(searchText)) {
                            if(bill != null && !b.isReturns()) {
                                obList.add(bill);
                            }
                        }
                        break;

                    default:
                        if(b.getBillNumber().toLowerCase().contains(searchText.toLowerCase()) ||
                                Objects.requireNonNull(userName).toLowerCase().contains(searchText.toLowerCase()) ||
                                Double.toString(b.getDiscount()).contains(searchText) ||
                                Double.toString(b.getPrice()).contains(searchText) ||
                                String.valueOf(b.getDate()).contains(searchText) ||
                                String.valueOf(b.getTime()).contains(searchText)) {
                            if(bill != null) {
                                obList.add(bill);
                            }
                        }
                        break;
                }
            }

            billTbl.setItems(obList);
        }
    }

    private model.tableRows.sellLogWindow.Bill getBillForTable(Bill b) throws SQLException {
        Button btn = getBillReturnButton(b.getBillNumber());
        if(b.isReturns()){
            btn.setDisable(true);
        }

        return new model.tableRows.sellLogWindow.Bill(b.getBillNumber(), dbConnection.getUserName(b.getUserId()),
                b.getDiscount(), b.getPrice(), b.getDate(), b.getTime(), btn);
    }

    private Button getBillReturnButton(String billNumber) {
        Button btn = new Button("Return");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform Return");
            alert.setContentText("Are you sure do you want to Return this Bill?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.YES) {
                    try {
                        if(dbConnection.setReturnBill(billNumber)) {
                            bills.forEach(b -> {
                                if(b.getBillNumber().equals(billNumber)) {
                                    try {
                                        dbConnection.addLog(new Log(super.getUserId(),
                                                "Return Bill (Bill number: " + billNumber + ", price: "
                                                        + b.getPrice() + ", discount: " + b.getDiscount() + ")",
                                                LogTypes.INFORMATION, super.getDate(), super.getTime(),
                                                b.getPrice(), IncomeOrExpenseLogTypes.RETURNS));

                                    } catch (SQLException ex) {
                                        alert(Alert.AlertType.ERROR, "ERROR",
                                                "Database Connection Error", ex.getMessage());
                                    }
                                }
                            });

                            if(sellsTbl != null) {
                                for (model.tableRows.sellLogWindow.Sell s : sellsTbl.getItems()) {
                                    s.getBtn().setDisable(true);
                                }
                            }

                            btn.setDisable(true);
                            alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successful Return",
                                    "Return successful Bill number: " + billNumber);

                        } else {
                            alert(Alert.AlertType.WARNING, "WARNING", "Can't Return",
                                    "This Bill can not Return");
                        }

                    } catch (SQLException ex) {
                        alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                ex.getMessage());
                    }
                }
            });
        });

        return btn;
    }

    private Button getSellReturnButton(String sellId) {
        Button btn = new Button("Return");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform Return");
            alert.setContentText("Are you sure do you want to Return this Sell?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.YES) {
                    try {
                        if(dbConnection.setReturnSell(sellId)) {
                            sellsTbl.getItems().forEach(s -> {
                                if(s.getSellId().equals(sellId)) {
                                    try {
                                        dbConnection.addLog(new Log(super.getUserId(),
                                                "Return Sell (Sell ID: " + sellId + ", price: "
                                                        + s.getPrice() + ", discount: " + s.getDiscount() + ")",
                                                LogTypes.INFORMATION, super.getDate(), super.getTime(),
                                                s.getPrice(), IncomeOrExpenseLogTypes.RETURNS));

                                    } catch (SQLException ex) {
                                        alert(Alert.AlertType.ERROR, "ERROR",
                                                "Database Connection Error", ex.getMessage());
                                    }
                                }
                            });

                            btn.setDisable(true);
                            alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successful Return",
                                    "Return successful Sell ID: " + sellId);

                        } else {
                            alert(Alert.AlertType.WARNING, "WARNING", "Can't Return",
                                    "This Sell can not Return");
                        }

                    } catch (SQLException ex) {
                        alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                ex.getMessage());
                    }
                }
            });
        });

        return btn;
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellsOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellForm");
    }

    public void previewBillTableOnAction(ActionEvent actionEvent) {
        try {
            previewBillTableBtn.setDisable(true);
            if(bills != null && !bills.isEmpty()) {
                if((loadedRowCountBills - 25) >= 0) {
                    loadedRowCountBills -= 25;
                    bills = dbConnection.getBillTableDesc(loadedRowCountBills);
                    setDataIntoBillTable();
                    nextBillTableBtn.setDisable(false);

                    if((loadedRowCountBills - 25) >= 0) {
                        previewBillTableBtn.setDisable(false);
                    }
                }
            }
            if(!searchTxt.getText().isEmpty()) {
                String search = searchTxt.getText();
                searchTxt.clear();
                searchTxt.setText(search);
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void nextBillTableOnAction(ActionEvent actionEvent) {
        try {
            nextBillTableBtn.setDisable(true);
            if(bills != null && !bills.isEmpty()) {
                if((loadedRowCountBills + 25) < billTableDataCount) {
                    loadedRowCountBills += 25;
                    bills = dbConnection.getBillTableDesc(loadedRowCountBills);
                    setDataIntoBillTable();
                    previewBillTableBtn.setDisable(false);

                    if((loadedRowCountBills + 25) < billTableDataCount) {
                        nextBillTableBtn.setDisable(false);
                    }
                }
            }
            if(!searchTxt.getText().isEmpty()) {
                String search = searchTxt.getText();
                searchTxt.clear();
                searchTxt.setText(search);
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
    }

    public void updateOnAction(ActionEvent actionEvent) {
    }

    public void refreshOnAction(ActionEvent actionEvent) {
    }
}
