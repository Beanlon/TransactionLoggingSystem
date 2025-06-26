package utils;

import java.io.*;
import java.util.*;

//Used for recording items in the inventory
public class InventoryItemRecord {
    private static final String FILE_NAME = "INVENTORY.txt"; //declares the constant file name
    private String name;
    private double price;
    private int quantity;

    //constructor that initializes the item with name quantity and price
    public InventoryItemRecord(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Load inventory from file
    public static Map<String, InventoryItemRecord> loadInventory() {
        Map<String, InventoryItemRecord> inventory = new LinkedHashMap<>(); //This makes sure that the Inventory is kept at the same order on the file
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) { //Uses BufferedReader to read the file line by line
            String line; //Declares a varible line to hold each line
            while ((line = br.readLine()) != null) { //loops over each line in the file until there are no more lines left
                String[] parts = line.split(","); //Splits the line using comma
                if (parts.length == 3) { //if the length of the line is seperated by three parts then the name, quantity and price are all placed inside the inventory.txt
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    inventory.put(name, new InventoryItemRecord(name, quantity, price));
                }
            }
        } catch (FileNotFoundException e) {//It will catch that the inventory is not found
            System.out.println("No existing inventory found. A new file will be created.");
        } catch (IOException e) {//It will catch if there is an error with the overall method
            System.out.println("Error reading inventory: " + e.getMessage());
        }
        return inventory;//It will return to the inventory.txt
    }

    // Save inventory to file
    public static void saveInventory(Map<String, InventoryItemRecord> inventory) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (InventoryItemRecord item : inventory.values()) {
                bw.write(String.format("%s,%d,%.2f", item.name, item.quantity, item.price));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public static void restockItem(String itemName, int quantity) {
        Map<String, InventoryItemRecord> inventory = loadInventory();

        InventoryItemRecord item = inventory.get(itemName);
        if (item != null) {
            item.addQuantity(quantity); // existing item, add to quantity
        } else {
            // Optional: if item doesn't exist, create it with default price (e.g., 0.0)
            System.out.println("Item '" + itemName + "' not found in inventory. Adding with default price.");
            inventory.put(itemName, new InventoryItemRecord(itemName, quantity, 0.0));
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
