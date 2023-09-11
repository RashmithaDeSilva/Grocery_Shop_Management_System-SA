package model;

import java.util.ArrayList;

public class Item {
    private int itemId;
    private String itemName;
    private ArrayList<Stock> stocks;


    public Item() {
    }

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public Item(int itemId, String itemName, ArrayList<Stock> stocks) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.stocks = stocks;
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

    public void addStock(Stock stock) {
        if(this.stocks != null) {
            this.stocks.add(stock);

        } else {
            this.stocks = new ArrayList<>();
            this.stocks.add(stock);
        }
    }

}
