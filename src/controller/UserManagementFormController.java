package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Stock;
import model.Window;
import model.staticType.TableTypes;
import model.tableRows.userManagerWindow.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static model.staticType.RefillTableTypes.ITEMS;
import static model.staticType.RefillTableTypes.REFILL_STOCK;


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
    private String searchText = "";
    private int selectedUserId = -1;


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

        searchUserAccessesCmb.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(searchUserAccessesCmb.getValue() != null) {
                        String search = searchTxt.getText();
                        searchTxt.clear();
                        searchTxt.setText(search);
                    }
                });

        // Search stock
        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && users != null && !users.isEmpty()) {
                searchText = searchTxt.getText();
                setDataIntoUsersTable();
            }
        });

        userTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                selectedUserId = newValue.getUserId();
                setRowDataIntoInputs(newValue);
            }
        });
    }

    private void setRowDataIntoInputs(User newValue) {
        if(super.getUserId() != newValue.getUserId() && newValue.getUserId() != 1) {
            nameTxt.setText(newValue.getName());
            emailTxt.setText(newValue.getEmail());
            userAccessesCmb.setValue(newValue.getRoll().equalsIgnoreCase("admin") ? "Admin" : "User");
            addOrUpdateBtn.setText("Update");
            addOrUpdateBtn.setStyle("-fx-background-color: #feca57;");

        } else {
            alert(Alert.AlertType.WARNING, "WARNING", "Use User Profile",
                    "Use user profile to update your profile");
        }
    }

    private void setDataIntoUsersTable() {
        if(users != null && !users.isEmpty()) {
            ObservableList<User> obList = FXCollections.observableArrayList();

            for (model.User u : users) {
                if(u.getTitle() != 0) {

                    String title = u.getTitle() == 1 ? "Super Admin" : u.getTitle() == 2 ? "Admin" :
                            u.getTitle() == 3 ? "User" : "Banned";
                    User user = getUserForTable(u);

                    switch (searchUserAccessesCmb.getValue()) {
                        case "User ID":
                            if(Integer.toString(u.getUserId()).contains(searchText)) {
                                if(user != null) {
                                    obList.add(user);
                                }
                            }
                            break;

                        case "Name":
                            if(Objects.requireNonNull(u.getUserName().toLowerCase()).
                                    contains(searchText.toLowerCase())) {
                                if(user != null) {
                                    obList.add(user);
                                }
                            }
                            break;

                        case "Email":
                            if(Objects.requireNonNull(u.getEmail()).contains(searchText)) {
                                if(user != null) {
                                    obList.add(user);
                                }
                            }
                            break;

                        case "Roll":
                            if(title.toLowerCase().contains(searchText.toLowerCase())) {
                                if(user != null) {
                                    obList.add(user);
                                }
                            }
                            break;

                        default:
                            if(Integer.toString(u.getUserId()).contains(searchText) ||
                                    Objects.requireNonNull(u.getUserName().toLowerCase()).
                                            contains(searchText.toLowerCase()) ||
                                    Objects.requireNonNull(u.getEmail()).contains(searchText) ||
                                    title.toLowerCase().contains(searchText.toLowerCase())) {
                                if(user != null) {
                                    obList.add(user);
                                }
                            }
                            break;
                    }
                }
            }

            userTbl.setItems(obList);
        }
    }

    private User getUserForTable(model.User u) {
        if(super.getUserRoll() == 2 && u.getTitle() >= 2) {
            return new User(u.getUserId(), u.getUserName(), u.getEmail(),
                    u.getTitle() == 2 ? "Admin" : u.getTitle() == 3 ? "User": "Banned",
                    getBannedUnbannedButton(u.getUserId()));

        } else if (super.getUserRoll() == 1) {
            return new User(u.getUserId(), u.getUserName(), u.getEmail(),
                    u.getTitle() == 1 ? "Super Admin" : u.getTitle() == 2 ? "Admin" :
                            u.getTitle() == 3 ? "User": "Banned",
                    getBannedUnbannedButton(u.getUserId()));
        }

        return null;
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
                                alert(Alert.AlertType.WARNING, "WARNING", "Can't Banned",
                                        "This user can not Banned");
                            }

                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                    ex.getMessage());
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
                                alert(Alert.AlertType.WARNING, "WARNING", "Can't Unbanned",
                                        "This user can not Unbanned");
                            }

                        } catch (SQLException ex) {
                            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error",
                                    ex.getMessage());
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
                String name = nameTxt.getText().trim().toLowerCase();
                String email = emailTxt.getText().trim().toLowerCase();
                String password = passwordTxt.getText().trim();
                String conformPassword = conformPasswordTxt.getText().trim();
                String roll = userAccessesCmb.getValue();
                int rollNumber = roll.equalsIgnoreCase("admin") ? 2 : 3;

                if(!name.isEmpty() && !dbConnection.userNameIsAvailable(name) ||
                        addOrUpdateBtn.getText().equalsIgnoreCase("update") &&
                                !dbConnection.userNameIsAvailable(name, selectedUserId)) {
                    if(!email.isEmpty() &&!dbConnection.mailIsAvailable(email) ||
                            addOrUpdateBtn.getText().equalsIgnoreCase("update") &&
                                    !dbConnection.mailIsAvailable(email, selectedUserId)) {
                        if(!password.isEmpty() && !conformPassword.isEmpty() && password.equals(conformPassword) ||
                                addOrUpdateBtn.getText().equalsIgnoreCase("update") &&
                                        password.equals(conformPassword)) {
                            if(!roll.equalsIgnoreCase("User Roll")) {
                                if(addOrUpdateBtn.getText().equalsIgnoreCase("add")) {

                                    if(dbConnection.addUser(new model.User(name, email, password, rollNumber))){

                                        userTableDataCount +=1;
                                        if(userTableDataCount < 25 && userTableDataCount > 0) {
                                            nextUsersTableBtn.setDisable(true);
                                        }
                                        users = dbConnection.getUsersTable(loadedRowCountUsers);
                                        previewUsersTableBtn.setDisable(true);

                                        resetOnAction(actionEvent);
                                        refreshOnAction(actionEvent);

                                        alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                                "Successfully Added User",
                                                "Successfully added user into database");

                                    } else {
                                        alert(Alert.AlertType.WARNING, "WARNING",
                                                "User Didn't Added", "Try again");
                                    }

                                } else if (addOrUpdateBtn.getText().equalsIgnoreCase("update")) {

                                    if(dbConnection.updateAllUserDetails(new model.User(selectedUserId, name, email,
                                            password.isEmpty() ? dbConnection.getUserPassword(selectedUserId) :
                                                    password, rollNumber))){

                                        users.forEach((u) -> {
                                            if(u.getUserId() == selectedUserId) {
                                                u.setUserName(name);
                                                u.setEmail(email);
                                                u.setTitle(rollNumber);
                                            }
                                        });

                                        resetOnAction(actionEvent);
                                        setDataIntoUsersTable();

                                        alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                                "Successfully Update User",
                                                "Successfully update user in database");

                                    } else {
                                        alert(Alert.AlertType.WARNING, "WARNING",
                                                "User Didn't Update", "Try again");
                                    }
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
                                "This email is already use or empty try again with another email");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "WARNING", "User Name Can't Use",
                            "This user name is already use or empty try again with another name");
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
        selectedUserId = -1;
        nameTxt.clear();
        emailTxt.clear();
        passwordTxt.clear();
        conformPasswordTxt.clear();
        userAccessesCmb.setValue("User Roll");
        addOrUpdateBtn.setText("Add User");
        addOrUpdateBtn.setStyle("-fx-background-color:  #1dd1a1;");
        setDataIntoUsersTable();
    }

    public void refreshOnAction(ActionEvent actionEvent) {
        try {
            userTableDataCount = dbConnection.getTableRowCount(TableTypes.USER_TABLE);
            loadedRowCountUsers = 0;

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

    public void previewUsersTableOnAction(ActionEvent actionEvent) {
        try {
            previewUsersTableBtn.setDisable(true);
            if(users != null && !users.isEmpty()) {
                if((loadedRowCountUsers - 25) >= 0) {
                    loadedRowCountUsers -= 25;
                    users = dbConnection.getUsersTable(loadedRowCountUsers);
                    setDataIntoUsersTable();
                    nextUsersTableBtn.setDisable(false);

                    if((loadedRowCountUsers - 25) >= 0) {
                        previewUsersTableBtn.setDisable(false);
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

    public void nextUsersTableOnAction(ActionEvent actionEvent) {
        try {
            nextUsersTableBtn.setDisable(true);
            if(users != null && !users.isEmpty()) {
                if((loadedRowCountUsers + 25) < userTableDataCount) {
                    loadedRowCountUsers += 25;
                    users = dbConnection.getUsersTable(loadedRowCountUsers);
                    setDataIntoUsersTable();
                    previewUsersTableBtn.setDisable(false);

                    if((loadedRowCountUsers + 25) < userTableDataCount) {
                        nextUsersTableBtn.setDisable(false);
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
}
