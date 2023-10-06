package model;

import java.sql.Date;
import java.sql.Time;

public class Log {
    private int userId;
    private String logName;
    private int logType;
    private Date date;
    private Time time;
    private double amount;
    private boolean incomeOrExpenses;


    public Log() {
    }

    public Log(int userId, String logName, int logType, Date date, Time time, double amount, boolean incomeOrExpenses) {
        this.userId = userId;
        this.logName = logName;
        this.logType = logType;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.incomeOrExpenses = incomeOrExpenses;
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

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public boolean getIncomeOrExpenses() {
        return incomeOrExpenses;
    }

    public void setIncomeOrExpenses(boolean incomeOrExpenses) {
        this.incomeOrExpenses = incomeOrExpenses;
    }
}
