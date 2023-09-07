package model;

public class Item {
    private int itemId;
    private String itemName;
    private double price;
    private double sellingPrice;


    public Item() {
    }

    public Item(int itemId, String itemName, double price, double sellingPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.sellingPrice = sellingPrice;
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
