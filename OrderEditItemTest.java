package CPIT251;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderEditItemTest {

    private Order order;

    @Before
    public void setUp() {
        Order.getAllOrders().clear();
        String customerPhone = "0500000000";
        order = new Order(customerPhone);
        order.addItem(0, 0, 2);
        order.addItem(5, 2, 1);
    }

    @Test
    public void testEditItemValid() {

        int itemNumber = 1;
        int newitemType = 0;
        int newServiceType = 2;
        int newQuantity = 2;

        order.editItem(itemNumber, newitemType, newServiceType, newQuantity);

        OrderItem item = order.items.getFirst();

        assertEquals("0", String.valueOf(item.getItemType()));
        assertEquals("2", String.valueOf(item.getServiceType()));
        assertEquals("2", String.valueOf(item.getQuantity()));
        assertEquals("38.0", String.valueOf(item.getItemPrice()));
        assertEquals("68.0", String.valueOf(order.getTotalPrice()));
    }

    @Test
    public void testEditItemInvalidItemNumber() {

        int itemNumber = 5;
        int newitemType = 0;
        int newServiceType = 2;
        int newQuantity = 2;

        String beforeTotal = String.valueOf(order.getTotalPrice());
        int beforeCount = order.getItemsCount();

        order.editItem(itemNumber, newitemType, newServiceType, newQuantity);

        assertEquals(String.valueOf(beforeCount), String.valueOf(order.getItemsCount()));
        assertEquals(beforeTotal, String.valueOf(order.getTotalPrice()));
    }

    @Test
    public void testEditItemInvalidNewItemType() {

        int itemNumber = 1;
        int newitemType = 10;
        int newServiceType = 2;
        int newQuantity = 2;

        String beforeTotal = String.valueOf(order.getTotalPrice());

        order.editItem(itemNumber, newitemType, newServiceType, newQuantity);

        assertEquals(beforeTotal, String.valueOf(order.getTotalPrice()));
    }

    @Test
    public void testEditItemInvalidNewServiceType() {

        int itemNumber = 1;
        int newitemType = 0;
        int newServiceType = 6;
        int newQuantity = 2;

        String beforeTotal = String.valueOf(order.getTotalPrice());

        order.editItem(itemNumber, newitemType, newServiceType, newQuantity);

        assertEquals(beforeTotal, String.valueOf(order.getTotalPrice()));
    }
}
