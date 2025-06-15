package utils;

import java.io.*;
import java.util.*;

public class InventoryManager {
    private static final String FILE_NAME = "INVENTORY.txt";
    private String name;
    private double price;
    private int quantity;

    // Fixed constructor: quantity before price
    public InventoryManager(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Load inventory from file
    public static Map<String, InventoryManager> loadInventory() {
        Map<String, InventoryManager> inventory = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    inventory.put(name, new InventoryManager(name, quantity, price));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing inventory found. A new file will be created.");
        } catch (IOException e) {
            System.out.println("Error reading inventory: " + e.getMessage());
        }
        return inventory;
    }

    // Save inventory to file
    public static void saveInventory(Map<String, InventoryManager> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (InventoryManager item : inventory.values()) {
                bw.write(String.format("%s,%d,%.2f", item.name, item.quantity, item.price));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static void restockItem(String itemName, int quantity) {
        Map<String, InventoryManager> inventory = loadInventory();

        InventoryManager item = inventory.get(itemName);
        if (item != null) {
            item.addQuantity(quantity); // existing item, add to quantity
        } else {
            // Optional: if item doesn't exist, create it with default price (e.g., 0.0)
            System.out.println("Item '" + itemName + "' not found in inventory. Adding with default price.");
            inventory.put(itemName, new InventoryManager(itemName, quantity, 0.0));
        }

        saveInventory(inventory); // Save back to file
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

}
