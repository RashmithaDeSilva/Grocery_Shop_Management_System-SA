package model;

import java.sql.Date;
import java.sql.Time;

public class Bill {
    private int bill_number;
    private int userId;
    private double price;
    private double discount;
    private Date date;
    private Time time;


    public Bill() {
    }

    public Bill(int bill_number, int userId, double price, double discount, Date date, Time time) {
        this.bill_number = bill_number;
        this.userId = userId;
        this.price = price;
        this.discount = discount;
        this.date = date;
        this.time = time;
    }


    public int getBill_number() {
        return bill_number;
    }

    public void setBill_number(int bill_number) {
        this.bill_number = bill_number;
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
}
