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
import java.util.concurrent.atomic.AtomicBoolean;


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
    public Button addOrUpdateBtn;
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
                                u.getTitle() == 2 ? "Admin" : u.getTitle() == 3 ? "User": "Banned",
                                getBannedUnbannedButton(u.getUserId())));

                    } else if (super.getUserRoll() == 1) {
                        obList.add(new User(u.getUserId(), u.getUserName(), u.getEmail(),
                                u.getTitle() == 1 ? "Super Admin" : u.getTitle() == 2 ? "Admin" :
                                        u.getTitle() == 3 ? "User": "Banned",
                                getBannedUnbannedButton(u.getUserId())));
                    }
                }
            }

            userTbl.setItems(obList);
        }
    }

    private Button getBannedUnbannedButton(int userId) {

        Button btn = new Button("");
        boolean isBand = false;

        for (model.User u : users) {
            if(u.getUserId() == userId) {
                isBand = u.getTitle() == 4;
            }
        }

        if(!isBand) {
            btn.setText("Banned");
            btn.setStyle("-fx-background-color:  #ff6b6b;");

            btn.setOnAction((e) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Conform Banned");
                alert.setContentText("Are you sure do you want to Banned this User?");
                alert.getButtonTypes().set(0, ButtonType.YES);
                alert.getButtonTypes().set(1, ButtonType.NO);

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            if(dbConnection.bandUser(userId)) {
                                for (model.User u : users) {
                                    if(u.getUserId() == userId) {
                                        u.setTitle(4);
                                        break;
                                    }
                                }
                                setDataIntoUsersTable();

                                alert(Alert.AlertType.INFORMATION, "INFORMATION", "Banned Successful",
                                        "Banned successfully user " + dbConnection.getUserName(userId));

                            } else {
                                alert(Alert.AlertType.ERROR, "ERROR", "Can't Banned",
                                        "This user can not Banned");
                            }
                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Can't Banned",
                                    "This user can not Banned");
                        }
                    }
                });
            });

        } else {
            btn.setText("Unbanned");
            btn.setStyle("-fx-background-color:  #1dd1a1;");

            btn.setOnAction((e) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Conform Unbanned");
                alert.setContentText("Are you sure do you want to Unbanned this User?");
                alert.getButtonTypes().set(0, ButtonType.YES);
                alert.getButtonTypes().set(1, ButtonType.NO);

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            if(dbConnection.unbannedUser(userId)) {
                                for (model.User u : users) {
                                    if(u.getUserId() == userId) {
                                        u.setTitle(3);
                                        break;
                                    }
                                }
                                setDataIntoUsersTable();

                                alert(Alert.AlertType.INFORMATION, "INFORMATION", "Unbanned Successful",
                                        "BAND successfully user " + dbConnection.getUserName(userId));

                            } else {
                                alert(Alert.AlertType.ERROR, "ERROR", "Can't Unbanned",
                                        "This user can not Unbanned");
                            }
                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Can't Unbanned",
                                    "This user can not Unbanned");
                        }
                    }
                });
            });
        }

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
        try {
            if(userTableDataCount+1 <= super.getMaximumUserCount()) {
                if(!dbConnection.userNameIsAvailable(nameTxt.getText().trim().toLowerCase())) {
                    if(!dbConnection.mailIsAvailable(emailTxt.getText().trim().toLowerCase())) {
                        if(passwordTxt.getText().trim().equals(conformPasswordTxt.getText().trim()) &&
                                !passwordTxt.getText().isEmpty() && !conformPasswordTxt.getText().isEmpty()) {
                            if(!userAccessesCmb.getValue().equalsIgnoreCase("User Roll")) {
                                if(dbConnection.addUser(new model.User(nameTxt.getText().trim().toLowerCase(),
                                        emailTxt.getText().trim().toLowerCase(), passwordTxt.getText().trim(),
                                        userAccessesCmb.getValue().equalsIgnoreCase("admin") ? 2 : 3))){

                                    userTableDataCount +=1;
                                    try {
                                        if(userTableDataCount < 25 && userTableDataCount > 0) {
                                            nextUsersTableBtn.setDisable(true);
                                        }
                                        users = dbConnection.getUsersTable(loadedRowCountUsers);
                                        previewUsersTableBtn.setDisable(true);

                                        resetOnAction(actionEvent);
                                        setDataIntoUsersTable();

                                    } catch (SQLException e) {
                                        alert(Alert.AlertType.ERROR, "ERROR",
                                                "Database Connection Error", e.getMessage());
                                    }

                                    alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successfully Added User",
                                            "Successfully added user into database");

                                } else {
                                    alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                            "Try again");
                                }

                            } else {
                                alert(Alert.AlertType.WARNING, "WARNING", "Select User Roll",
                                        "Select users access and try again");
                            }

                        } else {
                            alert(Alert.AlertType.WARNING, "WARNING", "Password Not Mach",
                                    "This password is not mach try again");
                        }

                    }else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Email Can't Use",
                                "This email is already use try again with another email");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "WARNING", "User Name Can't Use",
                            "This user name is already use try again with another name");
                }

            } else {
                alert(Alert.AlertType.WARNING, "WARNING", "Maximum User Limit Is Up",
                        "Maximum user limit is full, if you want to add more users contact " +
                                "developer and buy more specie");
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        nameTxt.clear();
        emailTxt.clear();
        passwordTxt.clear();
        conformPasswordTxt.clear();
        userAccessesCmb.setValue("User Roll");
        addOrUpdateBtn.setText("Add User");
        addOrUpdateBtn.setStyle("-fx-background-color:  #1dd1a1;");

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
