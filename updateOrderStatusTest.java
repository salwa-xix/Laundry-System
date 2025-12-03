package CPIT251;


import static org.junit.Assert.*;
import org.junit.*;

public class updateOrderStatusTest {


    @Test
    public void testStatusCreated(){
        Order order = new Order();
        int id = order.getOrderID();

        // check status update is correct
        String actualStatus = order.getStatus();
        String expectedStatus = "Created";
        assertEquals(expectedStatus, actualStatus);

        // check status update notify is correct
        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " has been created.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void testStatusInProgress(){
        Order order = new Order();
        int id = order.getOrderID();

        // check status update is correct
        order.updateOrderStatus("In progress");
        String actualStatus = order.getStatus();
        String expectedStatus = "In progress";
        assertEquals(expectedStatus, actualStatus);

        // check status update notify is correct
        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " is now in progress.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void testStatusReady(){
        Order order = new Order();
        int id = order.getOrderID();

        // check status update is correct
        order.updateOrderStatus("Ready");
        String actualStatus = order.getStatus();
        String expectedStatus = "Ready";
        assertEquals(expectedStatus, actualStatus);

        // check status update notify is correct
        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " is ready.";
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void testStatusPickedUp(){
        Order order = new Order();
        int id = order.getOrderID();

        // check status update is correct
        order.updateOrderStatus("Picked up");
        String actualStatus = order.getStatus();
        String expectedStatus = "Picked up";
        assertEquals(expectedStatus, actualStatus);

        // check status update notify is correct
        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " has been Picked up.";
        assertEquals(expectedMessage, actualMessage);

    }
}