package CPIT251;
import java.io.*;
import java.util.*;

public class Rating {

    private static List<Rating> ratingRecords = new ArrayList<>();

    private int orderID;
    private String comment;
    private int ratingScore;



    private Rating(int orderID, String comment, int ratingScore) {
        this.orderID = orderID;
        this.comment = comment;
        this.ratingScore = ratingScore;
    }

    public static String rateOrder(int orderID, String comment, int ratingScore) {

        if (ratingScore < 1 || ratingScore > 5) {
            return "Invalid rating. Please enter a value between 1 and 5.";
        }

        Order order = Order.findOrderByID(orderID);

        if (order == null) {
            return "Order not found. Please check the order ID.";
        }

        if (!order.getStatus().equalsIgnoreCase("Picked up")) {
            return "You can only rate your order after it has been Picked up.";
        }

        Rating newRating = new Rating(orderID, comment, ratingScore);
        ratingRecords.add(newRating);

        saveRatingForCustomer(orderID, comment, ratingScore);

        return "Thank you. You rated order #" + orderID +
                " with " + ratingScore + " stars. Comment: " + comment;
    }


    private static void saveRatingForCustomer(int orderID, String comment, int ratingScore) {

        try (FileWriter CustomerRatingFile = new FileWriter("customer_ratings.txt", true);
             PrintWriter ratingWriter = new PrintWriter(CustomerRatingFile
             )) {

            ratingWriter.println(orderID + ";" + ratingScore + ";" + comment);

        } catch (Exception e) {
            System.out.println("Error saving rating: " + e.getMessage());
        }
    }

    public static void loadRatingsFromFile() {

        File ratingsFile = new File("customer_ratings.txt");

        try (Scanner fileScanner = new Scanner(ratingsFile)) {

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(";");

                int id = Integer.parseInt(parts[0]);
                int score = Integer.parseInt(parts[1]);
                String comment = parts[2];

                ratingRecords.add(new Rating(id, comment, score));
            }

        } catch (Exception e) {
            System.out.println("Error loading ratings from file.");
        }
    }


    public static List<Rating> getAllRatings() {
        return ratingRecords;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getComment() {
        return comment;
    }

    public int getRatingScore() {
        return ratingScore;
    }
}
