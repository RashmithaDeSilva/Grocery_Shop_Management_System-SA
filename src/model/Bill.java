package model;

import java.sql.Date;
import java.sql.Time;

public class Bill {
    private String billNumber;
    private int userId;
    private double price;
    private double discount;
    private Date date;
    private Time time;
    private boolean returns;


    public Bill() {
    }

    public Bill(String billNumber, int userId, double price, double discount, Date date, Time time, boolean returns) {
        this.billNumber = billNumber;
        this.userId = userId;
        this.price = price;
        this.discount = discount;
        this.date = date;
        this.time = time;
        this.returns = returns;
    }


    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int bill_number) {
        this.billNumber = billNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public boolean isReturns() {
        return returns;
    }

    public void setReturns(boolean returns) {
        this.returns = returns;
    }
}
