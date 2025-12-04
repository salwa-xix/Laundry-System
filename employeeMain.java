package CPIT251;

import java.io.*;
import java.util.*;

public class employeeMain {

    public static void main(String[] args) {

        Order.loadOrdersFromFile();
        Scanner input = new Scanner(System.in);
        int employeeChoice = -1;

        while (employeeChoice != 5) {
            System.out.println("\n---- Laundry System ----");
            System.out.println("---- Employee UI ----");
            System.out.println("1) Create new order");
            System.out.println("2) Edit existing order items");
            System.out.println("3) Update order status");
            System.out.println("4) View all orders ");
            System.out.println("5) Exit");
            System.out.print("Enter choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid choice.");
                input.nextLine();
                continue;
            }

            employeeChoice = input.nextInt();
            input.nextLine();

            if (employeeChoice == 1) {
                createNewOrder(input);
            } else if (employeeChoice == 2) {
                editOrder(input);
            } else if (employeeChoice == 3) {
                updateOrderStatus(input);
            } else if (employeeChoice == 4) {
                printOrdersSummary();
            } else if (employeeChoice == 5) {
                System.out.println("Goodbye");
            } else {
                System.out.println("Invalid choice");
            }
        }

        input.close();
    }


    private static void createNewOrder(Scanner input) {

        System.out.print("Enter customer phone number: 0500000000 ");
        String customerPhone = "0500000000";

        Order order = new Order(customerPhone);

        System.out.println("\nNew order created. Order ID = " + order.getOrderID());

        int moreItems = 1;
        while (moreItems != 0) {

            int itemType = validateInput(
                    input,
                    "Enter item type:\n" +
                            "0 Thobe, 1 Shirt, 2 Pants, 3 Short, 4 Shemagh,\n" +
                            "5 Abaya, 6 Dress, 7 Jacket, 8 Blanket, 9 Carpet",
                    0,
                    Order.getItemsTypeCount() - 1
            );

            int serviceType = validateInput(
                    input,
                    "Enter service type (0 Wash, 1 Iron, 2 Wash+Iron):",
                    0,
                    Order.getServiceTypeCount() - 1
            );

            int quantity = readPositiveInt(
                    input,
                    "Enter quantity:"
            );

            order.addItem(itemType, serviceType, quantity);

            moreItems = validateInput(
                    input,
                    "Item added. Add another item? (1 = yes, 0 = no):",
                    0,
                    1
            );
        }

        System.out.println("\nOrder completed.");
        saveNotificationForCustomer(order);
        order.printItems();
    }


    private static void editOrder(Scanner input) {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("\nNo orders found. Create an order first.");
            return;
        }

        System.out.println("\nHere are all current orders:");
        printOrdersSummary();

        int orderID = readPositiveInt(input, "\nEnter Order ID to edit items:");

        Order order = Order.findOrderByID(orderID);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        order.printItems();

        if (order.getItemsCount() == 0) {
            System.out.println("This order has no items to edit.");
            return;
        }

        int itemNumber = validateInput(
                input,
                "Enter item number to edit (1 - " + order.getItemsCount() + "):",
                1,
                order.getItemsCount()
        );

        int newitemType = validateInput(
                input,
                "Enter the new item type:\n" +
                        "0 Thobe, 1 Shirt, 2 Pants, 3 Short, 4 Shemagh,\n" +
                        "5 Abaya, 6 Dress, 7 Jacket, 8 Blanket, 9 Carpet",
                0,
                Order.getItemsTypeCount() - 1
        );

        int newServiceType = validateInput(
                input,
                "Enter the new service type (0 Wash, 1 Iron, 2 Wash+Iron):",
                0,
                Order.getServiceTypeCount() - 1
        );

        int newQuantity = readPositiveInt(
                input,
                "Enter the new quantity:"
        );

        order.editItem(itemNumber, newitemType, newServiceType, newQuantity);
        System.out.println("\nItem updated. Updated order items:");
        order.printItems();
    }


    public static void updateOrderStatus(Scanner input) {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("\nNo orders found. Create an order first.");
            return;
        }

        System.out.println("\nHere are all current orders:");
        printOrdersSummary();

        int orderID = readPositiveInt(input, "\nEnter Order ID to update status:");

        Order order = Order.findOrderByID(orderID);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Current status: " + order.getStatus());
        System.out.println("Choose new status:");
        System.out.println("1) In progress");
        System.out.println("2) Ready");
        System.out.println("3) Picked up");

        int statusChoice = validateInput(input, "Enter choice:", 1, 3);


        String newStatus;
        if (statusChoice == 1) {
            newStatus = "In progress";
        } else if (statusChoice == 2) {
            newStatus = "Ready";
        } else if (statusChoice == 3) {
            newStatus = "Picked up";
        } else {
            return;
        }



        order.updateOrderStatus(newStatus);

        saveNotificationForCustomer(order);

        if (newStatus.equals("Picked up")) {
            saveReadyOrderForCustomer(order);
        }


        System.out.println("\nOrder updated successfully:\n");
        System.out.println("|----------|----------------------|");
        System.out.println("| OrderID  | Status               |");
        System.out.println("|----------|----------------------|");
        System.out.printf("| %-8d | %-20s |\n",
                order.getOrderID(),
                order.getStatus());
        System.out.println("|----------|----------------------|");
    }


    private static void printOrdersSummary() {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("|----------|--------|----------------------|--------------|------------------|");
        System.out.println("| OrderID  | Items  | Status               | Total Price  | CustomerPhone   |");
        System.out.println("|----------|--------|----------------------|--------------|------------------|");

        for (Order order : Order.getAllOrders()) {
            System.out.printf("| %-8d | %-6d | %-20s | %-12.2f | %-16s |\n",
                    order.getOrderID(),
                    order.getItemsCount(),
                    order.getStatus(),
                    order.getTotalPrice(),
                    order.getCustomerPhone());
        }

        System.out.println("|----------|--------|----------------------|--------------|------------------|");

    }


    private static void saveReadyOrderForCustomer(Order order) {
        try (FileWriter readyOrderFile = new FileWriter("ready_orders_for_customers.txt", true);
             PrintWriter readyWriter = new PrintWriter(readyOrderFile)) {

            readyWriter.println(order.getCustomerPhone() + ";" +
                    order.getOrderID() + ";" +
                    order.getTotalPrice() + ";" +
                    order.getStatus());

        } catch (Exception e) {
            System.out.println("Error writing order file: " + e.getMessage());
        }
    }


    private static void saveNotificationForCustomer(Order order) {
        try (FileWriter notificationFile = new FileWriter("customer_notifications.txt", true);
             PrintWriter notificationWriter = new PrintWriter(notificationFile)) {

            String message = order.getNotification().getCustomerMessage();

            notificationWriter.println(order.getCustomerPhone() + ";" + order.getOrderID() + ";" + message);

        } catch (Exception e) {
            System.out.println("Error saving customer notification: " + e.getMessage());
        }
    }


    private static int validateInput(Scanner input, String message, int min, int max) {
        while (true) {
            System.out.println(message);
            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.nextLine();
                continue;
            }
            int value = input.nextInt();
            input.nextLine();

            if (value < min || value > max) {
                System.out.println("Please enter a value between " + min + " and " + max + ".");
                continue;
            }
            return value;
        }
    }

    private static int readPositiveInt(Scanner input, String message) {
        while (true) {
            System.out.println(message);
            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.nextLine();
                continue;
            }
            int value = input.nextInt();
            input.nextLine();

            if (value <= 0) {
                System.out.println("Value must be greater than 0.");
                continue;
            }
            return value;
        }
    }
}
