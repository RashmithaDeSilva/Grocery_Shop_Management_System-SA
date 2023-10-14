package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Window;

import java.io.IOException;


public class UserManagementFormController extends Window {
    public AnchorPane contextUserManagement;
    public TextField nameTxt;
    public TextField emailTxt;
    public PasswordField passwordTxt;
    public PasswordField conformPasswordTxt;
    public ComboBox userAccessesCmb;
    public TableView userTbl;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn emailCol;
    public TableColumn rollCol;
    public TableColumn deleteCol;
    public TextField searchTxt;
    public ComboBox searchUserAccessesCmb;

    public void initialize() {
        super.context = contextUserManagement;
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
}
