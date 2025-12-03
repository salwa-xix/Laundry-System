package CPIT251;

public class Notification {

    private String customerMessage;
    private String employeeMessage;

    public void prepare(Order order) {
        String status = order.getStatus();
        int OrderID = order.getOrderID();

        if (status.equalsIgnoreCase("Created")) {
            customerMessage = "Your order #" + OrderID+ " has been created.";
        } else if (status.equalsIgnoreCase("In progress")) {
            customerMessage = "Your order #" + OrderID + " is now in progress.";
        } else if (status.equalsIgnoreCase("Ready")) {
            customerMessage= "Your order #" + OrderID+ " is ready.";
        } else if (status.equalsIgnoreCase("Picked up")) {
            customerMessage = "Your order #" + OrderID + " has been Picked up.";
        }

        employeeMessage = "Order #" + OrderID + " changed to: " + status;
    }


    public String getCustomerMessage() {
        return customerMessage;
    }

    public String getEmployeeMessage() {
        return employeeMessage;
    }
}
