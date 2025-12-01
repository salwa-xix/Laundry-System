package org.example;

public class OrderInfo {
    public int orderId;
    public double totalPrice;
    public String status;

    public OrderInfo(int orderId, double totalPrice, String status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}