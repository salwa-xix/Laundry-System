package CPIT251;

import java.io.*;
import java.util.*;

public class CustomerMain {


    private static final String CURRENT_CUSTOMER_PHONE = "0500000000";

    public static void main(String[] args) {

        Rating.loadRatingsFromFile();
        Order.loadOrdersFromFile();

        Scanner input = new Scanner(System.in);
        int Customerchoice = -1;

        while (Customerchoice != 4) {
            System.out.println("\n-------- Laundry System --------");
            System.out.println("-------- Customer UI --------");
            System.out.println("---- For Customer with Phone# 0500000000 ----");
            System.out.println("1) View my notifications");
            System.out.println("2) View past ratings");
            System.out.println("3) Rate my new order");
            System.out.println("4) Exit");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid choice.");
                input.nextLine();
                continue;
            }

            Customerchoice = input.nextInt();
            input.nextLine();

            if (Customerchoice == 1) {
                viewNotifications();
            } else if (Customerchoice == 2) {
                viewPastRatings();
            } else if (Customerchoice == 3) {
                rateNewOrder(input);
            } else if (Customerchoice == 4) {
                System.out.println("Goodbye");
            } else {
                System.out.println("Invalid choice.");
            }
        }

        input.close();
    }


    // ----------------------------------------------------------

    private static void viewNotifications() {

        System.out.println("\n---- Your Notifications ----");

        File notificationsFile = new File("customer_notifications.txt");

        try (Scanner read = new Scanner(notificationsFile)) {

            boolean hasNotifications = false;

            System.out.println("|----------|-------------------------------------------------------------|");
            System.out.println("| Order    | Message                                                     |");
            System.out.println("|----------|-------------------------------------------------------------|");

            while (read.hasNextLine()) {

                String line = read.nextLine();
                String[] parts = line.split(";", 3);

                if (parts.length < 3) continue;

                String phoneInFile = parts[0];
                int orderID = Integer.parseInt(parts[1]);
                String msg = parts[2];

                if (!phoneInFile.equals(CURRENT_CUSTOMER_PHONE)) {
                    continue;
                }

                hasNotifications = true;
                System.out.println("Order #" + orderID + " | " + msg);
            }

            System.out.println("|----------|-------------------------------------------------------------|");

            if (!hasNotifications) {
                System.out.println("| No notifications yet.                                                  |");
                System.out.println("|------------------------------------------------------------------------|");
            }

        } catch (Exception e) {
            System.out.println("No notifications file found.");
        }
    }


    // ----------------------------------------------------------

    private static void viewPastRatings() {
        System.out.println("\n---- Your Ratings ----");

        if (Rating.getAllRatings().isEmpty()) {
            System.out.println("No previous ratings.");
            return;
        }

        System.out.println("|----------|--------|------------------------------------------|");
        System.out.println("| Order    | Rating | Comment                                  |");
        System.out.println("|----------|--------|------------------------------------------|");

        for (Rating rating : Rating.getAllRatings()) {
            System.out.printf("| %-8s | %-6d | %-40s |\n",
                    "Order #" + rating.getOrderID(),
                    rating.getRatingScore(),
                    rating.getComment());
        }

        System.out.println("|----------|--------|------------------------------------------|");
    }

    // ----------------------------------------------------------

    private static void rateNewOrder(Scanner input) {

        OrderInfo info = findNextUnratedOrder();

        if (info == null) {
            System.out.println("There are no new orders to rate.");
            return;
        }

        System.out.println("\nYour order #" + info.orderId + " is Picked up");
        System.out.println("Order details:");
        System.out.println("  Total price: " + info.totalPrice);
        System.out.println("  Status     : " + info.status);

        int ratingScore = 0;

        while (true) {
            System.out.print("\nRate your order (1 - 5): ");

            if (input.hasNextInt()) {
                ratingScore = input.nextInt();
                input.nextLine();

                if (ratingScore >= 1 && ratingScore <= 5) {
                    break;
                } else {
                    System.out.println("Please enter a number from 1 to 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }

        System.out.print("Add a comment: ");
        String comment = input.nextLine();

        String ratingHistory = Rating.rateOrder(info.orderId, comment, ratingScore);

        System.out.println("\n" + ratingHistory);
    }

    // ----------------------------------------------------------

    private static OrderInfo findNextUnratedOrder() {

        Set<Integer> ratedOrders = new HashSet<>();
        for (Rating rating : Rating.getAllRatings()) {
            ratedOrders.add(rating.getOrderID());
        }

        File readyOrdersFile = new File("ready_orders_for_customers.txt");

        try (Scanner fileScanner = new Scanner(readyOrdersFile)) {

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split(";");

                if (parts.length < 4) continue;

                String phoneInFile = parts[0];
                int orderID = Integer.parseInt(parts[1]);
                double price = Double.parseDouble(parts[2]);
                String status = parts[3];

                if (!phoneInFile.equals(CURRENT_CUSTOMER_PHONE)) {
                    continue;
                }

                if (status.equalsIgnoreCase("Picked up") && !ratedOrders.contains(orderID)) {
                    return new OrderInfo(orderID, price, status);
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading orders file.");
        }

        return null;
    }
}

