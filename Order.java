package CPIT251;

import java.io.*;
import java.util.*;

public class Order {

    private static List<Order> ordersRecord = new ArrayList<>();

    private static int nextOrderID = 1;

    private static final String[] ITEM_NAMES = {
            "Thobe",    "Shirt",    "Pants",    "Short",    "Shemagh",
            "Abaya",    "Dress",    "Jacket",   "Blanket",  "Carpet"
    };

    private static final String[] SERVICE_NAMES = {
            "Wash", "Iron", "Wash and Iron"
    };

    private static final int[][] PRICES = {
            {12, 7},    // Thobe
            {8, 5},     // Shirt
            {10, 6},    // Pants
            {6, 4},     // Short
            {7, 5},     // Shemagh
            {18, 12},   // Abaya
            {25, 15},   // Dress
            {20, 10},   // Jacket
            {35, 25},   // Blanket
            {50, 30}    // Carpet
    };

    private int orderID;
    List<OrderItem> items;
    private double totalPrice;
    private String status;
    private Notification notification;
    private String customerPhone;


    public Order(String customerPhone) {
        this.orderID = nextOrderID++;
        this.items = new ArrayList<>();
        this.totalPrice = 0;
        this.status = "Created";
        this.customerPhone = customerPhone;
        this.notification = new Notification();
        this.notification.prepare(this);
        ordersRecord.add(this);
        saveAllOrdersForCustomer();
    }


