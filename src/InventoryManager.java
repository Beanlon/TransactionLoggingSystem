import java.io.*;
import java.util.*;

public class InventoryManager {
    private String filename;
    private Map<String, InventoryItem> items;

    public InventoryManager(String filename) {
        this.filename = filename;
        this.items = new HashMap<>();
        loadInventory();
    }

    private void loadInventory() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());
                    items.put(name, new InventoryItem(name, quantity, price));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Inventory file not found, will create new one.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }

    public void updateItem(String itemName, int quantityToAdd, double sellingPrice) {
        InventoryItem item = items.get(itemName);
        if (item != null) {
            // Update existing item
            item.quantity += quantityToAdd;
            item.price = sellingPrice; // Update to new selling price
        } else {
            // Add new item
            item = new InventoryItem(itemName, quantityToAdd, sellingPrice);
            items.put(itemName, item);
        }
        saveInventory();
    }

    public void removeStock(String itemName, int removeCount) {
        List<String[]> items = new ArrayList<>();
        // 1. Read all lines
        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(itemName)) {
                    int stock = Integer.parseInt(parts[1].trim());
                    int newStock = Math.max(0, stock - removeCount); // Avoid negative
                    parts[1] = String.valueOf(newStock);
                }
                items.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. Write all lines back
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Inventory.txt"))) {
            for (String[] item : items) {
                bw.write(String.join(",", item));
                bw.newLine();
            }
            bw.flush(); // Ensure all data is written
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInventory() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (InventoryItem item : items.values()) {
                bw.write(String.format("%s,%d,%.2f\n", item.name, item.quantity, item.price));
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    private static class InventoryItem {
        String name;
        int quantity;
        double price;

        public InventoryItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }
}