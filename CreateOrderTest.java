package CPIT251;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CreateOrderTest {

    private Order order;

    @Before
    public void setUp() {
        Order.getAllOrders().clear();
        String customerPhone = "0500000000";
        order = new Order(customerPhone);
    }

    @Test
    public void testCreateOrderDefaultValues() {
        int id = order.getOrderID();

        // check default values for first new order
        assertEquals("Created", order.getStatus());

        assertEquals(0, order.getItemsCount());

        assertEquals("0.0", String.valueOf(order.getTotalPrice()));

        assertEquals(1, Order.getAllOrders().size());

        // Check that the default notification message for first new order
        Notification notification = order.getNotification();
        String actualMessage = notification.getCustomerMessage();
        String expectedMessage = "Your order #" + id + " has been created.";
        assertEquals(expectedMessage,actualMessage );
    }

    @Test
    public void testFindOrderByIdFound() {
        int id = order.getOrderID();

        Order isIdFound = Order.findOrderByID(id);

        assertNotNull(isIdFound);
        assertEquals(id, isIdFound.getOrderID());
    }

    @Test
    public void testFindOrderByIdNotFound() {

        int id = order.getOrderID();

        Order.getAllOrders().clear();

        Order isIdFound = Order.findOrderByID(id);

        assertNull(isIdFound);
    }

    @Test
    public void testCustomerPhoneNumberValid() {

        order = new Order("0500000000");
        int id = order.getOrderID();
        Order customerOrder = order.findOrderByID(id);

        String actualCustomerPhone = customerOrder.getCustomerPhone();
        String expectedCustomerPhone = "0500000000";

        assertNotNull(customerOrder);
        assertEquals(expectedCustomerPhone, actualCustomerPhone);
    }

    @Test
    public void testCustomerPhoneNumberInvalid() {

        order = new Order("0505356934");
        int id = order.getOrderID();
        Order customerOrder = order.findOrderByID(id);

        String actualCustomerPhone = customerOrder.getCustomerPhone();
        String expectedCustomerPhone = "0500000000";

        assertNotNull(customerOrder);
        assertNotEquals(expectedCustomerPhone, actualCustomerPhone);
    }
}