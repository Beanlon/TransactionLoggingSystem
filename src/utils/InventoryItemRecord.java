package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class InventoryItemRecord {
    private String name;
    private double price;
    private int quantity; // This is the authoritative stock in the system

    public InventoryItemRecord(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        } else {
            System.err.println("Attempted to reduce quantity below zero for " + name + ". Current: " + this.quantity + ", Tried to reduce by: " + amount);
        }
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // Static method to load inventory from a file
    public static Map<String, InventoryItemRecord> loadInventory() {
        Map<String, InventoryItemRecord> inventory = new HashMap<>();
        File file = new File("inventory.csv");

        if (!file.exists()) {
            System.out.println("inventory.csv not found. Creating a new empty file.");
            try {
                file.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error creating inventory file: " + e.getMessage(), "File Creation Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            return inventory; // Return an empty map if file didn't exist or couldn't be created
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        int quantity = Integer.parseInt(parts[2].trim());
                        inventory.put(name, new InventoryItemRecord(name, price, quantity));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed inventory line (NumberFormat): " + line);
                    }
                } else {
                    System.err.println("Skipping malformed inventory line (Incorrect parts count): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading inventory: " + e.getMessage(), "Inventory Load Error", JOptionPane.ERROR_MESSAGE);
        }
        return inventory;
    }

    // Static method to save inventory to a file
    public static void saveInventory(Map<String, InventoryItemRecord> inventory) throws IOException {
        File file = new File("inventory.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (InventoryItemRecord record : inventory.values()) {
                bw.write(record.getName() + "," + record.getPrice() + "," + record.getQuantity());
                bw.newLine();
            }
        }
    }
}