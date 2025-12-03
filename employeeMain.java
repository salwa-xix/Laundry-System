package org.example;

import java.util.Scanner;

public class employeeMain {

    public static void main(String[] args) {

        Order.loadOrdersFromFile();
        Scanner input = new Scanner(System.in);
        int choice = -1;

        while (choice != 5) {
            System.out.println("\n===== Laundry System =====");
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

            choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                createNewOrderFlow(input);
            } else if (choice == 2) {
                editOrderFlow(input);
            } else if (choice == 3) {
                updateOrderStatusFlow(input);
            } else if (choice == 4) {
                printOrdersSummary();
            } else if (choice == 5) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid choice");
            }
        }

        input.close();
    }

    // 🌟 1) إنشاء طلب جديد + إضافة عناصر مع تحقق كامل
    private static void createNewOrderFlow(Scanner input) {

        Order order = new Order();

        System.out.println("\nNew order created. Order ID = " + order.getOrderID());

        int more = 1;
        while (more != 0) {

            int itemType = readIntInRange(
                    input,
                    "Enter item type:\n" +
                            "0 Thobe, 1 Shirt, 2 Pants, 3 Short, 4 Shemagh,\n" +
                            "5 Abaya, 6 Dress, 7 Jacket, 8 Blanket, 9 Carpet",
                    0,
                    Order.getItemsTypeCount() - 1
            );

            int serviceType = readIntInRange(
                    input,
                    "Enter service type (0 Wash, 1 Iron, 2 Wash+Iron):",
                    0,
                    Order.getServiceTypeCount() - 1
            );

            int qty = readPositiveInt(
                    input,
                    "Enter quantity (must be > 0):"
            );

            order.addItem(itemType, serviceType, qty);

            more = readIntInRange(
                    input,
                    "Item added. Add another item? (1 = yes, 0 = no):",
                    0,
                    1
            );
        }

        System.out.println("\nOrder completed.");
        saveCustomerNotification(order);
        order.printItems();
    }



    // 🌟 2) تعديل عناصر طلب موجود
// قبل ما نطلب ID نعرض كل الطلبات في جدول
    private static void editOrderFlow(Scanner input) {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("\nNo orders found. Create an order first.");
            return;
        }

        System.out.println("\nHere are all current orders:");
        printOrdersSummary();

        int id = readPositiveInt(input, "\nEnter Order ID to edit items:");

        Order order = Order.findOrderByID(id);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        order.printItems();

        if (order.getItemsCount() == 0) {
            System.out.println("This order has no items to edit.");
            return;
        }

        int itemNo = readIntInRange(
                input,
                "Enter item number to edit (1 - " + order.getItemsCount() + "):",
                1,
                order.getItemsCount()
        );

        int newType = readIntInRange(
                input,
                "Enter NEW item type:\n" +
                        "0 Thobe, 1 Shirt, 2 Pants, 3 Short, 4 Shemagh,\n" +
                        "5 Abaya, 6 Dress, 7 Jacket, 8 Blanket, 9 Carpet",
                0,
                Order.getItemsTypeCount() - 1
        );

        int newService = readIntInRange(
                input,
                "Enter NEW service type (0 Wash, 1 Iron, 2 Wash+Iron):",
                0,
                Order.getServiceTypeCount() - 1
        );

        int newQty = readPositiveInt(
                input,
                "Enter NEW quantity (must be > 0):"
        );

        order.editItem(itemNo, newType, newService, newQty);

        System.out.println("\nItem updated. Updated order items:");
        order.printItems();
    }


    // 🌟 3) تحديث حالة الطلب — برضو نعرض كل الطلبات قبل ما نطلب ID
    private static void updateOrderStatusFlow(Scanner input) {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("\nNo orders found. Create an order first.");
            return;
        }

        System.out.println("\nHere are all current orders:");
        printOrdersSummary();

        int id = readPositiveInt(input, "\nEnter Order ID to update status:");

        Order order = Order.findOrderByID(id);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("Current status: " + order.getStatus());
        System.out.println("Choose new status:");
        System.out.println("1) In progress");
        System.out.println("2) Ready");

        int statusChoice = readIntInRange(input, "Enter choice:", 1, 2);

        String newStatus;
        if (statusChoice == 1) {
            newStatus = "In progress";
        } else if (statusChoice == 2) {
            newStatus = "Ready";
        } else {
            return; // مستحيل نوصل هنا بسبب readIntInRange
        }


        // نحدّث الحالة
        order.updateOrderStatus(newStatus);

        // ⭐⭐ هنا: الإشعار ينضاف للعميل في كل الحالات
        saveCustomerNotification(order);

        // هذا الملف فقط نحتاجه لو الكستمر راح يقيّم الطلب
        if (newStatus.equals("Ready")) {
            saveReadyOrderForCustomer(order);
        }

        // 👇 جدول "Order updated successfully"
        System.out.println("\nOrder updated successfully:\n");
        System.out.println("+----------+----------------------+");
        System.out.println("| OrderID  | Status               |");
        System.out.println("+----------+----------------------+");
        System.out.printf("| %-8d | %-20s |\n",
                order.getOrderID(),
                order.getStatus());
        System.out.println("+----------+----------------------+");
    }

    // 🌟 4) طباعة ملخص كل الطلبات في جدول حقيقي
    public static void printOrdersSummary() {

        if (Order.getAllOrders().isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("+----------+--------+----------------------+--------------+");
        System.out.println("| OrderID  | Items  | Status               | Total Price  |");
        System.out.println("+----------+--------+----------------------+--------------+");

        for (Order order : Order.getAllOrders()) {
            System.out.printf("| %-8d | %-6d | %-20s | %-12.2f |\n",
                    order.getOrderID(),
                    order.getItemsCount(),
                    order.getStatus(),
                    order.getTotalPrice());
        }

        System.out.println("+----------+--------+----------------------+--------------+");
    }

    // يكتب الطلب الجاهز في ملف عشان العميل يستخدمه لاحقًا
    private static void saveReadyOrderForCustomer(Order order) {
        try (java.io.FileWriter fw = new java.io.FileWriter("orders_for_customers.txt", true);
             java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {

            pw.println(order.getOrderID() + ";" + order.getTotalPrice() + ";" + order.getStatus());

            System.out.println("Order saved for customer use.");
        } catch (java.io.IOException e) {
            System.out.println("Error writing order file: " + e.getMessage());
        }
    }


    // يحفظ إشعار العميل في ملف لاستخدامه لاحقاً في مين العميل
    private static void saveCustomerNotification(Order order) {
        try (java.io.FileWriter fw = new java.io.FileWriter("customer_notifications.txt", true);
             java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {

            String msg = order.getNotification().getCustomerMessage();

            // نخزن: orderID;message
            pw.println(order.getOrderID() + ";" + msg);

        } catch (java.io.IOException e) {
            System.out.println("Error saving customer notification: " + e.getMessage());
        }
    }

    // ============== 🎯 Helper methods للتحقق من المدخلات ==============

    private static int readIntInRange(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.println(prompt);
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

    private static int readPositiveInt(Scanner input, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (!input.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                input.nextLine();
                continue;
            }
            int value = input.nextInt();
            input.nextLine(); // clear newline

            if (value <= 0) {
                System.out.println("Value must be greater than 0.");
                continue;
            }
            return value;
        }
    }
}
