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
import model.tableRows.SellItems;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SellFormController {
    public AnchorPane contextSellForm;
    public TableView quotationTbl;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn quantityCol;
    public TableColumn discountCol;
    public TableColumn priceCol;
    public TableColumn deleteCol;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField discountTxt;
    public TextField quantityTxt;
    public TextField priceTxt;
    public Label totalLbl;
    public TableView<SellItems> itemTbl;
    public TableColumn<Object, String> idCol2;
    public TableColumn<Object, String> nameCol2;
    public TextField availableQuantityTxt;
    private String searchTextId = "";
    private String searchTextName = "";
    private ArrayList<Item> items;
    private final DBConnection dbConnection = DBConnection.getInstance();


    public void initialize() {

        idCol2.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        nameCol2.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        try{
            items = dbConnection.getItemTable();
            if(items != null) {
                setTable2Data();
            }

        } catch (SQLException e) {
            alertError(Alert.AlertType.ERROR, "Error", "Item Table Data Load Error", e.getMessage());
        }

        idTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTextId = newValue.toLowerCase();
            try {
                fillOtherInputs(SellFillterTypes.ID);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        nameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTextName = newValue.toLowerCase();
            try {
                fillOtherInputs(SellFillterTypes.NAME);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

//            ObservableList<StudentTM> obList = FXCollections.observableArrayList();
//
//            for (Student st : dbcon.getStudentTable()) {
//
//                if (st.getFullName().toLowerCase().contains(searchText)) {
//                    Button btn = new Button("Delete");
//
//                    obList.add(new StudentTM(st.getId(), st.getFullName(),
//                            String.valueOf(st.getDateOfBirth()), st.getAddress(), btn));
//
//                    btn.setOnAction(e->{
//                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?",
//                                ButtonType.YES, ButtonType.NO);
//                        Optional<ButtonType> buttonType = alert.showAndWait();
//                        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
//                            dbcon.deleteStudent(st);
//                            setTableData(searchText);
//                            new Alert(Alert.AlertType.INFORMATION, "Student Delete Successfully").show();
//                        }
//                    });
//                }
//            }
//            tblStudent.setItems(obList);





        });
    }

    private void fillOtherInputs(SellFillterTypes sellFillterTypes) throws SQLException {
        switch(sellFillterTypes) {
            case ID:
                for (Item i : items) {
                    if(searchTextId.equals(Integer.toString(i.getItemId()))) {
                        nameTxt.setText(i.getItemName()); // dbConnection.getItemName(searchTextId)
                        availableQuantityTxt.setText(i.get);
                        priceTxt.setText("Rs: " + i.getSellingPrice());
                    }
                }
                break;

            case NAME:
                System.out.println(searchTextName);
                break;
        }
    }

    public void addOnAction(ActionEvent actionEvent) {

    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void sellOnAction(ActionEvent actionEvent) {
    }

    public void printInvoiceOnAction(ActionEvent actionEvent) {
    }

    public void resetInvoiceOnAction(ActionEvent actionEvent) {
    }

    public void resetItemOnAction(ActionEvent actionEvent) {
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

    private void alertError(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

}
