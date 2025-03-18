package item;

import java.util.Date;

public class SalesItem {
    private int itemId;
    private String itemName;
    private int quantity;
    private double totalPrice;
    private Date transactionDate;

    // Constructors
    public SalesItem() {
        // Default constructor
    }

    public SalesItem(int itemId, String itemName, int quantity, double totalPrice, Date transactionDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}

