package model;

public class Sell {
    private String sellId;
    private String billNumber;
    private int itemId;
    private int stockId;
    private double discount;
    private double price;
    private double profit;
    private int quantity;
    private boolean edited;
    private boolean returns;


    public Sell() {}

    public Sell(String sellId, String billNumber, int itemId, int stockId, double discount, double price,
                double profit, int quantity, boolean edited, boolean returns) {
        this.sellId = sellId;
        this.billNumber = billNumber;
        this.itemId = itemId;
        this.stockId = stockId;
        this.discount = discount;
        this.price = price;
        this.profit = profit;
        this.quantity = quantity;
        this.edited = edited;
        this.returns = returns;
    }


    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public void setPrice(double sellAmount) {
        this.price = sellAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public boolean isReturns() {
        return returns;
    }

    public void setReturns(boolean returns) {
        this.returns = returns;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }


    @Override
    public String toString() {
        return "Sell{" +
                "sellId='" + sellId + '\'' +
                ", billNumber='" + billNumber + '\'' +
                ", itemId=" + itemId +
                ", stockId=" + stockId +
                ", discount=" + discount +
                ", price=" + price +
                ", profit=" + profit +
                ", quantity=" + quantity +
                ", edited=" + edited +
                ", returns=" + returns +
                '}';
    }
}
