package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Log;
import model.Window;
import model.staticType.MoneyType;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;

import static model.staticType.IncomeDayTypes.*;
import static model.staticType.IncomeOrExpensesTypes.*;
import static model.staticType.MoneyType.*;


public class LockerFormController extends Window {
    public AnchorPane contextLocker;
    public TextField addMoneyTxt;
    public TextField forWhatTxt;
    public TextField withdrawTxt;
    public TextField stockValueTxt;
    public TextField lockerMoneyTxt;
    public TextField todayIncomeTxt;
    public TextField lastWeekIncomeTxt;
    public TextField lastMonthIncomeTxt;
    public CheckBox payBillChBx;
    public ComboBox<String> todayCmb;
    public ComboBox<String> lastWeekCmb;
    public ComboBox<String> lastMonthCmb;
    public ComboBox<String> thisWeekCmb;
    public ComboBox<String> thisMonthCmb;
    public ComboBox<String> yesterdayCmb;
    public TextField yesterdayIncomeTxt;
    public TextField thisWeekIncomeTxt;
    public TextField thisMonthIncomeTxt;


    public void initialize() {
        super.context = contextLocker;
        ObservableList<String> comboBoxSelections =
                FXCollections.observableArrayList("All", "Income", "Expenses");

        todayCmb.setItems(comboBoxSelections);
        todayCmb.setValue(comboBoxSelections.get(0));

        yesterdayCmb.setItems(comboBoxSelections);
        yesterdayCmb.setValue(comboBoxSelections.get(0));

        thisWeekCmb.setItems(comboBoxSelections);
        thisWeekCmb.setValue(comboBoxSelections.get(0));

        lastWeekCmb.setItems(comboBoxSelections);
        lastWeekCmb.setValue(comboBoxSelections.get(0));

        thisMonthCmb.setItems(comboBoxSelections);
        thisMonthCmb.setValue(comboBoxSelections.get(0));

        lastMonthCmb.setItems(comboBoxSelections);
        lastMonthCmb.setValue(comboBoxSelections.get(0));

        try {
            stockValueTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getAllStockadeValue()) + " Rs");
            lockerMoneyTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getLockerMoney()) + " Rs");

            todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(TODAY, ALL)) + " Rs");
            yesterdayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(YESTERDAY, ALL)) + " Rs");

            thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(THIS_WEEK, ALL)) + " Rs");
            lastWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(LAST_WEEK, ALL)) + " Rs");

            thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(THIS_MONTH, ALL)) + " Rs");
            lastMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                    format(dbConnection.getIncome(LAST_MONTH, ALL)) + " Rs");

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }

        todayCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(TODAY, ALL)) + " Rs");
                            break;

                        case "Income":
                            todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(TODAY, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(TODAY, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        yesterdayCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            yesterdayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(YESTERDAY, ALL)) + " Rs");
                            break;

                        case "Income":
                            yesterdayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(YESTERDAY, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            yesterdayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(YESTERDAY, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        thisWeekCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_WEEK, ALL)) + " Rs");
                            break;

                        case "Income":
                            thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_WEEK, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_WEEK, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        lastWeekCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            lastWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_WEEK, ALL)) + " Rs");
                            break;

                        case "Income":
                            lastWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_WEEK, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            lastWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_WEEK, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        thisMonthCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_MONTH, ALL)) + " Rs");
                            break;

                        case "Income":
                            thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_MONTH, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(THIS_MONTH, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });

        lastMonthCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if(newValue != null && !newValue.isEmpty()) {
                    switch (newValue) {
                        case "All":
                            lastMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_MONTH, ALL)) + " Rs");
                            break;

                        case "Income":
                            lastMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_MONTH, INCOME)) + " Rs");
                            break;

                        case "Expenses":
                            lastMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                                    format(dbConnection.getIncome(LAST_MONTH, EXPENSES)) + " Rs");
                            break;
                    }
                }

            } catch (SQLException e) {
                alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
            }
        });
    }

    public void withdrawMoneyOnAction(ActionEvent actionEvent) {
        try {
            double amount = Double.parseDouble(withdrawTxt.getText());
            double lockerMoney = Double.parseDouble(String.join("",
                    lockerMoneyTxt.getText().split(" ")[0].split(",")));

            if(forWhatTxt != null && !forWhatTxt.getText().isEmpty()) {
                if(withdrawTxt != null && !withdrawTxt.getText().isEmpty()) {
                    if(amount > 0) {
                        if(amount <= lockerMoney) {
                            if(dbConnection.addLog(new Log(super.getUserId(), "Withdraw money Rs: " + amount
                                    + " ( " + forWhatTxt.getText().trim().toLowerCase() + " )",
                                    3, new Date(Calendar.getInstance().getTime().getTime()),
                                    new Time(Calendar.getInstance().getTime().getTime()), amount,
                                    payBillChBx.isSelected() ? 5 : 4))) {

                                setMoney(WITHDRAW, amount);
                                forWhatTxt.clear();
                                payBillChBx.setSelected(false);
                                withdrawTxt.clear();

                                alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successfully Withdraw",
                                        "Successfully withdraw money in locker");

                            } else {
                                alert(Alert.AlertType.WARNING, "WARNING", "Database Connection Error",
                                        "Try again");
                            }

                        } else {
                            alert(Alert.AlertType.WARNING, "WARNING", "There have No Money",
                                    "There have no money enough");
                        }

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Invalid Input",
                                "Try again");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "WARNING", "Invalid Input",
                            "Try again");
                }

            } else {
                alert(Alert.AlertType.WARNING, "WARNING", "Enter Input",
                        "Enter withdraw reason");
            }

        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void lockerLogOnAction(ActionEvent actionEvent) {
    }

    private void setMoney(MoneyType moneyType, double amount) {

        double lockerMoney = Double.parseDouble(String.join("",
                lockerMoneyTxt.getText().split(" ")[0].split(",")));
        double todayIncome = Double.parseDouble(String.join("",
                todayIncomeTxt.getText().split(" ")[0].split(",")));
        double thisWeekIncome = Double.parseDouble(String.join("",
                thisWeekIncomeTxt.getText().split(" ")[0].split(",")));
        double thisMonthIncome = Double.parseDouble(String.join("",
                thisMonthIncomeTxt.getText().split(" ")[0].split(",")));

        switch (moneyType) {
            case ADD_INCOME:
                lockerMoneyTxt.setText(new DecimalFormat("#,##0.00").
                        format(lockerMoney + amount) + " Rs");
                todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(todayIncome + amount) + " Rs");
                thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(thisWeekIncome + amount) + " Rs");
                thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(thisMonthIncome + amount) + " Rs");
                break;

            case WITHDRAW:
                lockerMoneyTxt.setText(new DecimalFormat("#,##0.00").
                        format(lockerMoney - amount) + " Rs");
                todayIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(todayIncome - amount) + " Rs");
                thisWeekIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(thisWeekIncome - amount) + " Rs");
                thisMonthIncomeTxt.setText(new DecimalFormat("#,##0.00").
                        format(thisMonthIncome - amount) + " Rs");
                break;
        }
    }

    public void addMoneyOnAction(ActionEvent actionEvent) {
        try {
            if(addMoneyTxt != null && !addMoneyTxt.getText().isEmpty()) {
                double amount = Double.parseDouble(addMoneyTxt.getText().trim());

                if(amount > 0) {
                    if(dbConnection.addLog(new Log(super.getUserId(), "Add money Rs: " + amount,
                            3, new Date(Calendar.getInstance().getTime().getTime()),
                            new Time(Calendar.getInstance().getTime().getTime()), amount, 2))) {

                        setMoney(ADD_INCOME, amount);
                        addMoneyTxt.clear();

                        alert(Alert.AlertType.INFORMATION, "INFORMATION", "Successfully Added",
                                "Successfully added money into locker");

                    } else {
                        alert(Alert.AlertType.WARNING, "WARNING", "Database Connection Error",
                                "Try again");
                    }

                } else {
                    alert(Alert.AlertType.WARNING, "WARNING", "Invalid Input", "Try again");
                }

            } else {
                alert(Alert.AlertType.WARNING, "WARNING", "Enter Input", "Try again");
            }

        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());

        } catch (SQLException e) {
            alert(Alert.AlertType.ERROR, "ERROR", "Database Connection Error", e.getMessage());
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

}
