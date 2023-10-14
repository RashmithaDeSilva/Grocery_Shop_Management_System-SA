package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import model.Window;

import java.sql.SQLException;


public class UserManagementFormController extends Window {
    public AnchorPane contextUserManagement;

    public void initialize() {
        super.context = contextUserManagement;
    }

    public void backOnAction(ActionEvent actionEvent) {
    }

    public void addUsersOnAction(ActionEvent actionEvent) {
    }
}
