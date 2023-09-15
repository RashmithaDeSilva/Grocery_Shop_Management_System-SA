package model.tableRows;

import javafx.scene.control.Button;

import java.sql.Date;
import java.sql.Time;

public class Stock {
    private int stockId;
    private String userName;
    private int itemId;
    private int quantity;
    private int refillQuantity;
    private double price;
    private double sellingPrice;
    private Date lastRefillDate;
    private Time lastRefillTime;
    private Button delete;


    public Stock() {}

    public Stock(int stockId, String userName, int itemId, int quantity, int refillQuantity, double price,
                 double sellingPrice, Date lastRefillDate, Time lastRefillTime, Button delete) {
        this.stockId = stockId;
        this.userName = userName;
        this.itemId = itemId;
        this.quantity = quantity;
        this.refillQuantity = refillQuantity;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.lastRefillDate = lastRefillDate;
        this.lastRefillTime = lastRefillTime;
        this.delete = delete;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(int refillQuantity) {
        this.refillQuantity = refillQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Date getLastRefillDate() {
        return lastRefillDate;
    }

    public void setLastRefillDate(Date lastRefillDate) {
        this.lastRefillDate = lastRefillDate;
    }

    public Time getLastRefillTime() {
        return lastRefillTime;
    }

    public void setLastRefillTime(Time lastRefillTime) {
        this.lastRefillTime = lastRefillTime;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
