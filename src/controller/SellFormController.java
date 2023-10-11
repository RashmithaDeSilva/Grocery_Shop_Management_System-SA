package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Item;
import model.Stock;
import model.Window;
import model.staticType.TableTypes;
import model.tableRows.sellWindow.InvoiceItem;
import model.tableRows.sellWindow.SellItem;

import java.io.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SellFormController extends Window{
    public AnchorPane contextSellForm;
    public TableView<InvoiceItem> quotationTbl;
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
    public TableView<SellItem> itemTbl;
    public TableColumn<Object, String> idCol2;
    public TableColumn<Object, String> nameCol2;
    public TextField availableQuantityTxt;
    public TextField searchTxt;
    public TextField totalPriceTxt;
    public Label totalBill;
    public Button addOrUpdateBtn;
    public Button previewItemTableBtn;
    public Button nextItemTableBtn;
    private ArrayList<Item> items;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int loadedRowCountItems = 0;
    private int itemsTableDataCount;


    public void initialize() {
        super.context = contextSellForm;

        idCol2.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        nameCol2.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("delete"));

        try{
            itemsTableDataCount = dbConnection.getTableRowCount(TableTypes.STOCK_AVAILABLE_ITEM_TABLE);

            // Set item table
            if(itemsTableDataCount < 25 && itemsTableDataCount > 0) {
                nextItemTableBtn.setDisable(true);
            }
            items = dbConnection.getItemTableWithStockAvailable(loadedRowCountItems);
            previewItemTableBtn.setDisable(true);

            setTable2Data();

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && items != null && !items.isEmpty()) {
                ObservableList<SellItem> obList = FXCollections.observableArrayList();
                for (Item i : items) {
                    if (i.getItemName().toLowerCase().contains(searchTxt.getText().toLowerCase()) ||
                            Integer.toString(i.getItemId()).contains(searchTxt.getText())) {
                        obList.add(new SellItem(i.getItemId(), i.getItemName()));
                    }
                }
                itemTbl.setItems(obList);
            }
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
                                if(!i.getStocks().isEmpty()) {
                                    price = i.getStocks().get(0).getSellingPrice();
                                }
                            }
                        }

                        if(quantity >= 0 && quantity <= availableQuantity) {
                            priceTxt.setText(String.valueOf(quantity * price));

                        } else {
                            alert(Alert.AlertType.WARNING, "Invalid Input",
                                    "Select Correct Quantity",
                                    "Quantity must be in less than available quantity");
                            quantityTxt.setText(oldValue);
                        }

                    } else {
                        alert(Alert.AlertType.WARNING, "Invalid Input",
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
                            alert(Alert.AlertType.WARNING, "Invalid Input",
                                    "Discount Is Greathearted Price",
                                    "Discount must be less than  price so set discount again");
                            discountTxt.setText(oldValue);
                        }

                    } else {
                        alert(Alert.AlertType.WARNING, "Invalid Input",
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

    private void setDataIntoInputs(InvoiceItem newValue) {
        resetAllInputs();
        idTxt.setText(Integer.toString(newValue.getItemId()));
        nameTxt.setText(newValue.getItemName());
        for (Item i : items) {
            if(i.getItemId() == newValue.getItemId()) {
                int quantity = 0;
                for (Stock s : i.getStocks()) {
                    quantity += s.getQuantity();
                }
                availableQuantityTxt.setText(Integer.toString(quantity));
                break;
            }
        }
        quantityTxt.setText(Integer.toString(newValue.getQuantity()));
        priceTxt.setText(Double.toString(newValue.getDiscount() + newValue.getPrice()));
        discountTxt.setText(Double.toString(newValue.getDiscount()));
        totalPriceTxt.setText(Double.toString(newValue.getPrice()));
        addOrUpdateBtn.setText("Update");
        addOrUpdateBtn.setStyle("-fx-background-color: #feca57;");
    }

    private void setDataIntoInputs(SellItem newValue) {
        for (Item i : items) {
            if(i.getItemId() == newValue.getItemId()) {
                resetAllInputs();
                idTxt.setText(Integer.toString(i.getItemId()));
                nameTxt.setText(i.getItemName());
                int quantity = 0;
                for (Stock s : i.getStocks()) {
                    quantity += s.getQuantity();
                }
                availableQuantityTxt.setText(Integer.toString(quantity));
                quantityTxt.setText("1");
                priceTxt.setText(Double.toString(i.getStocks().get(0).getSellingPrice()));
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
        addOrUpdateBtn.setText("Add");
        addOrUpdateBtn.setStyle("-fx-background-color: #1dd1a1;");
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

                    for (InvoiceItem i : quotationTbl.getItems()) {
                        if(i.getItemId() == itemId) {

                            totalBill.setText(String.valueOf(Double.parseDouble(
                                    totalBill.getText().split(" ")[1]) - i.getPrice()));
                            quotationTbl.getItems().remove(i);
                            break;
                        }
                    }

                    alert(Alert.AlertType.INFORMATION, "INFORMATION", "Delete Successful",
                            "Delete successfully stock id = " + itemId);
                }
            });
        });

        return btn;
    }

    public void addOnAction(ActionEvent actionEvent) {
        if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {
            boolean notAddedToInvoice = true;
            if(quotationTbl != null && !quotationTbl.getItems().isEmpty()) {
                for (InvoiceItem i : quotationTbl.getItems()) {
                    if(Integer.parseInt(idTxt.getText()) == i.getItemId()) {
                        notAddedToInvoice = false;
                        break;
                    }
                }
            }

            if(notAddedToInvoice) {
                try {
                    if(Integer.parseInt(quantityTxt.getText()) > 0 && Integer.parseInt(quantityTxt.getText())
                            <= Integer.parseInt(availableQuantityTxt.getText())){
                        try{
                            if(discountTxt.getText().isEmpty() || Double.parseDouble(discountTxt.getText()) == 0) {
                                quotationTbl.getItems().add(new InvoiceItem(Integer.parseInt(idTxt.getText()),
                                        nameTxt.getText(), Integer.parseInt(quantityTxt.getText()),
                                        0, Double.parseDouble(totalPriceTxt.getText()),
                                        getDeleteButton(Integer.parseInt(idTxt.getText()))));
                                resetAllInputs();
                                searchTxt.clear();

                            } else if(Double.parseDouble(discountTxt.getText()) > 0 &&
                                    Double.parseDouble(discountTxt.getText()) <= Double.parseDouble(priceTxt.getText())) {
                                quotationTbl.getItems().add(new InvoiceItem(Integer.parseInt(idTxt.getText()),
                                        nameTxt.getText(), Integer.parseInt(quantityTxt.getText()),
                                        Double.parseDouble(discountTxt.getText()), Double.parseDouble(totalPriceTxt.getText()),
                                        getDeleteButton(Integer.parseInt(idTxt.getText()))));
                                resetAllInputs();
                                searchTxt.clear();

                            } else {
                                alert(Alert.AlertType.WARNING, "Invalid Input",
                                        "Set Discount Correctly",
                                        "Sorry this discount is incorrect");
                            }

                        } catch (NumberFormatException e) {
                            alert(Alert.AlertType.ERROR, "Invalid Input",
                                    "Set Integer or Float Value Into Discount", e.getMessage());
                        }

                    } else {
                        alert(Alert.AlertType.WARNING, "Invalid Input",
                                "Set Quantity Correctly", "Sorry this quantity is not available");
                    }

                } catch (NumberFormatException e) {
                    alert(Alert.AlertType.ERROR, "Invalid Input",
                            "Set Integer Value Into Quantity", e.getMessage());
                }

            } else {
                alert(Alert.AlertType.WARNING, "Check Invoice",
                        "This Item Is Already Added",
                        "This item is already added into invoice if you want to update it try to update");
            }

        } else if(addOrUpdateBtn.getText().equalsIgnoreCase("update")) {
            for (InvoiceItem i : quotationTbl.getItems()) {
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
                                    alert(Alert.AlertType.WARNING, "Invalid Input",
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
                            alert(Alert.AlertType.WARNING, "Invalid Input",
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
            for (InvoiceItem i : quotationTbl.getItems()) {
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
        if(!quotationTbl.getItems().isEmpty()) {
            // Get the user's home directory
            String userHome = System.getProperty("user.home");

            // Determine the "Downloads" folder path based on the operating system
            String downloadsFolder;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                downloadsFolder = userHome + "\\Downloads";

            } else if (os.contains("mac")) {
                downloadsFolder = userHome + "/Downloads";

            } else {
                downloadsFolder = userHome + "/Downloads";
            }

            // Create an "Invoice" folder inside the "Downloads" folder
            String invoiceFolderPath = downloadsFolder + "/Invoice";
            File invoiceFolder = new File(invoiceFolderPath);
            if (!invoiceFolder.exists()) {
                if (invoiceFolder.mkdirs()) {
                    alert(Alert.AlertType.CONFIRMATION, "Successful",
                            "Invoice folder created successfully",
                            invoiceFolderPath);
//                    System.out.println("Invoice folder created successfully at: " + invoiceFolderPath);

                } else {
                    alert(Alert.AlertType.ERROR, "Folder Error",
                            "Folder Error",
                            "Failed to create the Invoice folder");
//                    System.err.println("Failed to create the Invoice folder");
                    return;
                }
            }

            // Generate a timestamp for the file name
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String timestamp = dateFormat.format(new Date());

            // Create the "Invoice.txt" file with a timestamp within the "Invoice" folder
            String fileName = "Invoice_" + timestamp + ".txt";
            File file = new File(invoiceFolderPath, fileName);

            try (FileWriter writer = new FileWriter(file)) {
                // Write content to the file
                String formattedTital = String.format("%-30s %10s %10s %10s %10s%n",
                        "Item name", "Quantity", "Price", "Discount", "Total");
                writer.write(formattedTital);

                for (InvoiceItem i : quotationTbl.getItems()) {
                    // Use String.format to format each field with the specified width
                    String formattedLine = String.format("%-30s %10d %10.2f %10s) %10.2f%n",
                            i.getItemName(), i.getQuantity(), (i.getPrice() + i.getDiscount()),
                            "(" + i.getDiscount(), i.getPrice());
                    writer.write(formattedLine);
                }

                writer.write("\nTotal Bill " + totalBill.getText());

                alert(Alert.AlertType.CONFIRMATION, "Successful",
                        "Successfully Print Invoice",
                        "Check: " + invoiceFolder.getPath() + "\n" + "File name: " + fileName);
//                System.out.println("File created successfully at: " + file.getAbsolutePath());

            } catch (IOException e) {
                alert(Alert.AlertType.ERROR, "An Error Occurred",
                        "Folder Error", e.getMessage());
//                System.err.println("An error occurred: " + e.getMessage());
            }

        } else {
            alert(Alert.AlertType.WARNING, "Can Not Try",
                    "There Have No Items To Print",
                    "Sorry their have no items to print so add items");
        }
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
        if(items != null && !items.isEmpty()) {
            ObservableList<SellItem> obList = FXCollections.observableArrayList();
            for (Item i : items) {
                obList.add(new SellItem(i.getItemId(), i.getItemName()));
            }
            itemTbl.setItems(obList);
        }
    }

    public void sellLogOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SellLogForm");
    }

    public void previewItemsOnAction(ActionEvent actionEvent) throws SQLException {
        previewItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems - 25) >= 0) {
                loadedRowCountItems -= 25;
                items = dbConnection.getItemTableWithStockAvailable(loadedRowCountItems);
                setTable2Data();
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

    public void nextItemsOnAction(ActionEvent actionEvent) throws SQLException {
        nextItemTableBtn.setDisable(true);
        if(items != null && !items.isEmpty()) {
            if((loadedRowCountItems + 25) < itemsTableDataCount) {
                loadedRowCountItems += 25;
                items = dbConnection.getItemTableWithStockAvailable(loadedRowCountItems);
                setTable2Data();
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
