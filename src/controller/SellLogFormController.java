package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.*;
import model.staticType.IncomeOrExpenseLogTypes;
import model.staticType.LogTypes;
import model.staticType.TableTypes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
    public ComboBox<String> searchCbBx;
    public TextField searchTxt;
    public Button previewBillTableBtn;
    public Button nextBillTableBtn;
    public TextField itemNameTxt;
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
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        discountSellTableCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceSellTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        returnSellTableCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCbBx.setItems(FXCollections.observableArrayList("All", "Bill Number", "User Name",
                "Discount", "Price", "Date", "Time", "Return", "Not Return"));
        searchCbBx.setValue("All");

        refreshOnAction(new ActionEvent());

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
                clearAllInputs();
                setDataIntoSellTable(newValue.getBillNumber());
            }
        });

        sellsTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.getBtn().isDisable()) {
                setDataIntoInputs(newValue);
            }
        });

        quantityTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty() && sellIdTxt.getText() != null &&
                    !sellIdTxt.getText().isEmpty()) {
                try {
                    Stock stock = dbConnection.getStock(dbConnection.getStockId(sellIdTxt.getText()));
                    Sell sell = dbConnection.getSell(sellIdTxt.getText());
                    int quantity = Integer.parseInt(newValue);

                    if(stock != null) {
                        if(quantity <= (stock.getQuantity() + sell.getQuantity()) && quantity > 0) {
                            priceTxt.setText(String.valueOf(stock.getSellingPrice() * quantity));
                            discountTxt.setText("0");

                        } else {
                            quantityTxt.setText(String.valueOf(sell.getQuantity()));
                            discountTxt.setText(String.valueOf(sell.getDiscount()));
                            alert(Alert.AlertType.WARNING, "WARNING", "Invalid input",
                                    "This quantity is invalid, Enter valid quantity");
                        }

                    } else {
                        if(quantity <= sell.getQuantity()) {
                            priceTxt.setText(String.valueOf(((sell.getPrice() + sell.getDiscount()) /
                                    sell.getQuantity()) * quantity));
                            discountTxt.setText("0");

                        } else {
                            quantityTxt.setText(String.valueOf(sell.getQuantity()));
                            discountTxt.setText(String.valueOf(sell.getDiscount()));
                            alert(Alert.AlertType.WARNING, "WARNING", "Invalid input",
                                    "This quantity is invalid, Enter valid quantity");
                        }
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.WARNING, "WARNING", "Invalid input enter integer value",
                            e.getMessage());

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
        });

        discountTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(sellIdTxt.getText() != null && !sellIdTxt.getText().isEmpty()) {
                try {
                    Stock stock = dbConnection.getStock(dbConnection.getStockId(sellIdTxt.getText()));
                    Sell sell = dbConnection.getSell(sellIdTxt.getText());
                    int quantity = Integer.parseInt(quantityTxt.getText());
                    double discount = Double.parseDouble(new DecimalFormat("#.##").
                            format(Double.parseDouble(newValue != null && !newValue.isEmpty() ? newValue : "0")));

                    if(stock != null) {
                        if(discount >= 0 && stock.getSellingPrice() >= discount) {
                            priceTxt.setText(String.valueOf(new DecimalFormat("#.##").format
                                    ((stock.getSellingPrice() * quantity) - discount)));

                        } else {
                            discountTxt.setText("0.0");
                            alert(Alert.AlertType.WARNING, "WARNING", "Invalid input",
                                    "This discount is invalid, Enter valid discount");
                        }

                    } else {

                        if(discount >= 0 && ((sell.getPrice() + sell.getDiscount()) * quantity) >= discount) {
                            priceTxt.setText(String.valueOf(new DecimalFormat("#.##").format
                                    ((((sell.getPrice() + sell.getDiscount()) / sell.getQuantity()) *
                                            quantity) - discount)));

                        } else {
                            discountTxt.setText("0.0");
                            alert(Alert.AlertType.WARNING, "WARNING", "Invalid input",
                                    "This discount is invalid, Enter valid discount");
                        }
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.WARNING, "WARNING", "Invalid input enter integer value",
                            e.getMessage());

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
        });

    }

    private void setDataIntoInputs(model.tableRows.sellLogWindow.Sell newValue) {
        discountTxt.setText(String.valueOf(newValue.getDiscount()));
        priceTxt.setText(String.valueOf(newValue.getPrice()));
        quantityTxt.setText(String.valueOf(newValue.getQuantity()));
        sellIdTxt.setText(String.valueOf(newValue.getSellId()));
        itemNameTxt.setText(newValue.getItemName());
    }

    private void setDataIntoSellTable(String billNumber) {
        try {
            clearAllInputs();
            ArrayList<Sell> sells = dbConnection.getSells(billNumber);

            if(sells != null && !sells.isEmpty()) {
                ObservableList<model.tableRows.sellLogWindow.Sell> obList = FXCollections.observableArrayList();
                int returnCount = 0;

                for (Sell s : sells) {
                    if(!s.isReturns()) {
                        returnCount += 1;
                    }
                }

                for (Sell s : sells) {
                    Button btn = new Button("Return");
                    btn.setStyle("-fx-background-color:  #ff6b6b;");
                    btn.setDisable(true);

                    if((returnCount == 1 || sells.size() == 1) && !s.isReturns()) {
                        obList.add(new model.tableRows.sellLogWindow.Sell(s.getSellId(),
                                dbConnection.getItemName(s.getItemId()), s.getQuantity(), s.getDiscount(),
                                s.getPrice(), getBillReturnButton(billNumber)));

                    } else {
                        obList.add(new model.tableRows.sellLogWindow.Sell(s.getSellId(),
                                dbConnection.getItemName(s.getItemId()), s.getQuantity(), s.getDiscount(),
                                s.getPrice(), s.isReturns() ? btn : getSellReturnButton(s.getSellId())));
                    }
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
            setDataIntoSellTable(billTbl.getItems().get(0).getBillNumber());
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
                                        setDataIntoSellTable(billNumber);

                                        for (model.tableRows.sellLogWindow.Bill bill: billTbl.getItems()) {
                                            if(bill.getBillNumber().equals(billNumber)) {
                                                bill.getBtn().setDisable(true);
                                                break;
                                            }
                                        }

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
                            for (model.tableRows.sellLogWindow.Sell s : sellsTbl.getItems()) {
                                if(s.getSellId().equals(sellId)) {
                                    try {
                                        btn.setDisable(true);
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
                            }

                            setDataIntoSellTable(dbConnection.getBillNumber(sellId));
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
        refreshOnAction(new ActionEvent());

        try {
            previewBillTableBtn.setDisable(true);
            if(bills != null && !bills.isEmpty()) {
                if((loadedRowCountBills - 25) >= 0) {
                    loadedRowCountBills -= 25;
                    bills = dbConnection.getBillTableDesc(loadedRowCountBills);
                    setDataIntoBillTable();
                    setDataIntoSellTable(bills.get(0).getBillNumber());
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
        refreshOnAction(new ActionEvent());

        try {
            nextBillTableBtn.setDisable(true);
            if(bills != null && !bills.isEmpty()) {
                if((loadedRowCountBills + 25) < billTableDataCount) {
                    loadedRowCountBills += 25;
                    bills = dbConnection.getBillTableDesc(loadedRowCountBills);
                    setDataIntoBillTable();
                    setDataIntoSellTable(bills.get(0).getBillNumber());
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

        sellsTbl.getItems().removeAll();
        try {
            setDataIntoSellTable(dbConnection.getBillNumber(sellIdTxt.getText()));

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        clearAllInputs();
    }

    private void clearAllInputs() {
        discountTxt.clear();
        priceTxt.clear();
        quantityTxt.clear();
        sellIdTxt.clear();
        itemNameTxt.clear();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        if(sellIdTxt != null && !sellIdTxt.getText().isEmpty()) {
            try {
                Stock stock = dbConnection.getStock(dbConnection.getStockId(sellIdTxt.getText()));
                Sell sell = dbConnection.getSell(sellIdTxt.getText());
                Bill oldBill = bills.stream().filter(b -> b.getBillNumber().equals(sell.getBillNumber()))
                        .findFirst().orElse(dbConnection.getBill(sell.getBillNumber()));
                int quantity = Integer.parseInt(quantityTxt.getText());
                double price = Double.parseDouble(new DecimalFormat("#.##").format
                        (Double.parseDouble(priceTxt.getText())));
                double discount = Double.parseDouble(new DecimalFormat("#.##").format
                        (Double.parseDouble(discountTxt.getText())));
                boolean doTheChangers = false;

                if(stock != null) {
                    if(sell.getQuantity() != quantity || sell.getDiscount() != discount) {
                        if (stock.getItemId() == sell.getItemId() &&
                                stock.getPrice() == (Math.abs(sell.getPrice() - sell.getProfit()) /
                                        sell.getQuantity()) &&
                                stock.getSellingPrice() == ((sell.getPrice() + sell.getDiscount()) /
                                        sell.getQuantity())) {

                            if (sell.getQuantity() < quantity &&
                                    (sell.getQuantity() + stock.getQuantity()) >= quantity) {
                                doTheChangers = true;

                            } else if (sell.getQuantity() > quantity) {
                                doTheChangers = true;

                            } else if (discount <= (quantity * stock.getSellingPrice()) && discount >= 0) {
                                doTheChangers = true;
                            }

                            if (doTheChangers) {
                                dbConnection.updateStock(new Sell(sell.getSellId(), sell.getBillNumber(),
                                        sell.getItemId(), sell.getStockId(), discount, price,
                                        ((stock.getSellingPrice() - stock.getPrice()) * quantity) - discount,
                                        quantity, true, false),
                                        (stock.getQuantity() + sell.getQuantity()) - quantity);

                                dbConnection.updateSell(new Sell(sell.getSellId(), sell.getBillNumber(),
                                        sell.getItemId(), sell.getStockId(), discount, price,
                                        ((stock.getSellingPrice() - stock.getPrice()) * quantity) - discount,
                                        quantity, true, false));

                                dbConnection.addSellEdits(new SellEdits(super.getUserId(), sell.getSellId(),
                                        super.getDate(), super.getTime(), sell.getPrice(), price,
                                        sell.getDiscount(), discount, sell.getQuantity(), quantity));

                                dbConnection.updateBill(new Bill(oldBill.getBillNumber(), oldBill.getUserId(),
                                        price, discount, oldBill.getDate(), oldBill.getTime(), oldBill.isReturns()));

                                dbConnection.addLog(new Log(super.getUserId(),
                                        "Change sell and update oldBill (sell id: " + sell.getSellId() +
                                                " and oldBill number: " + sell.getBillNumber() + ")",
                                        LogTypes.INFORMATION, super.getDate(), super.getTime(),
                                        price - oldBill.getPrice(),
                                        IncomeOrExpenseLogTypes.SELL_INCOME));

                                oldBill.setPrice(price);
                                oldBill.setDiscount(discount);
                            }
                        }
                    }

                } else {
                    if(sell.getQuantity() != quantity || sell.getDiscount() != discount) {
                        if (sell.getQuantity() >= quantity) {
                            doTheChangers = true;

                        } else if (discount <= (quantity * (sell.getDiscount() + sell.getPrice())) && discount >= 0) {
                            doTheChangers = true;
                        }

                        if (doTheChangers) {
                            dbConnection.updateStock(new Sell(sell.getSellId(), sell.getBillNumber(),
                                    sell.getItemId(), sell.getStockId(), discount, price,
                                    ((discount + price) * quantity) - ((((sell.getDiscount() +
                                            sell.getPrice()) - sell.getProfit()) / sell.getQuantity()) * quantity),
                                    quantity, true, false), sell.getQuantity() - quantity);

                            dbConnection.updateSell(new Sell(sell.getSellId(), sell.getBillNumber(),
                                    sell.getItemId(), sell.getStockId(), discount, price,
                                    ((discount + price) * quantity) - ((((sell.getDiscount() +
                                            sell.getPrice()) - sell.getProfit()) / sell.getQuantity()) * quantity),
                                    quantity, true, false));

                            dbConnection.addSellEdits(new SellEdits(super.getUserId(), sell.getSellId(),
                                    super.getDate(), super.getTime(), sell.getPrice(), price,
                                    sell.getDiscount(), discount, sell.getQuantity(), quantity));

                            dbConnection.updateBill(new Bill(oldBill.getBillNumber(), oldBill.getUserId(),
                                    price, discount, oldBill.getDate(), oldBill.getTime(), oldBill.isReturns()));

                            dbConnection.addLog(new Log(super.getUserId(),
                                    "Change sell and update oldBill (sell id: " + sell.getSellId() +
                                            " and oldBill number: " + sell.getBillNumber() + ")",
                                    LogTypes.INFORMATION, super.getDate(), super.getTime(),
                                    price - oldBill.getPrice(),
                                    IncomeOrExpenseLogTypes.SELL_INCOME));

                            oldBill.setPrice(price);
                            oldBill.setDiscount(discount);
                        }
                    }
                }


            } catch (NumberFormatException e) {
                alert(Alert.AlertType.WARNING, "WARNING", "Invalid input enter integer value",
                        e.getMessage());

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());

            } catch (Exception e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Invalid input enter integer value",
                        e.getMessage());

            } finally {
                resetOnAction(new ActionEvent());
            }
        }
    }

    public void refreshOnAction(ActionEvent actionEvent) {
        loadedRowCountBills = 0;
        searchCbBx.setValue("All");
        searchText = "";
        searchTxt.clear();

        try {
            billTableDataCount = dbConnection.getTableRowCount(TableTypes.BILL_TABLE);

            // Set stock table
            if (billTableDataCount < 25 && billTableDataCount >= 0) {
                nextBillTableBtn.setDisable(true);
            }

            bills = dbConnection.getBillTableDesc(loadedRowCountBills);
            previewBillTableBtn.setDisable(true);

            setDataIntoBillTable();

        } catch (SQLException e){
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }
}
