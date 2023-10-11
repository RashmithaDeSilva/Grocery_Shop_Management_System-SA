package model.tableRows.sellWindow;

import javafx.scene.control.Button;

public class InvoiceItem {
    private int itemId;
    private int stockid;
    private String itemName;
    private int quantity;
    private double discount;
    private double price;
    private Button delete;

    public InvoiceItem() {}

    public InvoiceItem(int itemId, int stockId, String itemName, int quantity, double discount, double price, Button delete) {
        this.itemId = itemId;
        this.stockid = stockId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.discount = discount;
        this.price = price;
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
        return stockid;
    }

    public void setStockId(int stockid) {
        this.stockid = stockid;
    }
}
