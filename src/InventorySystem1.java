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

    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JPanel paneltable;
    private Menu menuRef;
    private InventoryManager inventoryManager;
    private Map<String, String> lastRestockDates;

    public InventorySystem1(Menu menuRef) {
        this.menuRef = menuRef;
        this.inventoryManager = new InventoryManager("Inventory.txt");
        this.lastRestockDates = new HashMap<>();
        setLayout(null);
        setPreferredSize(new Dimension(900, 520));

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

        RoundedPanel paneldelete = new RoundedPanel(30);
        paneldelete.setBounds(446, 535, 100, 47); // stack below panel1, adjust Y as needed
        paneldelete.setBackground(new Color(201, 42, 42, 255));
        paneldelete.setLayout(null);
        paneldelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = inventoryTable.getSelectedRow();
                if (row >= 0) {
                    String itemName = inventoryTableModel.getValueAt(row, 1).toString();
                    String itemCategory = inventoryTableModel.getValueAt(row, 2).toString();
                    int stock = Integer.parseInt(inventoryTableModel.getValueAt(row, 4).toString()); // <-- FIX

                    JLabel lblInfo = new JLabel("Remove from: " + itemName + " (" + itemCategory + ")");
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, stock, 1)); // min 1, max 100

                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(2, 1));
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

        RoundedPanel panel3 = new RoundedPanel(30);
        panel3.setBounds(555, 535, 200, 47); // stack below panel1, adjust Y as needed
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

        JLabel HeaderItem = new JLabel("INVENTORY ITEMS");
        HeaderItem.setBounds(43, 230, 300, 25);
        HeaderItem.setFont(new Font("Arial", Font.BOLD, 25));
        this.add(HeaderItem);

        // --- Inventory Table Panel ---
        paneltable = new JPanel();
        paneltable.setBounds(40, 275, 717, 250);
        paneltable.setBackground(Color.white);
        paneltable.setLayout(new BorderLayout());

        // Define column names
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

        loadInventoryData();
    }

    public void loadInventoryData() {
        // First load purchase records to get restock dates
        loadPurchaseRecords();

        inventoryTableModel.setRowCount(0); // Clear the table
        Map<String, Vector<Object>> itemsMap = new HashMap<>();

        // 1. Load ALL items from Items.txt (this is where ID and Category come from)
        try (BufferedReader br = new BufferedReader(new FileReader("Items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) { // We only need first 3 columns for ID, Name, Category
                    Vector<Object> row = new Vector<>();
                    row.add(data[0].trim()); // ID (FIRST COLUMN)
                    row.add(data[1].trim()); // Item Name
                    row.add(data[2].trim()); // CATEGORY (THIRD COLUMN)
                    row.add("₱0.00"); // Price placeholder
                    row.add(0); // Stock placeholder
                    row.add("-"); // Restock date placeholder
                    itemsMap.put(data[1].trim().toLowerCase(), row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Items.txt: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to load item data. Please check Items.txt",
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }

        // 2. Update with inventory quantities and prices
        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String itemName = data[0].trim();
                    String itemKey = itemName.toLowerCase();

                    if (itemsMap.containsKey(itemKey)) {
                        // Update existing item
                        Vector<Object> row = itemsMap.get(itemKey);
                        row.set(3, "₱" + data[2].trim()); // Price
                        row.set(4, Integer.parseInt(data[1].trim())); // Stock
                    } else {
                        // Create new entry for items not in Items.txt
                        Vector<Object> row = new Vector<>();
                        row.add("N/A"); // No ID available
                        row.add(itemName); // Item Name
                        row.add("Uncategorized"); // Default category
                        row.add("₱" + data[2].trim()); // Price
                        row.add(Integer.parseInt(data[1].trim())); // Stock
                        row.add("-"); // Restock date
                        itemsMap.put(itemKey, row);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading Inventory.txt: " + e.getMessage());
        }

        // 3. Update with restock dates
        for (Map.Entry<String, String> entry : lastRestockDates.entrySet()) {
            String itemKey = entry.getKey().toLowerCase();
            if (itemsMap.containsKey(itemKey)) {
                itemsMap.get(itemKey).set(5, entry.getValue());
            }
        }

        // Finally add all items to the table
        for (Vector<Object> row : itemsMap.values()) {
            inventoryTableModel.addRow(row);
        }
    }

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

    public void refreshInventory() {
        lastRestockDates.clear();
        loadPurchaseRecords();
        loadInventoryData();
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}