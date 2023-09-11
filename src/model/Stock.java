package model;

public class Stock {
    private int stockId;
    private int itemId;
    private int quantity;
    private double price;
    private double sellingPrice;


    public Stock() {}

    public Stock(int stockId, int itemId, int quantity, double price, double sellingPrice) {
        this.stockId = stockId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.sellingPrice = sellingPrice;
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
}
