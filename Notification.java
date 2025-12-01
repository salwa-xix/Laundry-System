package org.example;

public class Notification {

    private String customerMsg;
    private String employeeMsg;

    public void prepare(Order order) {
        String status = order.getStatus();
        int OrderID = order.getOrderID();

        if (status.equalsIgnoreCase("Created")) {
            customerMsg = "Your order #" + OrderID+ " has been created.";
        } else if (status.equalsIgnoreCase("In progress")) {
            customerMsg = "Your order #" + OrderID + " is now in progress.";
        } else if (status.equalsIgnoreCase("Ready")) {
            customerMsg = "Your order #" + OrderID+ " is ready for pickup.";
        } else if (status.equalsIgnoreCase("Cancelled")) {
            customerMsg = "Your order #" + OrderID + " has been cancelled.";
        }

        employeeMsg = "Order #" +OrderID + " changed to: " + status;
    }

    public String getCustomerMessage() {
        return customerMsg;
    }

    public String getEmployeeMessage() {
        return employeeMsg;
    }

}