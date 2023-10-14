package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Window;
import model.staticType.TableTypes;
import model.tableRows.userManagerWindow.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserManagementFormController extends Window {
    public AnchorPane contextUserManagement;
    public TextField nameTxt;
    public TextField emailTxt;
    public PasswordField passwordTxt;
    public PasswordField conformPasswordTxt;
    public ComboBox<String> userAccessesCmb;
    public TableView<User> userTbl;
    public TableColumn<Object, String> idCol;
    public TableColumn<Object, String> nameCol;
    public TableColumn<Object, String> emailCol;
    public TableColumn<Object, String> rollCol;
    public TableColumn<Object, String> deleteCol;
    public TextField searchTxt;
    public ComboBox<String> searchUserAccessesCmb;
    public Button previewUsersTableBtn;
    public Button nextUsersTableBtn;
    private ArrayList<model.User> users;
    private int loadedRowCountUsers = 0;
    private int userTableDataCount;


    public void initialize() throws IOException {
        super.context = contextUserManagement;

        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        rollCol.setCellValueFactory(new PropertyValueFactory<>("roll"));
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("btn"));

        userAccessesCmb.setItems(FXCollections.observableArrayList("User Roll", "Admin", "User"));
        userAccessesCmb.setValue("User Roll");
        searchUserAccessesCmb.setItems(FXCollections.observableArrayList("All", "User ID", "Name",
                "Email", "Roll"));
        searchUserAccessesCmb.setValue("All");

        try {
            userTableDataCount = dbConnection.getTableRowCount(TableTypes.USER_TABLE);

            if(userTableDataCount < 25 && userTableDataCount > 0) {
                nextUsersTableBtn.setDisable(true);
            }
            users = dbConnection.getUsersTable(loadedRowCountUsers);
            previewUsersTableBtn.setDisable(true);

            setDataIntoUsersTable();

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    private void setDataIntoUsersTable() {
        ObservableList<User> obList = FXCollections.observableArrayList();

        if(users != null && !users.isEmpty()) {
            for (model.User u : users) {
                if(u.getTitle() != 0) {
                    if(super.getUserRoll() == 2 && u.getTitle() >= 2) {
                        obList.add(new User(u.getUserId(), u.getUserName(), u.getEmail(),
                                u.getTitle() == 2 ? "Admin" : "User",
                                getDeleteButton(u.getUserId())));

                    } else if (super.getUserRoll() == 1) {
                        obList.add(new User(u.getUserId(), u.getUserName(), u.getEmail(),
                                u.getTitle() == 1 ? "Super Admin" : u.getTitle() == 2 ? "Admin" : "User",
                                getDeleteButton(u.getUserId())));
                    }
                }
            }

            userTbl.setItems(obList);
        }
    }

    private Button getDeleteButton(int userId) {
        Button btn = new Button("Delete");
        btn.setStyle("-fx-background-color:  #ff6b6b;");

        btn.setOnAction((e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Conform DELETE");
            alert.setContentText("Are you sure do you want to DELETE this User?");
            alert.getButtonTypes().set(0, ButtonType.YES);
            alert.getButtonTypes().set(1, ButtonType.NO);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if(dbConnection.deleteUser(userId)) {
                        for (model.User u : users) {
                            if(u.getUserId() == userId) {
                                users.remove(u);
                                break;
                            }
                        }
                        setDataIntoUsersTable();

                        alert(Alert.AlertType.INFORMATION, "INFORMATION", "Delete Successful",
                                "Delete successfully user id: " + userId);

                    } else {
                        alert(Alert.AlertType.ERROR, "ERROR", "Can't Delete",
                                "This item have stocks");
                    }
                }
            });
        });

        if(super.getUserId() == userId) {
            btn.setDisable(true);
        }

        return btn;
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void userProfileOnAction(ActionEvent actionEvent) throws IOException {
        setUI("UserForm");
    }

    public void addOrUpdateUsersOnAction(ActionEvent actionEvent) {
    }

    public void resetOnAction(ActionEvent actionEvent) {
    }

    public void searchUserAccessesOnAction(ActionEvent actionEvent) {
    }

    public void refreshOnAction(ActionEvent actionEvent) {
    }

    public void previewUsersTableOnAction(ActionEvent actionEvent) {
    }

    public void nextUsersTableOnAction(ActionEvent actionEvent) {
    }
}
