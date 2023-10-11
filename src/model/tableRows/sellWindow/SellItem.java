package model.tableRows.sellWindow;

public class SellItem {
    private int itemId;
    private int stockId;
    private String itemName;

    public SellItem() {}

    public SellItem(int itemId, int stockId, String itemName) {
        this.itemId = itemId;
        this.stockId = stockId;
        this.itemName = itemName;
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

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
