package model.tableRows.sellWindow;

import javafx.scene.control.Button;

public class InvoiceItem {
    private int itemId;
    private int stockId;
    private String itemName;
    private int quantity;
    private double discount;
    private double price;
    private double sellingPrice;
    private Button delete;

    public InvoiceItem() {}

    public InvoiceItem(int itemId, int stockId, String itemName, int quantity, double discount,
                       double price, double sellingPrice, Button delete) {
        this.itemId = itemId;
        this.stockId = stockId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.discount = discount;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.delete = delete;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockid) {
        this.stockId = stockid;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
