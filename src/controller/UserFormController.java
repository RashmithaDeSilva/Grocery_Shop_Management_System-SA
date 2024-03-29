package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.User;
import model.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class UserFormController extends Window {
    public AnchorPane contextUserProfile;
    public TextField mailTxt;
    public TextField nameTxt;
    public PasswordField newPasswordTxt;
    public PasswordField conformPasswordTxt;


    public void initialize() {
        super.context = contextUserProfile;

        try {
            nameTxt.setText(dbConnection.getUserName(super.getUserId()));
            mailTxt.setText(dbConnection.getUserEmail(super.getUserId()));

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        nameTxt.setEditable(super.getUserRoll() == 1 || super.getUserRoll() == 2);
        mailTxt.setEditable(super.getUserRoll() == 1 || super.getUserRoll() == 2);
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) throws IOException {
        try {
            if(newPasswordTxt.getText().trim().equals(conformPasswordTxt.getText().trim())) {
                String password = newPasswordTxt.getText().trim().isEmpty() ||
                        conformPasswordTxt.getText().trim().isEmpty() ?
                        dbConnection.getUserPassword(super.getUserId()) : newPasswordTxt.getText().trim();

                if(super.getUserRoll() == 1 || super.getUserRoll() == 2) {
                    if(!dbConnection.userNameIsAvailable(nameTxt.getText().trim().toLowerCase(), super.getUserId())) {
                        if(!dbConnection.mailIsAvailable(nameTxt.getText().trim().toLowerCase(), super.getUserId())) {

                            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationAlert.initOwner(this.contextUserProfile.getScene().getWindow());
                            confirmationAlert.setTitle("CONFIRMATION");
                            confirmationAlert.setHeaderText("Are you sure you want to continue?");
                            confirmationAlert.setContentText("Click OK to proceed or Cancel to abort.");

                            Optional<ButtonType> result = confirmationAlert.showAndWait();

                            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                                if(dbConnection.updateUserDetails(new User(super.getUserId(),
                                        nameTxt.getText().trim().toLowerCase(), mailTxt.getText().trim().toLowerCase(),
                                        password))) {

                                    setUI("LoginForm");
                                    alert(Alert.AlertType.INFORMATION, "INFORMATION",
                                            "Successfully Update",
                                            "Your new user details is update");

                                } else {
                                    alert(Alert.AlertType.WARNING, "WARNING", "Database Connection Error",
                                            "User details is not updated try again");
                                }
                            }

                        } else {
                            alert(Alert.AlertType.WARNING, "WARNING", "Email Already Exist",
                                    "This email is already used try again with another email");
                        }

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "User Name Already Exist",
                                "This user name is already used try again with another user name");
                    }

                } else if(super.getUserRoll() == 3) {
                    if (dbConnection.changeUserPassword(super.getUserId(), password)) {
                        setUI("LoginForm");
                        alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successfully Update Password",
                                "Your new password is update");

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Database Connection Error",
                                "Password is not updated try again");
                    }
                }

            } else {
                alert(Alert.AlertType.WARNING, "WARNING", "Password Are Not Mach",
                        "This new password and conform password is not mach try again");
            }

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void resetOnAction(ActionEvent actionEvent) {
        try {
            nameTxt.setText(dbConnection.getUserName(super.getUserId()));
            mailTxt.setText(dbConnection.getUserEmail(super.getUserId()));

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void userManagementOnAction(ActionEvent actionEvent) throws IOException {
        if(super.getUserRoll() == 1 || super.getUserRoll() == 2){
            setUI("UserManagementForm");

        } else {
            alert(Alert.AlertType.WARNING, "WARNING", "You Can't Accuses",
                    "Admins accuses only");
        }
    }
}
