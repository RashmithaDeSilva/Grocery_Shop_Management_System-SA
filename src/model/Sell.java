package model;

import java.sql.Date;
import java.sql.Time;

public class Sell {
    private int sellId;
    private int billNumber;
    private int userId;
    private int itemId;
    private Date sellDate;
    private Time sellTime;
    private double discount;
    private double price;
    private int quantity;
    private boolean edited;


    public Sell() {
    }

    public Sell(int sellId, int billNumber, int userId, int itemId, Date sellDate, Time sellTime,
                double discount, double price, int quantity, boolean edited) {
        this.sellId = sellId;
        this.billNumber = billNumber;
        this.userId = userId;
        this.itemId = itemId;
        this.sellDate = sellDate;
        this.sellTime = sellTime;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
        this.edited = edited;
    }


    public int getSellId() {
        return sellId;
    }

    public void setSellId(int sellId) {
        this.sellId = sellId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public Time getSellTime() {
        return sellTime;
    }

    public void setSellTime(Time sellTime) {
        this.sellTime = sellTime;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }
}
