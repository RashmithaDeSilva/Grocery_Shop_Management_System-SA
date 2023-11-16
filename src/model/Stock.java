package model;

import java.sql.Date;
import java.sql.Time;

public class Stock {
    private int stockId;
    private int userId;
    private int itemId;
    private int quantity;
    private int refillQuantity;
    private double price;
    private double sellingPrice;
    private Date lastRefillDate;
    private Time lastRefillTime;


    public Stock() {}

    public Stock(int stockId, int itemId, int quantity, double price) {
        this.stockId = stockId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public Stock(int stockId, int itemId, int quantity, double price, double sellingPrice) {
        this.stockId = stockId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.sellingPrice = sellingPrice;
    }

    public Stock(int stockId, int userId, int itemId, int quantity, int refillQuantity,
                 double price, double sellingPrice, Date lastRefillDate, Time lastRefillTime) {
        this.stockId = stockId;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.refillQuantity = refillQuantity;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.lastRefillDate = lastRefillDate;
        this.lastRefillTime = lastRefillTime;
    }


    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(int refillQuantity) {
        this.refillQuantity = refillQuantity;
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


    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", refillQuantity=" + refillQuantity +
                ", price=" + price +
                ", sellingPrice=" + sellingPrice +
                ", lastRefillDate=" + lastRefillDate +
                ", lastRefillTime=" + lastRefillTime +
                '}';
    }
}
