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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ItemFormController {
    public AnchorPane contextItemForm;
    public TextField searchTxt;
    public TableView<model.tableRows.itemWindow.Item> itemTbl;
    public TableColumn<Object, String> idCol;
    public TableColumn<Object, String> nameCol;
    public TableColumn<Object, String> deleteCol;
    public TextField nameTxt;
    public TextField idTxt;
    public Button addOrUpdateTxt;
    private ArrayList<model.Item> items;
    private final DBConnection dbConnection = DBConnection.getInstance();

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));

        try{
            items = dbConnection.getItemTable();
            if(items != null) {
                setTableData();
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        idTxt.setText(items != null ? String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<model.tableRows.itemWindow.Item> obList = FXCollections.observableArrayList();
            for (Item i : items) {
                if (i.getItemName().toLowerCase().contains(searchTxt.getText().toLowerCase()) ||
                        Integer.toString(i.getItemId()).contains(searchTxt.getText())) {
                    obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(), getDeleteButton()));
                }
            }
            itemTbl.setItems(obList);
        });

        itemTbl.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (null != newValue) {
                    setDataIntoInputs(newValue);
                }
        });
    }

    private void setDataIntoInputs(model.tableRows.itemWindow.Item newValue) {
        idTxt.setText(String.valueOf(newValue.getId()));
        nameTxt.setText(newValue.getName());
        addOrUpdateTxt.setText("Update");
    }

    private void setTableData() {
        ObservableList<model.tableRows.itemWindow.Item> obList = FXCollections.observableArrayList();
        for (Item i : items) {
            obList.add(new model.tableRows.itemWindow.Item(i.getItemId(), i.getItemName(), getDeleteButton()));
        }
        itemTbl.setItems(obList);
    }

    private Button getDeleteButton() {
        Button btn = new Button("Delete");
        return btn;
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void refreshOnAction(ActionEvent actionEvent) {
        searchTxt.clear();
        try{
            items = dbConnection.getItemTable();
            if(items != null) {
                setTableData();
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        if(addOrUpdateTxt.getText().equalsIgnoreCase("add")) {
            idTxt.setText(items != null ? String.valueOf(items.get(items.size() - 1).getItemId() + 1) : "1");
        }
    }

    public void addOrUpdateOnAction(ActionEvent actionEvent) {
        if(addOrUpdateTxt.getText().equalsIgnoreCase("add")) {
            if(!nameTxt.getText().isEmpty()) {

                String name = nameTxt.getText().toLowerCase().trim();

                if (dbConnection.addItem(new Item(0, name))) {

                    refreshOnAction(actionEvent);
                    resetOnAction(actionEvent);
                    alert(Alert.AlertType.INFORMATION, "Successful", "Successfully Added Item",
                            "Successfully added item " + name + " With Item ID " + idTxt.getText());

                } else {
                    // alert(Alert.AlertType type, String title, String headerText, String contentText)
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

                if (dbConnection.updateItem(new Item(Integer.parseInt(idTxt.getText()), name))) {

                    refreshOnAction(actionEvent);
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
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextItemForm.getScene().getWindow();
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
