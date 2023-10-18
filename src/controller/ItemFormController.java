package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Item;
import model.User;
import model.Window;
import model.staticType.TableTypes;

import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ItemFormController extends Window{
    public AnchorPane contextItemForm;
    public TextField searchTxt;
    public TableView<model.tableRows.itemWindow.Item> itemTbl;
    public TableColumn<Object, String> idCol;
    public TableColumn<Object, String> nameCol;
    public TableColumn<Object, String> stopSellingCol;
    public TableColumn<Object, String> userNameCol;
    public TableColumn<Object, String> dateCol;
    public TableColumn<Object, String> timeCol;
    public TextField nameTxt;
    public TextField idTxt;
    public Button previewItemTableBtn;
    public Button nextItemTableBtn;
    public ComboBox<String> searchItemsCbBx;
    public Button addOrUpdateBtn;
    private ArrayList<model.Item> items;
    private int loadedRowCountItems = 0;
    private int itemsTableDataCount;
    private String search = "";

    public void initialize() {
        super.context = contextItemForm;

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        stopSellingCol.setCellValueFactory(new PropertyValueFactory<>("sellStopBtn"));

        searchItemsCbBx.setItems(FXCollections.observableArrayList("All", "Item ID", "Item Name",
                "User Name", "Set or Reset Date", "Set or Reset Time", "Selling", "Selling Stop"));
        searchItemsCbBx.setValue("All");

        try{
            itemsTableDataCount = dbConnection.getTableRowCount(TableTypes.ITEM_TABLE);

            // Set item table
            if(itemsTableDataCount < 25 && itemsTableDataCount > 0) {
                nextItemTableBtn.setDisable(true);
            }
            items = dbConnection.getItemTable(loadedRowCountItems);
            previewItemTableBtn.setDisable(true);

            setDataIntoItemTable();

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        idTxt.setText((items != null && !items.isEmpty()) ?
                String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");

        searchItemsCbBx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(searchItemsCbBx.getValue() != null) {
                searchTxt.setText(search);
                setDataIntoItemTable();
            }
        });

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                search = newValue;
                setDataIntoItemTable();
            }
        });

        itemTbl.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setDataIntoInputs(newValue);
                }
        });
    }

    private void setDataIntoItemTable() {
        try {
            if(items != null && !items.isEmpty()) {
                ObservableList<model.tableRows.itemWindow.Item> obList = FXCollections.observableArrayList();

                for (Item i : items) {
                    String userName = dbConnection.getUserName(i.getUserId());

                    switch (searchItemsCbBx.getValue()) {
                        case "Item ID":
                            if(Integer.toString(i.getItemId()).contains(search)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "Item Name":
                            if(i.getItemName().contains(search)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "User Name":
                            if(Objects.requireNonNull(userName).contains(search)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "Set or Reset Date":
                            if(String.valueOf(i.getDate()).contains(search)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "Set or Reset Time":
                            if(String.valueOf(i.getTime()).contains(search)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "Selling Stop":
                            if(i.isStopSelling()) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        case "Selling":
                            if(!i.isStopSelling()) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;

                        default:
                            if(Integer.toString(i.getItemId()).contains(search) ||
                                    i.getItemName().contains(search) ||
                                    Objects.requireNonNull(userName).contains(search) ||
                                    String.valueOf(i.getDate()).contains(search) ||
                                    String.valueOf(i.getTime()).contains(search)) {

                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getSellingStopButton(i.getItemId())));
                            }
                            break;
                    }
                }

                itemTbl.setItems(obList);
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    private void setDataIntoInputs(model.tableRows.itemWindow.Item newValue) {
        idTxt.setText(String.valueOf(newValue.getId()));
        nameTxt.setText(newValue.getName());
        addOrUpdateBtn.setText("Update");
        addOrUpdateBtn.setStyle("-fx-background-color: #feca57;");
    }

    private Button getSellingStopButton(int itemId) {
        boolean isStop = false;

        for (Item i : items) {
            if(i.getItemId() == itemId) {
                isStop = i.isStopSelling();
                break;
            }
        }

        Button btn = new Button("Resell");;
        if(isStop) {
            btn.setText("Resell");
            btn.setStyle("-fx-background-color: #1dd1a1;");

        } else {
            btn.setText("Sell Stop");
            btn.setStyle("-fx-background-color: #ff6b6b;");
        }

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform Selling STOP");
            alert.setContentText("Are you sure do you want to STOP Selling this ITEM?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        Item item = new Item();

                        for (Item i : items) {
                            if(i.getItemId() == itemId) {
                                item = i;
                                break;
                            }
                        }

                        if(!item.isStopSelling()) {
                            if(dbConnection.updateSellingItemStopOrNot(itemId, true)) {
                                item.setStopSelling(true);
                                setDataIntoItemTable();

                                alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                        "Stop Selling Successful",
                                        "Stop Selling successfully item id = " + itemId);

                            } else {
                                alert(Alert.AlertType.WARNING, "WARNING", "Can't Stop",
                                        "Try again");
                            }

                        } else {
                            if(dbConnection.updateSellingItemStopOrNot(itemId, false)) {
                                item.setStopSelling(false);
                                setDataIntoItemTable();

                                alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                        "Start Selling Successful",
                                        "Start Selling successfully item id = " + itemId);

                            } else {
                                alert(Alert.AlertType.WARNING, "WARNING", "Can't Start",
                                        "Try again");
                            }
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

    public void refreshOnAction(ActionEvent actionEvent) {
        searchItemsCbBx.setValue("All");
        searchTxt.clear();
        loadedRowCountItems = 0;

        try {
            // Set stock table
            itemsTableDataCount = dbConnection.getTableRowCount(TableTypes.ITEM_TABLE);
            nextItemTableBtn.setDisable(itemsTableDataCount < 25 && itemsTableDataCount > 0);
            items = dbConnection.getItemTable(loadedRowCountItems);
            previewItemTableBtn.setDisable(true);
            setDataIntoItemTable();

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {
            idTxt.setText((items != null && !items.isEmpty()) ?
                    String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");
        }
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
        try {
            if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {
                if(!nameTxt.getText().isEmpty()) {

                    String name = nameTxt.getText().toLowerCase().trim();

                    if (dbConnection.addItem(new Item(0, name, super.getUserId(),
                            super.getDate(), super.getTime(), false))) {

                        alert(Alert.AlertType.INFORMATION, "Successful", "Successfully Added Item",
                                "Successfully added item " + name + " With Item ID " + idTxt.getText());
                        resetOnAction(actionEvent);

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Try again",
                                "DB Connection error try again");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "Warning", "Incorrect Input",
                            "Set item name correctly");
                }

            } else if (addOrUpdateBtn.getText().equalsIgnoreCase("update")) {
                if(!nameTxt.getText().isEmpty()) {

                    String name = nameTxt.getText().toLowerCase().trim();

                    if (dbConnection.updateItem(new Item(Integer.parseInt(idTxt.getText()), name,
                            super.getUserId(), super.getDate(), super.getTime(), false))) {

                        resetOnAction(actionEvent);
                        alert(Alert.AlertType.INFORMATION, "Successful", "Successfully Update Item",
                                "Successfully update item " + name);

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Try again",
                                "DB Connection error try again");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "Warning", "Incorrect Input",
                            "Set item name correctly");
                }
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        refreshOnAction(actionEvent);
        idTxt.setText((items != null && !items.isEmpty()) ?
                String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");
        nameTxt.clear();
        addOrUpdateBtn.setText("Add");
        addOrUpdateBtn.setStyle("-fx-background-color: #1dd1a1;");
    }

    public void previewOnAction(ActionEvent actionEvent) {
        previewItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems - 25) >= 0) {
                loadedRowCountItems -= 25;
                try {
                    items = dbConnection.getItemTable(loadedRowCountItems);

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                            e.getMessage());
                }
                setDataIntoItemTable();
                nextItemTableBtn.setDisable(false);

                if((loadedRowCountItems - 25) >= 0) {
                    previewItemTableBtn.setDisable(false);
                }
            }
        }
        if(!searchTxt.getText().isEmpty()) {
            String search = searchTxt.getText();
            searchTxt.clear();
            searchTxt.setText(search);
        }
    }

    public void nextOnAction(ActionEvent actionEvent) {
        nextItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems + 25) < itemsTableDataCount) {
                loadedRowCountItems += 25;
                try {
                    items = dbConnection.getItemTable(loadedRowCountItems);

                } catch (SQLException e) {
                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                            e.getMessage());
                }
                setDataIntoItemTable();
                previewItemTableBtn.setDisable(false);

                if((loadedRowCountItems + 25) < itemsTableDataCount) {
                    nextItemTableBtn.setDisable(false);
                }
            }
        }
        if(!searchTxt.getText().isEmpty()) {
            String search = searchTxt.getText();
            searchTxt.clear();
            searchTxt.setText(search);
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            addOrUpdateOnAction(new ActionEvent());
        }
    }
}
