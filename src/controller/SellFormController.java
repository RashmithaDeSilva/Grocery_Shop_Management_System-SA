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
import model.staticType.SellFillterTypes;
import model.tableRows.InvoiceItems;
import model.tableRows.SellItems;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class SellFormController {
    public AnchorPane contextSellForm;
    public TableView<InvoiceItems> quotationTbl;
    public TableColumn<Object, String> idCol;
    public TableColumn<Object, String> nameCol;
    public TableColumn<Object, String> quantityCol;
    public TableColumn<Object, String> discountCol;
    public TableColumn<Object, String> priceCol;
    public TableColumn<Object, String> deleteCol;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField discountTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public TableView<SellItems> itemTbl;
    public TableColumn<Object, String> idCol2;
    public TableColumn<Object, String> nameCol2;
    public TextField availableQuantityTxt;
    public TextField searchTxt;
    public TextField totalPriceTxt;
    public Label totalBill;
    public Button addOrUpdateBtn;
    private ArrayList<Item> items;
    private final DBConnection dbConnection = DBConnection.getInstance();
    DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public void initialize() {
        idCol2.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        nameCol2.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("delete"));

        try{
            items = dbConnection.getItemTable();
            if(items != null) {
                setTable2Data();
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<SellItems> obList = FXCollections.observableArrayList();
            for (Item i : items) {
                if (i.getItemName().toLowerCase().contains(searchTxt.getText().toLowerCase()) ||
                        Integer.toString(i.getItemId()).contains(searchTxt.getText())) {
                    obList.add(new SellItems(i.getItemId(), i.getItemName()));
                }
            }
            itemTbl.setItems(obList);
        });

        quantityTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()) {
                try{
                    int quantity = Integer.parseInt(newValue);

                    if(!idTxt.getText().isEmpty()) {
                        double price = 0;
                        int availableQuantity = Integer.parseInt(availableQuantityTxt.getText());

                        for (Item i : items) {
                            if(i.getItemId() == Integer.parseInt(idTxt.getText())) {
                                price = i.getSellingPrice();
                            }
                        }

                        if(quantity >= 0 && quantity <= availableQuantity) {
                            priceTxt.setText(String.valueOf(quantity * price));

                        } else {
                            alert(Alert.AlertType.ERROR, "Invalid Input",
                                    "Select Correct Quantity",
                                    "Quantity must be in less than available quantity");
                            quantityTxt.setText(oldValue);
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "Invalid Input",
                                "Select Item Before Set Quantity",
                                "You did not select item so select item before set the quantity");
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.ERROR, "Invalid Input",
                            "Set Integer Value Into Quantity", e.getMessage());
                }

            } else {
                assert newValue != null;
                priceTxt.setText("0.00");
            }
        });

        priceTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()) {
                try{
                    double price = Double.parseDouble(newValue);
                    double discount = discountTxt.getText().isEmpty() ?
                            0 : Double.parseDouble(discountTxt.getText());

                    if(price >= discount) {
                        totalPriceTxt.setText(decimalFormat.format(price - discount));

                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.ERROR, "Invalid Input",
                            "Set Integer Value Into Price", e.getMessage());
                }
            } else {
                assert newValue != null;
                totalPriceTxt.setText("0.00");
            }
        });

        discountTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()) {
                try{
                    if(!idTxt.getText().isEmpty()) {
                        double price = Double.parseDouble(priceTxt.getText());
                        double discount = Double.parseDouble(newValue);

                        if(price >= discount) {
                            totalPriceTxt.setText(decimalFormat.format(price - discount));

                        } else {
                            alert(Alert.AlertType.ERROR, "Invalid Input",
                                    "Discount Is Greathearted Price",
                                    "Discount must be less than  price so set discount again");
                            discountTxt.setText(oldValue);
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "Invalid Input",
                                "Select Item Before Set Discount",
                                "You did not select item so select item before set the discount");
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.ERROR, "Invalid Input",
                            "Set Integer Value Into Discount", e.getMessage());
                }
            } else {
                assert newValue != null;
                    totalPriceTxt.setText(decimalFormat.format(Double.parseDouble(
                            priceTxt.getText().isEmpty() ? "0.00" : priceTxt.getText())));
            }
        });

        itemTbl.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            if (null != newValue) {
                setDataIntoInputs(newValue);
            }
        });

        quotationTbl.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            if (null != newValue) {
                setDataIntoInputs(newValue);
            }
        });
    }

    private void setDataIntoInputs(InvoiceItems newValue) {
        resetAllInputs();
        idTxt.setText(Integer.toString(newValue.getItemId()));
        nameTxt.setText(newValue.getItemName());
        for (Item i : items) {
            if(i.getItemId() == newValue.getItemId()) {
                availableQuantityTxt.setText(Integer.toString(i.getQuantity()));
                break;
            }
        }
        quantityTxt.setText(Integer.toString(newValue.getQuantity()));
        priceTxt.setText(Double.toString(newValue.getDiscount() + newValue.getPrice()));
        discountTxt.setText(Double.toString(newValue.getDiscount()));
        totalPriceTxt.setText(Double.toString(newValue.getPrice()));
        addOrUpdateBtn.setText("Update");
    }

    private void setDataIntoInputs(SellItems newValue) {
        for (Item i : items) {
            if(i.getItemId() == newValue.getItemId()) {
                resetAllInputs();
                idTxt.setText(Integer.toString(i.getItemId()));
                nameTxt.setText(i.getItemName());
                availableQuantityTxt.setText(Integer.toString(i.getQuantity()));
                quantityTxt.setText("1");
                priceTxt.setText(Double.toString(i.getSellingPrice()));
                addOrUpdateBtn.setText("Add");
                break;
            }
        }
    }

    private void resetAllInputs() {
        idTxt.clear();
        nameTxt.clear();
        quantityTxt.clear();
        availableQuantityTxt.clear();
        discountTxt.clear();
        priceTxt.clear();
        totalPriceTxt.clear();
    }

    public void addOnAction(ActionEvent actionEvent) {
        if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {
            boolean notAddedToInvoice = true;
            if(quotationTbl != null) {
                for (InvoiceItems i : quotationTbl.getItems()) {
                    if(Integer.parseInt(idTxt.getText()) == i.getItemId()) {
                        notAddedToInvoice = false;
                        break;
                    }
                }
            }

            if(notAddedToInvoice) {
                Button btn = new Button("Delete");

                try {
                    if(Integer.parseInt(quantityTxt.getText()) > 0 && Integer.parseInt(quantityTxt.getText())
                            <= Integer.parseInt(availableQuantityTxt.getText())){
                        try{
                            if(discountTxt.getText().isEmpty() || Double.parseDouble(discountTxt.getText()) == 0) {
                                quotationTbl.getItems().add(new InvoiceItems(Integer.parseInt(idTxt.getText()),
                                        nameTxt.getText(), Integer.parseInt(quantityTxt.getText()),
                                        0, Double.parseDouble(totalPriceTxt.getText()), btn));
                                resetAllInputs();
                                searchTxt.clear();

                            } else if(Double.parseDouble(discountTxt.getText()) > 0 &&
                                    Double.parseDouble(discountTxt.getText()) <= Double.parseDouble(priceTxt.getText())) {
                                quotationTbl.getItems().add(new InvoiceItems(Integer.parseInt(idTxt.getText()),
                                        nameTxt.getText(), Integer.parseInt(quantityTxt.getText()),
                                        Double.parseDouble(discountTxt.getText()), Double.parseDouble(totalPriceTxt.getText()), btn));
                                resetAllInputs();
                                searchTxt.clear();

                            } else {
                                alert(Alert.AlertType.ERROR, "Invalid Input",
                                        "Set Discount Correctly",
                                        "Sorry this discount is incorrect");
                            }

                        } catch (NumberFormatException e) {
                            alert(Alert.AlertType.ERROR, "Invalid Input",
                                    "Set Integer or Float Value Into Discount", e.getMessage());
                        }

                    } else {
                        alert(Alert.AlertType.ERROR, "Invalid Input",
                                "Set Quantity Correctly", "Sorry this quantity is not available");
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.ERROR, "Invalid Input",
                            "Set Integer Value Into Quantity", e.getMessage());
                }

            } else {
                alert(Alert.AlertType.INFORMATION, "Check Invoice",
                        "This Item Is Already Added",
                        "This item is already added into invoice if you want to update it try to update");
            }

        } else if(addOrUpdateBtn.getText().equalsIgnoreCase("update")) {
            for (InvoiceItems i : quotationTbl.getItems()) {
                if(i.getItemId() == Integer.parseInt(idTxt.getText())) {
                    try {
                        if(Integer.parseInt(quantityTxt.getText()) > 0 && Integer.parseInt(quantityTxt.getText())
                                <= Integer.parseInt(availableQuantityTxt.getText())){
                            try{
                                if(discountTxt.getText().isEmpty() || Double.parseDouble(discountTxt.getText()) == 0) {

                                    i.setDiscount(Double.parseDouble("0.00"));

                                } else if(Double.parseDouble(discountTxt.getText()) > 0 &&
                                        Double.parseDouble(discountTxt.getText()) <= Double.parseDouble(priceTxt.getText())) {

                                    i.setDiscount(Double.parseDouble(discountTxt.getText()));

                                } else {
                                    alert(Alert.AlertType.ERROR, "Invalid Input",
                                            "Set Discount Correctly",
                                            "Sorry this discount is incorrect");
                                }

                                i.setQuantity(Integer.parseInt(quantityTxt.getText()));
                                i.setPrice(Double.parseDouble(totalPriceTxt.getText()));
                                quotationTbl.refresh();
                                resetAllInputs();
                                searchTxt.clear();
                                addOrUpdateBtn.setText("Add");

                            } catch (NumberFormatException e) {
                                alert(Alert.AlertType.ERROR, "Invalid Input",
                                        "Set Integer or Float Value Into Discount", e.getMessage());
                            }

                        } else {
                            alert(Alert.AlertType.ERROR, "Invalid Input",
                                    "Set Quantity Correctly", "Sorry this quantity is not available");
                        }

                    } catch (NumberFormatException e) {
                        alert(Alert.AlertType.ERROR, "Invalid Input",
                                "Set Integer Value Into Quantity", e.getMessage());
                    }
                    break;
                }
            }
        }
        setTotal();
    }

    private void setTotal() {
        double total = 0;
        if(quotationTbl != null) {
            for (InvoiceItems i : quotationTbl.getItems()) {
                total += i.getPrice();
            }
        }
        totalBill.setText("Rs: " + decimalFormat.format(total));
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellOnAction(ActionEvent actionEvent) {
    }

    public void printInvoiceOnAction(ActionEvent actionEvent) {
    }

    public void resetInvoiceOnAction(ActionEvent actionEvent) {
        quotationTbl.getItems().clear();
    }

    public void resetInputOnAction(ActionEvent actionEvent) {
        resetAllInputs();
    }

    public void resetAllOnActon(ActionEvent actionEvent) {
        resetAllInputs();
        searchTxt.clear();
        quotationTbl.getItems().clear();
    }

    private void setTable2Data() throws SQLException {
        ObservableList<SellItems> obList = FXCollections.observableArrayList();
        for (Item i : items) {
            obList.add(new SellItems(i.getItemId(), i.getItemName()));
        }
        itemTbl.setItems(obList);
    }

    private void setUI(String UI_Name) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/" + UI_Name + ".fxml")));
        Stage stage = (Stage) contextSellForm.getScene().getWindow();
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
