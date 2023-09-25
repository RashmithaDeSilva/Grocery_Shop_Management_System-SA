package model.tableRows.stockWindow;

public class StockRefill implements RefillAndItem{
    private int stockId;
    private int itemId;
    private String itemName;
    private int quantity;
    private double price;


    public StockRefill() {
    }

    public StockRefill(int stockId, int itemId, String itemName, int quantity, double price) {
        this.stockId = stockId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
