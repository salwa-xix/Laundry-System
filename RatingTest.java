package CPIT251;

import static org.junit.Assert.*;
import org.junit.*;

public class RatingTest {

    @Before
    public void setUp() {
        Rating.getAllRatings().clear();
    }

    @Test
    public void testScoreTooLow() {

        String actualMessage   = Rating.rateOrder(10, "bad", 0);
        String expectedMessage = "Invalid rating. Please enter a value between 1 and 5.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testScoreTooHigh() {

        String actualMessage   = Rating.rateOrder(10, "Very clean", 6);
        String expectedMessage = "Invalid rating. Please enter a value between 1 and 5.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testValidRating() {
        Order order = new Order();
        order.updateOrderStatus("Picked up");
        int id = order.getOrderID();
        int score = 5;
        String comment = "Very good";

        String actualMessage = Rating.rateOrder(id, comment, score);
        String expectedMessage = "Thank you. You rated order #" + id + " with " + score + " stars. Comment: " + comment;
        assertEquals(expectedMessage, actualMessage);
    }
}
