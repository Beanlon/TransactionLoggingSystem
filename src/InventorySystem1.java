import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class InventorySystem1 extends JPanel {

    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JPanel paneltable;

    // A map to store item categories from Items.txt for quick lookup
    private Map<String, String> itemCategories = new HashMap<>();


    InventorySystem1() {
        setLayout(null);
        setPreferredSize(new Dimension(900, 520));

        RoundedPanel panel1 = new RoundedPanel(30);
        panel1.setBounds(40, 50, 340, 180);
        panel1.setBackground(new Color(255, 255, 255, 255));
        panel1.setLayout(null);
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventorySystem1.this);
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                new ItemCreate(); // Retained as per previous instruction to not change anything else
            }
        });
        add(panel1);

        RoundedPanel panel2 = new RoundedPanel(30);
        panel2.setBounds(415, 50, 340, 180);
        panel2.setBackground(new Color(255, 255, 255, 255));
        panel2.setLayout(null);
        panel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventorySystem1.this);
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                // Pass 'this' (the InventorySystem1 instance) to SupplyPurchasePanel
                new SupplyPurchase(InventorySystem1.this);
            }
        });
        add(panel2);


        paneltable = new JPanel();
        paneltable.setBounds(40, 250, 717, 250); // Fixed bounds for visibility
        paneltable.setBackground(Color.white);
        paneltable.setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Item");
        columnNames.add("Price");
        columnNames.add("Stock");
        columnNames.add("Category");
        columnNames.add("Date last restocked"); // Reflects actual purchase date/time

        inventoryTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTableModel.setColumnIdentifiers(columnNames);

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setBackground(Color.WHITE);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.getTableHeader().setBackground(new Color(200, 0, 0));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);
        inventoryTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        paneltable.add(scrollPane, BorderLayout.CENTER);
        add(paneltable);

        // Load item categories from Items.txt first
        loadItemCategories();
        // Then load inventory data which will use these categories
        loadInventoryData();
    }

    /**
     * Loads item names and their categories from "Items.txt" into itemCategories map.
     * Assumes Items.txt format: ID, Name, Category, Date Added
     */
    private void loadItemCategories() {
        itemCategories.clear(); // Clear existing categories
        try (BufferedReader br = new BufferedReader(new FileReader("Items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String itemName = data[1].trim();
                    String category = data[2].trim();
                    itemCategories.put(itemName, category);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Items.txt not found. Cannot load item categories.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading item categories from Items.txt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Loads inventory data from PurchaseRecords.txt to populate the table.
     * Displays only items that have been purchased, aggregating stock and
     * showing the actual last restocked date/time from purchase records.
     * Assumes PurchaseRecords.txt format (comma-separated):
     * [0]Supply ID, [1]Supplier Name, [2]Supplier Code, [3]Item Name, [4]Quantity,
     * [5]Cost, [6]Profit %, [7]Selling Price, [8]Date Supplied
     */
    public void loadInventoryData() {
        inventoryTableModel.setRowCount(0); // Clear existing data

        Map<String, InventoryItem> inventoryMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("PurchaseRecords.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Ensure enough data exists for all required fields based on SupplyPurchasePanel's write format
                if (data.length >= 9) { // We expect up to index 8
                    String itemName = data[3].trim();

                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(data[4].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid quantity for item '" + itemName + "': " + data[4].trim());
                        continue; // Skip this line if quantity is invalid
                    }

                    double sellingPrice = 0.0;
                    try {
                        sellingPrice = Double.parseDouble(data[7].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid selling price for item '" + itemName + "': " + data[7].trim());
                        continue; // Skip this line if selling price is invalid
                    }

                    // Correct index for Date Supplied is 8
                    String purchaseDateTime = data[8].trim();

                    // Get category from the pre-loaded itemCategories map
                    String category = itemCategories.getOrDefault(itemName, "N/A"); // Default to "N/A" if not found

                    if (inventoryMap.containsKey(itemName)) {
                        InventoryItem existingItem = inventoryMap.get(itemName);
                        existingItem.addStock(quantity);
                        // The category and selling price should ideally be consistent for an item,
                        // but if they could change, you might want to set them again.
                        // For simplicity, we assume they remain the same or take the last one.
                        existingItem.setSellingPrice(sellingPrice); // Update price to the latest one
                        existingItem.setCategory(category); // Update category if it changes (less common)

                        // Update last restocked date only if the current record is more recent
                        // Compare date-time strings lexicographically (yyyyMMdd_HHmmss format is best for this)
                        // Assuming "dd/MM/yyyy HH:mm:ss" for purchaseDateTime from SupplyPurchasePanel
                        if (purchaseDateTime.compareTo(existingItem.getLastRestocked()) > 0) {
                            existingItem.setLastRestocked(purchaseDateTime);
                        }
                    } else {
                        inventoryMap.put(itemName, new InventoryItem(itemName, category, quantity, sellingPrice, purchaseDateTime));
                    }
                } else {
                    System.err.println("Skipping malformed line in PurchaseRecords.txt (insufficient columns or unexpected data): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "PurchaseRecords.txt not found. Inventory table will be empty.", "Information", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("PurchaseRecords.txt not found. Inventory table will be empty.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading purchase records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add processed data from the map to the table model
        for (InventoryItem item : inventoryMap.values()) {
            Vector<Object> row = new Vector<>();
            row.add(item.getItemName());
            row.add(String.format("%.2f", item.getSellingPrice())); // Price (Selling Price)
            row.add(item.getStocks()); // Stock
            row.add(item.getCategory()); // Category
            row.add(item.getLastRestocked()); // Date last restocked
            inventoryTableModel.addRow(row);
        }
    }

    // Helper class to manage aggregated inventory item data
    private static class InventoryItem {
        private String itemName;
        private String category;
        private int stocks;
        private double sellingPrice;
        private String lastRestocked; // Stores the latest date/time string

        public InventoryItem(String itemName, String category, int stocks, double sellingPrice, String lastRestocked) {
            this.itemName = itemName;
            this.category = category;
            this.stocks = stocks;
            this.sellingPrice = sellingPrice;
            this.lastRestocked = lastRestocked;
        }

        public String getItemName() { return itemName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; } // Added setter
        public int getStocks() { return stocks; }
        public void addStock(int quantity) { this.stocks += quantity; }
        public double getSellingPrice() { return sellingPrice; }
        public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; } // Added setter
        public String getLastRestocked() { return lastRestocked; }
        public void setLastRestocked(String lastRestocked) { this.lastRestocked = lastRestocked; }
    }
}