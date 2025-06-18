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
    private JPanel paneltable; // Keep a reference to the panel where the table will be added

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

                new ItemCreate();
            }
        });
        add(panel1);

        RoundedPanel panel2 = new RoundedPanel(30);
        panel2.setBounds(415, 50, 340, 180);
        panel2.setBackground(new Color(255, 255, 255, 255));
        panel2.setLayout(null);
        add(panel2);

        // Panel for the inventory table
        paneltable = new JPanel(); // Initialize the panel here
        paneltable.setBounds(40, 300, 717, 250);
        paneltable.setBackground(Color.white);
        paneltable.setLayout(new BorderLayout()); // Use BorderLayout for the table

        // --- Inventory Table Setup ---
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Item Name");
        columnNames.add("Stocks");
        columnNames.add("Selling Price");
        columnNames.add("Last Restocked");

        inventoryTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells uneditable
            }
        };
        inventoryTableModel.setColumnIdentifiers(columnNames);

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setBackground(Color.WHITE);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.getTableHeader().setBackground(new Color(200, 0, 0)); // Red header
        inventoryTable.getTableHeader().setForeground(Color.WHITE);
        inventoryTable.setRowHeight(25); // Make rows a bit taller

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        paneltable.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the panel
        add(paneltable); // Add the panel containing the table to InventorySystem1

        // Load inventory data when the system starts
        loadInventoryData();
    }

    /**
     * Loads inventory data from PurchaseRecords.txt to populate the table.
     * This method processes the purchase records to show current stock,
     * selling price, and last restock date for each item.
     */
    public void loadInventoryData() {
        inventoryTableModel.setRowCount(0); // Clear existing data

        // Use a HashMap to aggregate stock and find the latest restock date for each item
        Map<String, InventoryItem> inventoryMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("PurchaseRecords.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) { // Ensure enough data exists for a full record
                    String itemName = data[3].trim();
                    int quantity = Integer.parseInt(data[4].trim());
                    double sellingPrice = Double.parseDouble(data[7].trim());
                    String dateSupplied = data[8].trim();

                    if (inventoryMap.containsKey(itemName)) {
                        InventoryItem existingItem = inventoryMap.get(itemName);
                        existingItem.addStock(quantity);
                        // Update selling price if it changes (taking the latest one from the record)
                        existingItem.setSellingPrice(sellingPrice);
                        // Update last restocked date if this record is more recent
                        if (dateSupplied.compareTo(existingItem.getLastRestocked()) > 0) {
                            existingItem.setLastRestocked(dateSupplied);
                        }
                    } else {
                        inventoryMap.put(itemName, new InventoryItem(itemName, quantity, sellingPrice, dateSupplied));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("PurchaseRecords.txt not found. Inventory table will be empty.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading purchase records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in PurchaseRecords.txt: " + e.getMessage());
        }

        // Add processed data to the table model
        for (InventoryItem item : inventoryMap.values()) {
            Vector<Object> row = new Vector<>();
            row.add(item.getItemName());
            row.add(item.getStocks());
            row.add(String.format("%.2f", item.getSellingPrice()));
            row.add(item.getLastRestocked());
            inventoryTableModel.addRow(row);
        }
    }

    // Helper class to manage inventory item data
    private static class InventoryItem {
        private String itemName;
        private int stocks;
        private double sellingPrice;
        private String lastRestocked;

        public InventoryItem(String itemName, int stocks, double sellingPrice, String lastRestocked) {
            this.itemName = itemName;
            this.stocks = stocks;
            this.sellingPrice = sellingPrice;
            this.lastRestocked = lastRestocked;
        }

        public String getItemName() {
            return itemName;
        }

        public int getStocks() {
            return stocks;
        }

        public void addStock(int quantity) {
            this.stocks += quantity;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public String getLastRestocked() {
            return lastRestocked;
        }

        public void setLastRestocked(String lastRestocked) {
            this.lastRestocked = lastRestocked;
        }
    }
}