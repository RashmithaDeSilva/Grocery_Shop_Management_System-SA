package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Bill;
import model.Log;
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
    public TableColumn<Object, String> deleteBillTableCol;
    public TableView sellsTbl;
    public TableColumn<Object, String> sellIdCol;
    public TableColumn<Object, String> itemNameCol;
    public TableColumn<Object, String> quantityCol;
    public TableColumn<Object, String> discountSellTableCol;
    public TableColumn<Object, String> priceSellTableCol;
    public TableColumn<Object, String> deleteSellTableCol;
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
        deleteBillTableCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        sellIdCol.setCellValueFactory(new PropertyValueFactory<>("sellId"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        discountSellTableCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceSellTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        deleteSellTableCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCbBx.setItems(FXCollections.observableArrayList("All", "Bill Number", "User Name",
                "Discount", "Price", "Date", "Time"));
        searchCbBx.setValue("All");

        try {
            billTableDataCount = dbConnection.getTableRowCount(TableTypes.BILL_TABLE);

            // Set stock table
            if(billTableDataCount < 25 && billTableDataCount > 0) {
                nextBillTableBtn.setDisable(true);
            }
            bills = dbConnection.getBillTable(loadedRowCountBills);
            previewBillTableBtn.setDisable(true);

            setDataIntoBillTable();

        } catch (SQLException e){
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
                        if(Integer.toString(b.getBillNumber()).contains(searchText)) {
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

                    default:
                        if(Integer.toString(b.getBillNumber()).contains(searchText) ||
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
        return new model.tableRows.sellLogWindow.Bill(b.getBillNumber(), dbConnection.getUserName(b.getUserId()),
                b.getDiscount(), b.getPrice(), b.getDate(), b.getTime(), getDeleteButton(b.getBillNumber()));
    }

    private Button getDeleteButton(int billNumber) {
        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Conform Delete");
        alert.setContentText("Are you sure do you want to Delete this Bill?");
        alert.getButtonTypes().set(0, ButtonType.YES);
        alert.getButtonTypes().set(1, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.YES) {
                try {
                    if(dbConnection.deleteBill(billNumber)) {

//                        int userId, String logName, int logType, Date date, Time time,
//                        double amount, int incomeOrExpensesType
                        bills.forEach(b -> {
                            if(b.getBillNumber() == billNumber) {
//                                dbConnection.addLog(new Log(super.getUserId(), "Delete Bill (Bill number: " +
//                                        billNumber + ", price: " + b.getPrice() + ", discount: " +
//                                        b.getDiscount() + ")", LogTypes.INFORMATION, super.getDate(),
//                                        super.getTime(), (b.getPrice() + b.getDiscount()),
//                                        IncomeOrExpenseLogTypes.RETURNS));
                            }
                        });

                        alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successful Delete",
                                "Delete successful bill number: " + billNumber);

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Can't Delete",
                                "This bill can not delete");
                    }

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
                }
            }
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
    }

    public void nextBillTableOnAction(ActionEvent actionEvent) {
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
