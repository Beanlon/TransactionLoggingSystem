// InventorySystem1.java
// Inventory management panel for displaying, adding, removing, and restocking items.
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InventorySystem1 extends JPanel {
    // Table and model for displaying inventory
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JPanel paneltable;
    private Menu menuRef;
    private InventoryManager inventoryManager;
    // Stores the last restock date for each item
    private Map<String, String> lastRestockDates;

    // Constructor: sets up UI and loads inventory data
    public InventorySystem1(Menu menuRef) {
        this.menuRef = menuRef; // Reference to main menu
        this.inventoryManager = new InventoryManager("Inventory.txt");
        this.lastRestockDates = new HashMap<>();
        setLayout(null);
        setPreferredSize(new Dimension(900, 520));

        // Panel for adding a new item
        RoundedPanel panel1 = new RoundedPanel(30);
        panel1.setBounds(40, 30, 340, 180);
        panel1.setBackground(new Color(255, 255, 255, 255));
        panel1.setLayout(null);
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventorySystem1.this);
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                new ItemCreate(InventorySystem1.this, menuRef);
            }
        });
        JLabel addItemLabel = new JLabel("ADD ITEM +", SwingConstants.CENTER);
        addItemLabel.setForeground(new Color (201, 42, 42));
        addItemLabel.setFont(new Font("Arial", Font.BOLD, 28));
        addItemLabel.setBounds(0, 60, 340, 40);
        panel1.add(addItemLabel);
        add(panel1);

        // Panel for adding supply (restocking)
        RoundedPanel panel2 = new RoundedPanel(30);
        panel2.setBounds(415, 30, 340, 180);
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
        JLabel addSupplyLabel = new JLabel("ADD SUPPLY +", SwingConstants.CENTER);
        addSupplyLabel.setFont(new Font("Arial", Font.BOLD, 28));
        addSupplyLabel.setForeground(new Color (201, 42, 42));
        addSupplyLabel.setBounds(0, 60, 340, 40);
        panel2.add(addSupplyLabel);
        add(panel2);

        // Panel for deleting items from inventory
        RoundedPanel paneldelete = new RoundedPanel(30);
        paneldelete.setBounds(446, 535, 100, 47);
        paneldelete.setBackground(new Color(201, 42, 42, 255));
        paneldelete.setLayout(null);
        paneldelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = inventoryTable.getSelectedRow();
                if (row >= 0) {
                    String itemName = inventoryTableModel.getValueAt(row, 1).toString();
                    String itemCategory = inventoryTableModel.getValueAt(row, 2).toString();
                    int stock = Integer.parseInt(inventoryTableModel.getValueAt(row, 4).toString());

                    // Dialog for removing a certain quantity
                    JLabel lblInfo = new JLabel("Remove from: " + itemName + " (" + itemCategory + ")");
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, stock, 1));
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(1, 2));
                    panel.add(lblInfo);
                    panel.add(spinner);

                    int result = JOptionPane.showConfirmDialog(
                            null, panel, "Remove Items", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        int removeCount = (Integer) spinner.getValue();
                        inventoryManager.removeStock(itemName, removeCount); // Updates Inventory.txt
                        refreshInventory(); // Reloads table from file
                        JOptionPane.showMessageDialog(null, "You removed " + removeCount + " items from " + itemName + ".");
                    }
                }
            }
        });
        JLabel lbldelete = new JLabel("DELETE");
        lbldelete.setFont(new Font("Arial", Font.BOLD, 18));
        lbldelete.setForeground(new Color (255, 253, 253));
        lbldelete.setBounds(13, 17, 300, 17);
        paneldelete.add(lbldelete);
        add(paneldelete);

        // Panel for viewing restock summary
        RoundedPanel panel3 = new RoundedPanel(30);
        panel3.setBounds(555, 535, 200, 47);
        panel3.setBackground(new Color(255, 255, 255, 255));
        panel3.setLayout(null);
        panel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RestockSummaryBrowser();
            }
        });
        JLabel lblRestock = new JLabel("RESTOCK SUMMARY");
        lblRestock.setFont(new Font("Arial", Font.BOLD, 18));
        lblRestock.setForeground(new Color (201, 42, 42));
        lblRestock.setBounds(5, 17, 300, 17);
        panel3.add(lblRestock);
        add(panel3);

        // Header label for inventory table
        JLabel HeaderItem = new JLabel("INVENTORY ITEMS");
        HeaderItem.setBounds(43, 230, 300, 25);
        HeaderItem.setFont(new Font("Arial", Font.BOLD, 25));
        this.add(HeaderItem);

        // Inventory table panel setup
        paneltable = new JPanel();
        paneltable.setBounds(40, 275, 717, 250);
        paneltable.setBackground(Color.white);
        paneltable.setLayout(new BorderLayout());

        // Define column names for inventory table
        String[] columnNames = {"ID", "Item Name", "Category", "Price", "Stock", "Last Restocked"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setBackground(Color.WHITE);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.getTableHeader().setBackground(new Color(200, 0, 0));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);
        inventoryTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        paneltable.add(scrollPane, BorderLayout.CENTER);
        add(paneltable);

        loadInventoryData(); // Load data into table
    }

    // Loads inventory data from Items.txt and Inventory.txt, and updates table
    public void loadInventoryData() {
        loadPurchaseRecords(); // Load last restock dates
        inventoryTableModel.setRowCount(0); // Clear table
        Map<String, Vector<Object>> itemsMap = new HashMap<>();
        Set<String> itemsTxtKeys = new HashSet<>(); // Store keys from Items.txt

        // 1. Load all items from Items.txt
        try (BufferedReader br = new BufferedReader(new FileReader("Items.txt"))) {
            String line; // Read each line from Items.txt
            while ((line = br.readLine()) != null) { // Split line by commas
                String[] data = line.split(",");
                if (data.length >= 3) { // Ensure there are at least 3 fields (ID, Name, Category)
                    Vector<Object> row = new Vector<>();
                    row.add(data[0].trim()); // ID
                    row.add(data[1].trim()); // Name
                    row.add(data[2].trim()); // Category
                    row.add("₱0.00"); // Default price
                    row.add(0); // Default stock
                    row.add("-"); // Default last restocked
                    String itemKey = data[1].trim().toLowerCase(); // Use item name as key (case-insensitive)
                    itemsMap.put(itemKey, row); // Add to map with item name as key
                    itemsTxtKeys.add(itemKey); // Store keys for later filtering
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Items.txt: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to load item data. Please check Items.txt",
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        // 2. Update with inventory quantities and prices from Inventory.txt
        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String itemName = data[0].trim();
                    String itemKey = itemName.toLowerCase();
                    int stock = Integer.parseInt(data[1].trim());
                    String price = data[2].trim();

                    if (itemsMap.containsKey(itemKey)) {
                        Vector<Object> row = itemsMap.get(itemKey);
                        row.set(3, "₱" + price); // Set price
                        row.set(4, stock); // Set stock
                    } else {
                        // Item not in Items.txt (deleted from ItemCreate), show if stock > 0
                        if (stock > 0) {
                            Vector<Object> row = new Vector<>();
                            row.add("N/A");
                            row.add(itemName);
                            row.add("Uncategorized");
                            row.add("₱" + price);
                            row.add(stock);
                            row.add("-");
                            itemsMap.put(itemKey, row);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading Inventory.txt: " + e.getMessage());
        }

// 3. Update last restock dates from purchase records
        for (Map.Entry<String, String> entry : lastRestockDates.entrySet()) { // Iterate through last restock dates
            String itemKey = entry.getKey().toLowerCase(); // Use item name as key (case-insensitive)
            if (itemsMap.containsKey(itemKey)) { // Check if item exists in itemsMap
                itemsMap.get(itemKey).set(5, entry.getValue()); // Set last restock date
            }
        }

        // Add rows to table (show all items in Items.txt, or items with stock > 0)
        for (Vector<Object> row : itemsMap.values()) {
            String itemName = ((String) row.get(1)).toLowerCase(); // Get item name in lowercase for case-insensitive comparison
            int stock = (Integer) row.get(4);
            if (itemsTxtKeys.contains(itemName) || stock > 0) {
                inventoryTableModel.addRow(row);
            }
        }
    }

    // Loads purchase records to determine last restock date for each item
    private void loadPurchaseRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader("PurchaseRecords.txt"))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    String itemName = data[3].trim();
                    String dateStr = data[8].trim();

                    try {
                        Date currentDate = dateFormat.parse(dateStr);
                        String existingDate = lastRestockDates.get(itemName);
                        if (existingDate == null || currentDate.after(dateFormat.parse(existingDate))) {
                            lastRestockDates.put(itemName, dateStr);
                        }
                    } catch (ParseException e) {
                        System.err.println("Invalid date format: " + dateStr);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading PurchaseRecords.txt: " + e.getMessage());
        }
    }

    // Refreshes the inventory table and restock dates
    public void refreshInventory() {
        lastRestockDates.clear();
        loadPurchaseRecords();
        loadInventoryData();
    }

    // Returns the inventory manager instance
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}