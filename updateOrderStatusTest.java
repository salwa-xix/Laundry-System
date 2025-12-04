package CPIT251;

import static org.junit.Assert.*;
import org.junit.*;

public class updateOrderStatusTest {

    @Test
    public void testStatusCreated(){
        String customerPhone = "0500000000";
        Order order = new Order(customerPhone);
        int id = order.getOrderID();

        String actualStatus = order.getStatus();
        String expectedStatus = "Created";
        assertEquals(expectedStatus, actualStatus);

        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " has been created.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testStatusInProgress(){
        String customerPhone = "0500000000";
        Order order = new Order(customerPhone);
        int id = order.getOrderID();

        order.updateOrderStatus("In progress");
        String actualStatus = order.getStatus();
        String expectedStatus = "In progress";
        assertEquals(expectedStatus, actualStatus);

        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " is now in progress.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testStatusReady(){
        String customerPhone = "0500000000";
        Order order = new Order(customerPhone);
        int id = order.getOrderID();

        order.updateOrderStatus("Ready");
        String actualStatus = order.getStatus();
        String expectedStatus = "Ready";
        assertEquals(expectedStatus, actualStatus);

        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " is ready.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testStatusPickedUp(){
        Order order = new Order("0500000000");
        int id = order.getOrderID();

        order.updateOrderStatus("Picked up");
        String actualStatus = order.getStatus();
        String expectedStatus = "Picked up";
        assertEquals(expectedStatus, actualStatus);

        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " has been Picked up.";
        assertEquals(expectedMessage, actualMessage);
    }
}
