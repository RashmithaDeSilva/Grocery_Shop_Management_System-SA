package model;

import model.staticType.IncomeOrExpenseLogTypes;
import model.staticType.LogTypes;

import java.sql.Date;
import java.sql.Time;

public class Log {
    private int userId;
    private String logName;
    private LogTypes logType;
    private Date date;
    private Time time;
    private double amount;
    private IncomeOrExpenseLogTypes incomeOrExpensesType;


    public Log() {
    }

    public Log(int userId, String logName, LogTypes logType, Date date, Time time, double amount,
               IncomeOrExpenseLogTypes incomeOrExpensesType) {
        this.userId = userId;
        this.logName = logName;
        this.logType = logType;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.incomeOrExpensesType = incomeOrExpensesType;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LogTypes getLogType() {
        return logType;
    }

    public void setLogType(LogTypes logType) {
        this.logType = logType;
    }

    public IncomeOrExpenseLogTypes getIncomeOrExpensesType() {
        return incomeOrExpensesType;
    }

    public void setIncomeOrExpensesType(IncomeOrExpenseLogTypes incomeOrExpensesType) {
        this.incomeOrExpensesType = incomeOrExpensesType;
    }
}
