package model.tableRows.sellLogWindow;

import javafx.scene.control.Button;

public class Sell {
    private String sellId;
    private String  itemName;
    private int quantity;
    private double discount;
    private double price;
    private Button btn;


    public Sell() {
    }

    public Sell(String sellId, String itemName, int quantity, double discount, double price, Button btn) {
        this.sellId = sellId;
        this.itemName = itemName;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
        this.btn = btn;
    }


    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
