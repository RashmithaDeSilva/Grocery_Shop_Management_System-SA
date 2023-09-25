package model.tableRows.stockWindow;

public class Item implements RefillAndItem {
    private int itemId;
    private String itemName;


    public Item() {}

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
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
}
