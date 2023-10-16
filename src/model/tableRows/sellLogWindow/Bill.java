package model.tableRows.sellLogWindow;

import javafx.scene.control.Button;

import java.sql.Date;
import java.sql.Time;

public class Bill {
    private int bill_number;
    private String userName;
    private double discount;
    private double price;
    private Date date;
    private Time time;
    private Button btn;


    public Bill() {
    }

    public Bill(int bill_number, String userName, double discount, double price, Date date, Time time, Button btn) {
        this.bill_number = bill_number;
        this.userName = userName;
        this.price = price;
        this.discount = discount;
        this.date = date;
        this.time = time;
        this.btn = btn;
    }


    public int getBill_number() {
        return bill_number;
    }

    public void setBill_number(int bill_number) {
        this.bill_number = bill_number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