    public int getOrderID() {
        return orderID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public int getItemsCount() {
        return items.size();
    }

    public static int getItemsTypeCount() {
        return ITEM_NAMES.length;
    }

    public static int getServiceTypeCount() {
        return SERVICE_NAMES.length;
    }

    public static List<Order> getAllOrders() {
        return ordersRecord;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void addItem(int itemType, int serviceType, int quantity) {

        if (itemType < 0 || itemType >= PRICES.length) {
            System.out.println("Invalid item type");
            return;
        }

        if (serviceType < 0 || serviceType > 2) {
            System.out.println("Invalid service type");
            return;
        }

        double price;
        // Wash + Iron
        if (serviceType == 2) {
            price = PRICES[itemType][0] + PRICES[itemType][1];
        } else {
            price = PRICES[itemType][serviceType];
        }

        price *= quantity;

        int itemNumber = items.size() + 1;

        OrderItem item = new OrderItem(itemNumber, itemType, serviceType, quantity, price);
        items.add(item);
        totalPrice += price;
        saveAllOrdersForCustomer();
    }


    public void editItem(int itemNumber, int newitemType, int newServiceType, int newQuantity) {

        int index = itemNumber - 1;

        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid item number");
            return;
        }

        if (newitemType < 0 || newitemType >= PRICES.length) {
            System.out.println("Invalid new item type");
            return;
        }

        if (newServiceType < 0 || newServiceType > 2) {
            System.out.println("Invalid new service type");
            return;
        }

        OrderItem item = items.get(index);

        totalPrice -= item.getItemPrice();

        double newPrice;
        if (newServiceType == 2) {
            newPrice = PRICES[newitemType][0] + PRICES[newitemType][1];
        } else {
            newPrice = PRICES[newitemType][newServiceType];
        }
        newPrice *= newQuantity;

        item.setItemType(newitemType);
        item.setServiceType(newServiceType);
        item.setQuantity(newQuantity);
        item.setItemPrice(newPrice);

        totalPrice += newPrice;
        saveAllOrdersForCustomer();
    }


    public void printItems() {

        System.out.println("\n----- Order Details -----");
        System.out.println("|---------------|----------------------|");
        System.out.printf("| %-13s | %-20d |\n", "Order ID", orderID);
        System.out.printf("| %-13s | %-20s |\n", "Customerphone", customerPhone);
        System.out.printf("| %-13s | %-20s |\n", "Status", status);
        System.out.println("|---------------|----------------------|");


        if (items.isEmpty()) {
            System.out.println("No items in this order.");
            System.out.printf("Total price = %.2f\n", totalPrice);
            return;
        }

        System.out.println("|--------|----------|-------------------|----------|--------|");
        System.out.println("| Item#  | Type     | Service           | Quantity | Price  |");
        System.out.println("|--------|----------|-------------------|----------|--------|");

        for (OrderItem item : items) {
            String itemName = ITEM_NAMES[item.getItemType()];
            String serviceName = SERVICE_NAMES[item.getServiceType()];

            System.out.printf("| %-6d | %-8s | %-17s | %-8d | %-6.2f |\n",
                    item.getItemNumber(),
                    itemName,
                    serviceName,
                    item.getQuantity(),
                    item.getItemPrice());
        }

        System.out.println("|--------|----------|-------------------|----------|--------|");
        System.out.printf("| Total Price: %-44.2f |\n", totalPrice);
        System.out.println("|-----------------------------------------------------------|");

    }

    public void updateOrderStatus(String newStatus) {
        this.status = newStatus;
        notification.prepare(this);
        saveAllOrdersForCustomer();
    }

    public Notification getNotification() {
        return notification;
    }

    public static Order findOrderByID(int id) {
        for (Order order : ordersRecord) {
            if (order.getOrderID() == id) {
                return order;
            }
        }
        return null;
    }

    private Order(int id, String status, double totalPrice, String customerPhone) {
        this.orderID = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customerPhone = customerPhone;
        this.items = new ArrayList<>();
        this.notification = new Notification();
        this.notification.prepare(this);
        ordersRecord.add(this);
    }

    private void addLoadedItem(OrderItem item) {
        items.add(item);
    }

    public static void loadOrdersFromFile() {

        File allOrdersFile = new File("all_orders.txt");

        try (Scanner read = new Scanner(allOrdersFile)) {

            String line;
            Order currentOrder = null;
            boolean firstLine = true;

            while (read.hasNextLine()) {
                line = read.nextLine();

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length == 0) continue;

                String recordType = parts[0];

                //  if recordType = NEXT_ID
                if (firstLine && recordType.equals("NEXT_ID") && parts.length >= 2) {
                    try {
                        nextOrderID = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        nextOrderID = 1;
                    }
                    firstLine = false;
                    continue;
                }

                firstLine = false;

                // if recordType = ORDER
                if (recordType.equals("ORDER") && parts.length >= 4) {
                    int id = Integer.parseInt(parts[1]);
                    String status = parts[2];
                    double total = Double.parseDouble(parts[3]);

                    String customerPhone = "";
                    if (parts.length >= 5) {
                        customerPhone = parts[4];
                    }

                    currentOrder = new Order(id, status, total, customerPhone);
                } else if (recordType.equals("ITEM") && parts.length >= 6 && currentOrder != null) {
                    int itemNumber = Integer.parseInt(parts[1]);
                    int itemType = Integer.parseInt(parts[2]);
                    int serviceType = Integer.parseInt(parts[3]);
                    int quantity = Integer.parseInt(parts[4]);
                    double price = Double.parseDouble(parts[5]);

                    OrderItem item = new OrderItem(itemNumber, itemType, serviceType, quantity, price);
                    currentOrder.addLoadedItem(item);
                }
            }

        } catch (Exception e) {
            System.out.println("No orders file found. Starting with empty records.");
            nextOrderID = 1;
        }
    }

    public static void saveAllOrdersForCustomer() {
        try (FileWriter orderFile = new FileWriter("all_orders.txt");
             PrintWriter orderWriter = new PrintWriter(orderFile)) {

            orderWriter.println("NEXT_ID;" + nextOrderID);

            for (Order order : ordersRecord) {

                orderWriter.println("ORDER;"
                        + order.orderID + ";"
                        + order.status + ";"
                        + order.totalPrice + ";"
                        + order.customerPhone);



                for (OrderItem item : order.items) {

                    orderWriter.println("ITEM;" +
                            item.getItemNumber() + ";" +
                            item.getItemType() + ";" +
                            item.getServiceType() + ";" +
                            item.getQuantity() + ";" +
                            item.getItemPrice());
                }
            }

        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }
}
