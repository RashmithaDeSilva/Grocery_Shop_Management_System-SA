package model.tableRows;

import javafx.scene.control.Button;

public class Item {
    private int id;
    private String name;
    private double price;
    private double sellingPrice;
    private Button deleteBtn;

    public Item() {}

    public Item(int id, String name, double price, double sellingPrice, Button deleteBtn) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.deleteBtn = deleteBtn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(Button deleteBtn) {
        this.deleteBtn = deleteBtn;
    }
}
