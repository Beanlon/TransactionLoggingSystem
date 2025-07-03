import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class MainPanel extends JPanel implements ActionListener {
    // UI components
    private JTextField txtSearch;
    private InventorySystem1 inventorySystem;
    private JPanel pnlbtn;
    private JButton createnew, load, delete;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSales, lblTotalTransactions, lblMostBought;
    private JComboBox<String> monthComboBox;
    // Helper data structures
    private final Map<String, String> fileToMonthYear = new HashMap<>();
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMMyyyy");

    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(785, 630));

        // Panels for overview statistics
        RoundedPanel salesPanelContainer = new RoundedPanel(25);
        salesPanelContainer.setBounds(20, 20, 243, 125);
        salesPanelContainer.setBackground(Color.WHITE);

        RoundedPanel transactionsPanelContainer = new RoundedPanel(25);
        transactionsPanelContainer.setBounds(280, 20, 243, 125);
        transactionsPanelContainer.setBackground(Color.WHITE);

        RoundedPanel mostBoughtPanelContainer = new RoundedPanel(25);
        mostBoughtPanelContainer.setBounds(540, 20, 243, 125);
        mostBoughtPanelContainer.setBackground(Color.WHITE);

        // Sales Panel
        salesPanelContainer.setLayout(null);
        JPanel salesLine = new JPanel();
        salesLine.setBackground(new Color(201, 42, 42));
        salesLine.setBounds(0, 12, 5, 100);
        salesPanelContainer.add(salesLine);

        JLabel lblSalesTitle = new JLabel("TOTAL SALES");
        lblSalesTitle.setFont(new Font("SansSerif", Font.BOLD, 21));
        lblSalesTitle.setForeground(new Color(201, 42, 42));
        lblSalesTitle.setBounds(10, 10, 150, 21);
        lblSalesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSalesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        salesPanelContainer.add(lblSalesTitle);

        lblTotalSales = new JLabel("₱ 0.00");
        lblTotalSales.setFont(new Font("SansSerif", Font.PLAIN, 21));
        lblTotalSales.setForeground(new Color(201, 42, 42));
        lblTotalSales.setBounds(20, 55, 200, 30);
        lblTotalSales.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalSales.setHorizontalAlignment(SwingConstants.CENTER);
        salesPanelContainer.add(lblTotalSales);

        // Transactions Panel
        transactionsPanelContainer.setLayout(null);
        JPanel transactionsLine = new JPanel();
        transactionsLine.setBackground(new Color(201, 42, 42));
        transactionsLine.setBounds(0, 12, 5, 100);
        transactionsPanelContainer.add(transactionsLine);

        JLabel lblTransactionsTitle = new JLabel("<html>TRANSACTIONS<br>MADE</html>");
        lblTransactionsTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTransactionsTitle.setForeground(new Color(201, 42, 42));
        lblTransactionsTitle.setBounds(10, 10, 220, 45);
        transactionsPanelContainer.add(lblTransactionsTitle);

        lblTotalTransactions = new JLabel("0");
        lblTotalTransactions.setFont(new Font("SansSerif", Font.PLAIN, 21));
        lblTotalTransactions.setForeground(new Color(201, 42, 42));
        lblTotalTransactions.setBounds(20, 60, 200, 30);
        lblTotalTransactions.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalTransactions.setHorizontalAlignment(SwingConstants.CENTER);
        transactionsPanelContainer.add(lblTotalTransactions);

        // Most Bought Panel
        mostBoughtPanelContainer.setLayout(null);
        JPanel mostBoughtLine = new JPanel();
        mostBoughtLine.setBackground(new Color(201, 42, 42));
        mostBoughtLine.setBounds(0, 12, 5, 100);
        mostBoughtPanelContainer.add(mostBoughtLine);

        JLabel lblMostBoughtTitle = new JLabel("<html>MOST BOUGHT<br>ITEM</html>");
        lblMostBoughtTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblMostBoughtTitle.setForeground(new Color(201, 42, 42));
        lblMostBoughtTitle.setBounds(10, 10, 220, 45);
        mostBoughtPanelContainer.add(lblMostBoughtTitle);

        lblMostBought = new JLabel("-");
        lblMostBought.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblMostBought.setForeground(new Color(201, 42, 42));
        lblMostBought.setBounds(20, 60, 200, 30);
        lblMostBought.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMostBought.setHorizontalAlignment(SwingConstants.CENTER);
        mostBoughtPanelContainer.add(lblMostBought);

        add(salesPanelContainer);
        add(transactionsPanelContainer);
        add(mostBoughtPanelContainer);

        setupButtons();
        setupFilterAndSearch();
        setupTablePanel();
        loadSavedLogs();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        updateFilteredOverview(selectedMonth); // Initial update
    }

    private void setupButtons() {
        pnlbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlbtn.setBounds(20, 165, 490, 26);
        pnlbtn.setOpaque(false);

        createnew = createButton("ADD", pnlbtn);
        load = createButton("LOAD", pnlbtn);
        delete = createButton("DELETE", pnlbtn);

        add(pnlbtn);
    }

    private JButton createButton(String text, JPanel container) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.white);
        btn.setBackground(new Color(201, 42, 42));
        btn.setPreferredSize(new Dimension(90, 26));
        btn.addActionListener(this);
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(10, 0)));
        return btn;
    }

    private void setupFilterAndSearch() {
        JPanel filterPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        filterPanel.setBounds(503, 165, 280, 26);
        filterPanel.setOpaque(false);

        monthComboBox = new JComboBox<>();
        monthComboBox.addItem("Overall");
        monthComboBox.addActionListener(e -> filterTableByMonthYear((String) monthComboBox.getSelectedItem()));
        filterPanel.add(monthComboBox);

        txtSearch = new JTextField("Search");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        filterPanel.add(txtSearch);

        add(filterPanel);
    }

    private void setupTablePanel() {
        // Table columns
        String[] columns = { "Log Name", "Transaction No.", "Date Created", "Last Modified", "Full Filename" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        logTable.setAutoCreateRowSorter(true);

        // Hide file path column
        if (logTable.getColumnModel().getColumnCount() > 4) {
            logTable.removeColumn(logTable.getColumnModel().getColumn(4));
        }

        // Center-align columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            if (logTable.getColumnModel().getColumnCount() > i) {
                logTable.getColumnModel().getColumn(i).setCellRenderer(center);
            }
        }

        JScrollPane scrollPane = new JScrollPane(logTable);
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(20, 205, 763, 375);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        add(panelTable);
    }

    // Loads log files from the logs directory and populates the table and filter
    private void loadSavedLogs() {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // IMPORTANT: Clear both table model and map before reloading
        monthComboBox.removeAllItems();
        monthComboBox.addItem("Overall");
        tableModel.setRowCount(0);
        fileToMonthYear.clear(); // Ensure the map is cleared before repopulating

        for (File file : files) {
            String filename = file.getName();
            String filepath = file.getPath();
            String modified = sdf.format(file.lastModified());
            String transactionId = "", creationDate = "";

            // Read creation date and transaction number from file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine();
                String dateLine = reader.readLine();
                String transLine = reader.readLine();
                if (dateLine != null && dateLine.contains(",")) {
                    String[] parts = dateLine.split(",");
                    if (parts.length > 1) creationDate = parts[1].trim();
                }
                if (transLine != null && transLine.contains(",")) {
                    String[] parts = transLine.split(",");
                    if (parts.length > 1) transactionId = parts[1].trim();
                }
            } catch (IOException e) {
                System.err.println("Error reading metadata from log file " + file.getName() + ": " + e.getMessage());
            }

            // Fallback to file creation time if needed
            try {
                Path path = file.toPath();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                if (creationDate.isEmpty()) {
                    creationDate = sdf.format(attr.creationTime().toMillis());
                }
            } catch (IOException e) {
                System.err.println("Error reading file creation time for " + file.getName() + ": " + e.getMessage());
            }

            String monthYear = monthYearFormat.format(new Date(file.lastModified())); // Format the last modified date to "MMMMyyyy"
            fileToMonthYear.put(filepath, monthYear); // Store the mapping of file path to month/year
            if (!comboBoxHasItem(monthYear)) monthComboBox.addItem(monthYear);

            tableModel.addRow(new Object[]{filename, transactionId, creationDate, modified, filepath});
        }
    }

    // Checks if the combo box already contains the item
    private boolean comboBoxHasItem(String item) {
        for (int i = 0; i < monthComboBox.getItemCount(); i++) {
            if (monthComboBox.getItemAt(i).equals(item)) // Compare the item with existing items
                return true; // Item found in the combo box
        }
        return false; // Item not found in the combo box
    }

    // Filters the table based on the search field
    private void filterTable() {
        String query = txtSearch.getText().trim().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        logTable.setRowSorter(sorter);
        if (query.isEmpty() || query.equals("search")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) { // Check if any of the first three columns contain the search query
                    for (int i = 0; i <= 2; i++) {
                        String value = entry.getStringValue(i).toLowerCase(); // Get the value of the column
                        if (value.contains(query)) return true; // If it contains the search query, include this row
                    }
                    return false;
                }
            });
        }
        updateFilteredOverview((String) monthComboBox.getSelectedItem()); // Update overview based on filtered data
    }

    // Filters the table by selected month/year
    private void filterTableByMonthYear(String selected) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        logTable.setRowSorter(sorter);
        if ("Overall".equals(selected)) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) { // Check if the file's month/year matches the selected one
                    String filepath = (String) tableModel.getValueAt(entry.getIdentifier(), 4); // Full Filename column
                    return fileToMonthYear.containsKey(filepath) && selected.equals(fileToMonthYear.get(filepath)); // Check if the file's month/year matches the selected one
                }
            });
        }
        updateFilteredOverview(selected);
    }

    // Updates the overview labels (sales, transactions, most bought) based on filtered data
    private void updateFilteredOverview(String selectedMonthYear) {
        double totalSales = 0;
        int totalTransactions = 0;
        HashMap<String, Integer> itemCount = new HashMap<>(); // To count items sold
        int viewRowCount = logTable.getRowCount(); // Number of rows in the view
        Set<String> processedFiles = new HashSet<>(); // To avoid processing the same file multiple times
        for (int i = 0; i < viewRowCount; i++) { // Iterate through the visible rows in the table
            int modelRow = logTable.convertRowIndexToModel(i);
            String filepath = (String) tableModel.getValueAt(modelRow, 4);
            if (!processedFiles.add(filepath)) // Skip if this file has already been processed
                continue;
            File file = new File(filepath); // Get the file from the path
            if (!file.exists() || !file.isFile()) { // Check if the file exists and is a valid file
                fileToMonthYear.remove(filepath);
                continue;
            }
            String monthYear = fileToMonthYear.get(filepath); // Get the month/year for this file
            if (!selectedMonthYear.equals("Overall") && (monthYear == null || !monthYear.equals(selectedMonthYear))) { // If not "Overall", check if the file's month/year matches the selected one
                continue;
            }
            totalTransactions++;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCounter = 0;
                while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    if (lineCounter <= 3 || line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length == 0 || parts[0].trim().equalsIgnoreCase("Subtotal")
                            || parts[0].trim().equalsIgnoreCase("Total")
                            || parts[0].trim().equalsIgnoreCase("Discount")
                            || parts[0].trim().isEmpty()) {
                        continue;
                    }
                    if (parts.length >= 4) {
                        String item = parts[0].trim();
                        int qty = 0;
                        double subtotal = 0.0;
                        try {
                            qty = Integer.parseInt(parts[2].trim());
                            String subtotalStr = parts[3].trim().replaceAll("[^\\d.]", "");
                            if (!subtotalStr.isEmpty()) {
                                subtotal = Double.parseDouble(subtotalStr);
                            }
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        totalSales += subtotal;
                        itemCount.put(item, itemCount.getOrDefault(item, 0) + qty);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading log file " + file.getName() + ": " + e.getMessage());
            }
        }
        lblTotalSales.setText("₱" + String.format("%,.2f", totalSales));
        lblTotalTransactions.setText(String.valueOf(totalTransactions));
        String mostBought = "-";
        int maxQty = 0;
        if (!itemCount.isEmpty()) { // Find the most bought item
            for (Map.Entry<String, Integer> entry : itemCount.entrySet()) { // Iterate through the item counts
                if (entry.getValue() > maxQty) { // If this item has a higher quantity than the current max
                    mostBought = entry.getKey();
                    maxQty = entry.getValue();
                }
            }
        }
        lblMostBought.setText(mostBought);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == createnew) {
            new createLog();
            if (parentWindow != null) parentWindow.dispose(); // Close the parent window if it exists
        } else if (e.getSource() == load) {
            int selected = logTable.getSelectedRow();
            if (selected != -1) {
                int modelRow = logTable.convertRowIndexToModel(selected);
                String logname = ((String) tableModel.getValueAt(modelRow, 0)).replace(".csv", "");
                String filepath = (String) tableModel.getValueAt(modelRow, 4);
                String date = extractDateFromCSV(filepath); // Extract the date from the CSV file
                new TransactionFrame(logname, date, filepath).setVisible(true);
                if (parentWindow != null) parentWindow.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log file to load.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == delete) {
            int selectedRowView = logTable.getSelectedRow();
            if (selectedRowView >= 0) {
                int selectedRowModel = logTable.convertRowIndexToModel(selectedRowView);
                String filepath = (String) tableModel.getValueAt(selectedRowModel, 4);
                File fileToDelete = new File(filepath);

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this log?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int restock = JOptionPane.showConfirmDialog(
                            this,
                            "Do you want to return stock to inventory?",
                            "Return Stock?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    boolean shouldRestock = (restock == JOptionPane.YES_OPTION);

                    if (fileToDelete.exists()) {
                        if (shouldRestock) {
                            restockFromDeletedLog(fileToDelete);
                            if (inventorySystem != null) {
                                inventorySystem.refreshInventory();
                            }
                        }
                        if (fileToDelete.delete()) {
                            // Directly remove from table model and map for immediate UI update
                            fileToMonthYear.remove(filepath);
                            tableModel.removeRow(selectedRowModel);

                            // After deleting a row, you might need to adjust the selected row
                            // to prevent issues if the next operation depends on a selected row
                            if (logTable.getRowCount() > 0) {
                                if (selectedRowView < logTable.getRowCount()) {
                                    logTable.setRowSelectionInterval(selectedRowView, selectedRowView);
                                } else {
                                    logTable.setRowSelectionInterval(logTable.getRowCount() - 1, logTable.getRowCount() - 1);
                                }
                            }

                            updateFilteredOverview((String) monthComboBox.getSelectedItem());
                            JOptionPane.showMessageDialog(this, "Log deleted" + (shouldRestock ? " and inventory restocked." : "."), "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete log. Please check file permissions.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // If file not found, remove from internal data structures anyway
                        JOptionPane.showMessageDialog(this, "File not found. It might have been deleted already.", "File Not Found", JOptionPane.WARNING_MESSAGE);
                        fileToMonthYear.remove(filepath);
                        tableModel.removeRow(selectedRowModel); // Remove from table even if file was already gone
                        updateFilteredOverview((String) monthComboBox.getSelectedItem());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log to delete.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Reads the creation date from a CSV log file
    private String extractDateFromCSV(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            reader.readLine();
            String dataLine = reader.readLine();
            if (dataLine != null) {
                String[] values = dataLine.split(",");
                if (values.length >= 2) return values[1].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading date from log file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }

    // Restocks inventory based on a deleted log file
    private void restockFromDeletedLog(File file) {
        try {
            Map<String, String[]> inventoryMap = new LinkedHashMap<>();
            File inventoryFile = new File("Inventory.txt");
            if (inventoryFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            inventoryMap.put(parts[0].trim(), parts);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Inventory.txt not found. Cannot restock.", "Restock Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update inventory map from log
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    if (lineNum <= 3 || line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String itemName = parts[0].trim();
                    if (itemName.equalsIgnoreCase("Subtotal") || itemName.equalsIgnoreCase("Total") ||
                            itemName.equalsIgnoreCase("Discount") || itemName.isEmpty())
                        continue;
                    int quantitySold = 0;
                    try {
                        quantitySold = Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping malformed quantity: " + Arrays.toString(parts));
                        continue;
                    }
                    String[] itemData = inventoryMap.get(itemName);
                    if (itemData != null) {
                        int currentStock = 0;
                        try {
                            currentStock = Integer.parseInt(itemData[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping malformed inventory stock for " + itemName);
                            continue;
                        }
                        int updatedStock = currentStock + quantitySold;
                        itemData[1] = String.valueOf(updatedStock);
                        inventoryMap.put(itemName, itemData);
                        System.out.println("Restocked " + itemName + " by " + quantitySold + " (new stock: " + updatedStock + ")");
                    } else {
                        System.out.println("Log item not found in inventory: " + itemName);
                    }
                }
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(inventoryFile))) {
                for (String[] item : inventoryMap.values()) {
                    writer.println(String.join(",", item));
                }
            }
            System.out.println("Inventory.txt updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating inventory: " + e.getMessage(), "Restock Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Allows MainPanel to update inventory panel after restock
    public void setInventorySystem(InventorySystem1 inventoryPanel) {
        this.inventorySystem = inventoryPanel;
    }
}