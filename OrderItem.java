package org.example;


public class OrderItem  {

    private int itemNumber;
    private int itemType;
    private int serviceType;
    private int quantity;
    private double itemPrice;

    public OrderItem(int itemNumber, int itemType, int serviceType, int quantity, double itemPrice) {
        this.itemNumber = itemNumber;
        this.itemType = itemType;
        this.serviceType = serviceType;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public int getItemNumber() {

        return itemNumber;
    }

    public int getItemType() {

        return itemType;
    }

    public int getServiceType() {

        return serviceType;
    }

    public int getQuantity() {

        return quantity;
    }

    public double getItemPrice() {

        return itemPrice;
    }

    public void setItemType(int itemType) {

        this.itemType = itemType;
    }

    public void setServiceType(int serviceType) {

        this.serviceType = serviceType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
