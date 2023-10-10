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
import model.Window;
import model.staticType.TableTypes;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static model.staticType.RefillTableTypes.ITEMS;
import static model.staticType.RefillTableTypes.REFILL_STOCK;

public class ItemFormController extends Window{
    public AnchorPane contextItemForm;
    public TextField searchTxt;
    public TableView<model.tableRows.itemWindow.Item> itemTbl;
    public TableColumn<Object, String> idCol;
    public TableColumn<Object, String> nameCol;
    public TableColumn<Object, String> deleteCol;
    public TableColumn<Object, String> userNameCol;
    public TableColumn<Object, String> dateCol;
    public TableColumn<Object, String> timeCol;
    public TextField nameTxt;
    public TextField idTxt;
    public Button addOrUpdateTxt;
    public Button previewItemTableBtn;
    public Button nextItemTableBtn;
    public ComboBox<String> searchItemsCbBx;
    private ArrayList<model.Item> items;
    private int loadedRowCountItems = 0;
    private int itemsTableDataCount;

    public void initialize() {
        super.context = contextItemForm;

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));

        searchItemsCbBx.setItems(FXCollections.observableArrayList("All", "Item ID", "Item Name",
                "User Name", "Set or Reset Date", "Set or Reset Time"));
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
            alert(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        idTxt.setText(items != null ? String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");

        searchItemsCbBx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(searchItemsCbBx.getValue() != null) {
                String search = searchTxt.getText();
                searchTxt.clear();
                searchTxt.setText(search);
            }
        });

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && items != null && !items.isEmpty()) {
                ObservableList<model.tableRows.itemWindow.Item> obList = FXCollections.observableArrayList();

                List<Integer> uniqueUserIds = items.stream().map(Item::getUserId).
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

                for (Item i : items) {
                    String userName = null;

                    for (User u : users) {
                        if(i.getUserId() == u.getUserId()) {
                            userName = u.getUserName();
                            break;
                        }
                    }

                    switch (searchItemsCbBx.getValue()) {
                        case "Item ID":
                            if(Integer.toString(i.getItemId()).contains(newValue)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;

                        case "Item Name":
                            if(i.getItemName().contains(newValue)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;

                        case "User Name":
                            if(Objects.requireNonNull(userName).contains(newValue)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;

                        case "Set or Reset Date":
                            if(String.valueOf(i.getDate()).contains(newValue)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;

                        case "Set or Reset Time":
                            if(String.valueOf(i.getTime()).contains(newValue)) {
                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;

                        default:
                            if(Integer.toString(i.getItemId()).contains(newValue) ||
                                    i.getItemName().contains(newValue) ||
                                    Objects.requireNonNull(userName).contains(newValue) ||
                                    String.valueOf(i.getDate()).contains(newValue) ||
                                    String.valueOf(i.getTime()).contains(newValue)) {

                                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(),
                                        userName, i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
                            }
                            break;
                    }
                }
                itemTbl.setItems(obList);
            }
        });

        itemTbl.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setDataIntoInputs(newValue);
                }
        });
    }

    private void setDataIntoItemTable() throws SQLException {
        ObservableList<model.tableRows.itemWindow.Item> obList = FXCollections.observableArrayList();
        ArrayList<String> userIdAndNames = new ArrayList<>();

        if(items != null && !items.isEmpty()) {
            userIdAndNames.add(items.get(0).getUserId() + "-");

            for (Item i : items) {
                for (String s : userIdAndNames) {
                    if(i.getUserId() != Integer.parseInt(s.split("-")[0])){
                        userIdAndNames.add(i.getUserId() + "-");
                        break;
                    }
                }
            }

            for (int i=0; i<userIdAndNames.size(); i++) {
                userIdAndNames.set(i, userIdAndNames.get(i) +
                        dbConnection.getUserName(Integer.parseInt(userIdAndNames.get(i).split("-")[0])));
            }

            for (Item i : items) {
                String userName = null;
                for (String ss : userIdAndNames) {
                    if(i.getUserId() == Integer.parseInt(ss.split("-")[0])) {
                        userName = ss.split("-")[1];
                        break;
                    }
                }

                obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(), userName,
                        i.getDate(), i.getTime(), getDeleteButton(i.getItemId())));
            }

            itemTbl.setItems(obList);
        }

    }

    private void setDataIntoInputs(model.tableRows.itemWindow.Item newValue) {
        idTxt.setText(String.valueOf(newValue.getId()));
        nameTxt.setText(newValue.getName());
        addOrUpdateTxt.setText("Update");
        addOrUpdateTxt.setStyle("-fx-background-color: #feca57;");
    }

    private Button getDeleteButton(int itemId) {
        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform DELETE");
            alert.setContentText("Are you sure do you want to DELETE this ITEM?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if(dbConnection.deleteItem(itemId)) {
                        try {
                            for (Item i : items) {
                                if(i.getItemId() == itemId) {
                                    items.remove(i);
                                    break;
                                }
                            }
                            setDataIntoItemTable();

                            alert(Alert.AlertType.INFORMATION, "INFORMATION", "Delete Successful",
                                    "Delete successfully stock id = " + itemId);

                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Data Reload Error",
                                    "Cant reload data in table");
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "ERROR", "Can't Delete",
                                "This item have stocks");
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

        if(addOrUpdateTxt.getText().equalsIgnoreCase("add")) {
            idTxt.setText(items != null ? String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");
        }
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
        if(addOrUpdateTxt.getText().equalsIgnoreCase("add")) {
            if(!nameTxt.getText().isEmpty()) {

                String name = nameTxt.getText().toLowerCase().trim();

                if (dbConnection.addItem(new Item(0, name, super.getUserId(),
                        new Date(Calendar.getInstance().getTime().getTime()),
                        new Time(Calendar.getInstance().getTime().getTime())))) {

                    resetOnAction(actionEvent);
                    alert(Alert.AlertType.INFORMATION, "Successful", "Successfully Added Item",
                            "Successfully added item " + name + " With Item ID " + idTxt.getText());

                } else {
                    alert(Alert.AlertType.ERROR, "Error", "Try again",
                            "DB Connection error try again");
                }

            } else {
                alert(Alert.AlertType.WARNING, "Warning", "Incorrect Input",
                        "Set item name correctly");
            }

        } else if (addOrUpdateTxt.getText().equalsIgnoreCase("update")) {
            if(!nameTxt.getText().isEmpty()) {

                String name = nameTxt.getText().toLowerCase().trim();

                if (dbConnection.updateItem(new Item(Integer.parseInt(idTxt.getText()), name, super.getUserId(),
                        new Date(Calendar.getInstance().getTime().getTime()),
                        new Time(Calendar.getInstance().getTime().getTime())))) {

                    resetOnAction(actionEvent);
                    alert(Alert.AlertType.INFORMATION, "Successful", "Successfully Update Item",
                            "Successfully update item " + name);

                } else {
                    // alert(Alert.AlertType type, String title, String headerText, String contentText)
                    alert(Alert.AlertType.ERROR, "Error", "Try again",
                            "DB Connection error try again");
                }

            } else {
                alert(Alert.AlertType.WARNING, "Warning", "Incorrect Input",
                        "Set item name correctly");
            }
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        refreshOnAction(actionEvent);
        idTxt.setText(items != null ? String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");
        nameTxt.clear();
        addOrUpdateTxt.setText("Add");
        addOrUpdateTxt.setStyle("-fx-background-color: #1dd1a1;");
    }

    public void previewOnAction(ActionEvent actionEvent) throws SQLException {
        previewItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems - 25) >= 0) {
                loadedRowCountItems -= 25;
                items = dbConnection.getItemTable(loadedRowCountItems);
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

    public void nextOnAction(ActionEvent actionEvent) throws SQLException {
        nextItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems + 25) < itemsTableDataCount) {
                loadedRowCountItems += 25;
                items = dbConnection.getItemTable(loadedRowCountItems);
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

}
