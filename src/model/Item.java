package model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Item {
    private int itemId;
    private String itemName;
    private int userId;
    private Date date;
    private Time time;
    private ArrayList<Stock> stocks;


    public Item() {
    }

    public Item(int itemId) {
        this.itemId = itemId;
    }

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public Item(int itemId, String itemName, int userId, Date date, Time time) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.date = date;
        this.time = time;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ArrayList<Stock> getStocks() {return stocks;}

    public void setStocks(ArrayList<Stock> stocks) {this.stocks = stocks;}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void addStock(Stock stock) {
        if(this.stocks != null) {
            this.stocks.add(stock);

        } else {
            this.stocks = new ArrayList<>();
            this.stocks.add(stock);
        }
    }
}
