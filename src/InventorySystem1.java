import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class InventorySystem1 extends JPanel {

    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JPanel paneltable;
    private Menu menuRef;
    private InventoryManager inventoryManager;

    public InventorySystem1(Menu menuRef) {
        this.menuRef = menuRef;
        this.inventoryManager = new InventoryManager("inventory.txt");
        setLayout(null);
        setPreferredSize(new Dimension(900, 520));

        // --- Panel 1: For ItemCreate ---
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
                new ItemCreate(InventorySystem1.this); // Pass reference to update inventory after creation
            }
        });
        add(panel1);

        // --- Panel 2: For SupplyPurchase ---
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
                    new SupplyPurchase(InventorySystem1.this, (Menu) parentFrame);
                }
            }
        });
        add(panel2);

        // --- Inventory Table Panel ---
        paneltable = new JPanel();
        paneltable.setBounds(40, 250, 717, 250);
        paneltable.setBackground(Color.white);
        paneltable.setLayout(new BorderLayout());

        // Define column names for the inventory table
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Item");
        columnNames.add("Category");
        columnNames.add("Price");
        columnNames.add("Stock");
        columnNames.add("Date Added");

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

        // Load the inventory data when the panel is initialized
        loadInventoryData();
    }

    /**
     * Loads inventory data from both Items.txt and Inventory.txt
     * Combines the data for display in the table
     */
    public void loadInventoryData() {
        inventoryTableModel.setRowCount(0); // Clear existing data

        // First load from Items.txt for detailed item information
        Map<String, Vector<Object>> itemsData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    try {
                        String id = data[0].trim();
                        String itemName = data[1].trim();
                        String category = data[2].trim();
                        String dateAdded = data[3].trim();
                        double sellingPrice = Double.parseDouble(data[6].trim());

                        Vector<Object> row = new Vector<>();
                        row.add(id);
                        row.add(itemName);
                        row.add(category);
                        row.add(String.format("%.2f", sellingPrice));
                        row.add(0); // Placeholder for quantity (will be updated from Inventory.txt)
                        row.add(dateAdded);

                        itemsData.put(itemName, row);
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid numeric data in Items.txt: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Items.txt: " + e.getMessage());
        }

        // Then update quantities from Inventory.txt
        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String itemName = data[0].trim();
                    int quantity = Integer.parseInt(data[1].trim());

                    if (itemsData.containsKey(itemName)) {
                        itemsData.get(itemName).set(4, quantity); // Update quantity
                    } else {
                        // Item exists in Inventory.txt but not in Items.txt
                        Vector<Object> row = new Vector<>();
                        row.add("N/A"); // No ID
                        row.add(itemName);
                        row.add("Uncategorized");
                        row.add(data[2].trim()); // Price from Inventory.txt
                        row.add(quantity);
                        row.add("Unknown");
                        itemsData.put(itemName, row);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Inventory.txt: " + e.getMessage());
        }

        // Add all combined data to the table
        for (Vector<Object> row : itemsData.values()) {
            inventoryTableModel.addRow(row);
        }
    }

    /**
     * Refreshes the inventory table from both data sources
     */
    public void refreshInventory() {
        loadInventoryData();
    }

    /**
     * Gets the inventory manager instance
     */
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}