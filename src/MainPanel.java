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

    private JTextField txtSearch;
    private InventorySystem1 inventorySystem;
    private JPanel pnlbtn;
    private JButton createnew, load, delete;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSales, lblTotalTransactions, lblMostBought;
    private JComboBox<String> monthComboBox;
    private final Map<String, String> fileToMonthYear = new HashMap<>();
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMMyyyy");

    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(785, 630));

        RoundedPanel salesPanelContainer = new RoundedPanel(25);
        salesPanelContainer.setBounds(20, 20, 243, 125);
        salesPanelContainer.setBackground(Color.WHITE);

        RoundedPanel transactionsPanelContainer = new RoundedPanel(25);
        transactionsPanelContainer.setBounds(280, 20, 243, 125);
        transactionsPanelContainer.setBackground(Color.WHITE);

        RoundedPanel mostBoughtPanelContainer = new RoundedPanel(25);
        mostBoughtPanelContainer.setBounds(540, 20, 243, 125);
        mostBoughtPanelContainer.setBackground(Color.WHITE);

        // ---- Sales Panel content ----
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

        // ---- Transactions Panel content ----
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

        // ---- Most Bought Panel content ----
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
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { // When the text field gains focus
                if (txtSearch.getText().equals("Search")) { // If the text is "Search", clear it
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) { // When the text field loses focus
                if (txtSearch.getText().isEmpty()) { // If the text field is empty, set it back to "Search"
                    txtSearch.setText("Search");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        filterPanel.add(txtSearch);

        add(filterPanel);
    }

    private void setupTablePanel() { // Creates the table to display logs
        String[] columns = { "Log Name", "Transaction No.", "Date Created", "Last Modified", "Full Filename" }; // Column names for the table
        tableModel = new DefaultTableModel(columns, 0) { //Sets the the table moddel with the column names and the row begins at 0
            public boolean isCellEditable(int row, int column) {
                return false;
            } //tables are not editable
        };

        logTable = new JTable(tableModel); // Creates a new JTable with the table model
        logTable.setRowHeight(25);
        logTable.setAutoCreateRowSorter(true);

        if (logTable.getColumnModel().getColumnCount() > 4) {
            logTable.removeColumn(logTable.getColumnModel().getColumn(4));
        }

        DefaultTableCellRenderer center = new DefaultTableCellRenderer(); // Creates a cell renderer to center-align text in the table cells
        center.setHorizontalAlignment(JLabel.CENTER); // Sets the horizontal alignment of the cell renderer to center
        for (int i = 0; i < 4; i++) { //for every column in the table
            if (logTable.getColumnModel().getColumnCount() > i) { // Checks if the column exists
                logTable.getColumnModel().getColumn(i).setCellRenderer(center); // Sets the cell renderer for the column to center-align text
            }
        }

        JScrollPane scrollPane = new JScrollPane(logTable); //Creates a scroll pane to hold the table
        JPanel panelTable = new JPanel(new BorderLayout()); //Creates a panel to hold the table thats inside a scroll pane
        panelTable.setBounds(20, 205, 763, 375);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        add(panelTable);
    }

    // method for loading saved logs that are creates a new file under a directory
    private void loadSavedLogs() {
        File dir = new File("logs"); // Directory where logs are stored
        if (!dir.exists()) dir.mkdirs(); // Create directory if it doesn't exist

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv")); // Filter for CSV files
        if (files == null) return; //It returns when no files are found

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Creates a date format and last modified date

        monthComboBox.removeAllItems(); //removes all item from the monthcombobox
        monthComboBox.addItem("Overall"); //It always adds "Overall" as the first item

        tableModel.setRowCount(0); // Clear existing table data before repopulating

        for (File file : files) { // for every file in the files array
            String filename = file.getName(); //get the file name
            String filepath = file.getPath(); //get the file path
            String modified = sdf.format(file.lastModified()); //get the last modified date and time of the file

            String transactionNo = "", creationDate = ""; //Leaves empty strings for transaction number and creation date since they are placed during log creation

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Skip header
                String dateLine = reader.readLine(); //Reads this line's date
                String transLine = reader.readLine(); //Reads this line's transaction number/ID

                if (dateLine != null && dateLine.contains(",")) {
                    String[] parts = dateLine.split(",");
                    if (parts.length > 1) creationDate = parts[1].trim();
                }
                if (transLine != null && transLine.contains(",")) {
                    String[] parts = transLine.split(",");
                    if (parts.length > 1) transactionNo = parts[1].trim();
                }
            } catch (IOException e) {
                System.err.println("Error reading metadata from log file " + file.getName() + ": " + e.getMessage());
            }

            try {
                Path path = file.toPath();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                if (creationDate.isEmpty()) {
                    creationDate = sdf.format(attr.creationTime().toMillis());
                }
            } catch (IOException e) {
                System.err.println("Error reading file creation time for " + file.getName() + ": " + e.getMessage());
            }

            String monthYear = monthYearFormat.format(new Date(file.lastModified()));
            fileToMonthYear.put(filepath, monthYear);
            if (!comboBoxHasItem(monthYear)) monthComboBox.addItem(monthYear);

            tableModel.addRow(new Object[]{filename, transactionNo, creationDate, modified, filepath});
        }
    }

    private boolean comboBoxHasItem(String item) {
        for (int i = 0; i < monthComboBox.getItemCount(); i++) {
            if (monthComboBox.getItemAt(i).equals(item)) return true;
        }
        return false;
    }

    private void filterTable() {
        String query = txtSearch.getText().trim().toLowerCase();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        logTable.setRowSorter(sorter);

        if (query.isEmpty() || query.equals("search")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    for (int i = 0; i <= 2; i++) {
                        String value = entry.getStringValue(i).toLowerCase();
                        if (value.contains(query)) return true;
                    }
                    return false;
                }
            });
        }
        updateFilteredOverview((String) monthComboBox.getSelectedItem()); // Update overview after search filter
    }

    private void filterTableByMonthYear(String selected) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        logTable.setRowSorter(sorter);

        if ("Overall".equals(selected)) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    String filepath = (String) tableModel.getValueAt(entry.getIdentifier(), 4);
                    return fileToMonthYear.containsKey(filepath) && selected.equals(fileToMonthYear.get(filepath));
                }
            });
        }
        updateFilteredOverview(selected);
    }

    private void updateFilteredOverview(String selectedMonthYear) {
        double totalSales = 0;
        int totalTransactions = 0;
        HashMap<String, Integer> itemCount = new HashMap<>();

        int viewRowCount = logTable.getRowCount();
        Set<String> processedFiles = new HashSet<>(); // Prevent double-counting

        for (int i = 0; i < viewRowCount; i++) {
            int modelRow = logTable.convertRowIndexToModel(i);
            String filepath = (String) tableModel.getValueAt(modelRow, 4);

            // Only process each file once
            if (!processedFiles.add(filepath)) continue;

            File file = new File(filepath);
            if (!file.exists() || !file.isFile()) {
                fileToMonthYear.remove(filepath);
                continue;
            }

            String monthYear = fileToMonthYear.get(filepath);
            if (!selectedMonthYear.equals("Overall") && (monthYear == null || !monthYear.equals(selectedMonthYear))) {
                continue;
            }

            totalTransactions++;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCounter = 0;
                while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    if (lineCounter <= 3 || line.trim().isEmpty()) {
                        continue; // skip header or empty lines
                    }

                    String[] parts = line.split(",");
                    // Skip non-item lines
                    if (parts.length == 0 || parts[0].trim().equalsIgnoreCase("Subtotal")
                            || parts[0].trim().equalsIgnoreCase("Total")
                            || parts[0].trim().equalsIgnoreCase("Discount")
                            || parts[0].trim().isEmpty()) {
                        continue;
                    }

                    // Only sum the SUBTOTAL column (column 3)
                    if (parts.length >= 4) {
                        String item = parts[0].trim();
                        int qty = 0;
                        double subtotal = 0.0;
                        try {
                            qty = Integer.parseInt(parts[2].trim());
                            // Subtotal is at index 3, parse directly
                            String subtotalStr = parts[3].trim().replaceAll("[^\\d.]", "");
                            if (!subtotalStr.isEmpty()) {
                                subtotal = Double.parseDouble(subtotalStr);
                            }
                        } catch (NumberFormatException e) {
                            continue; // skip malformed lines
                        }
                        totalSales += subtotal; // Use subtotal, not qty * price
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
        if (!itemCount.isEmpty()) {
            for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
                if (entry.getValue() > maxQty) {
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
            JFrame createLogFrame = new createLog();
            createLogFrame.setVisible(true);
            if (parentWindow != null) parentWindow.dispose();

        } else if (e.getSource() == load) {
            int selected = logTable.getSelectedRow();
            if (selected != -1) {
                int modelRow = logTable.convertRowIndexToModel(selected);
                String logname = ((String) tableModel.getValueAt(modelRow, 0)).replace(".csv", "");
                String filepath = (String) tableModel.getValueAt(modelRow, 4);
                String date = extractDateFromCSV(filepath);
                new TransactionFrame(logname, date, filepath).setVisible(true);
                if (parentWindow != null) parentWindow.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log file to load.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }

        }else if (e.getSource() == delete) {
            int selectedRowView = logTable.getSelectedRow();
            if (selectedRowView >= 0) {
                int selectedRowModel = logTable.convertRowIndexToModel(selectedRowView);
                int filePathCol = tableModel.findColumn("Full Filename");
                String filepath = (String) tableModel.getValueAt(selectedRowModel, filePathCol);
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
                            fileToMonthYear.remove(filepath);
                            loadSavedLogs();
                            updateFilteredOverview((String) monthComboBox.getSelectedItem());
                            JOptionPane.showMessageDialog(this, "Log deleted" + (shouldRestock ? " and inventory restocked." : "."), "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete log. Please check file permissions.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "File not found. It might have been deleted already.", "File Not Found", JOptionPane.WARNING_MESSAGE);
                        fileToMonthYear.remove(filepath);
                        loadSavedLogs();
                        updateFilteredOverview((String) monthComboBox.getSelectedItem());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log to delete.", "No Log Selected", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

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

    private void restockFromDeletedLog(File file) {
        try {
            // Step 1: Read inventory into map
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

            // Step 2: Read log and update map
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    if (lineNum <= 3 || line.trim().isEmpty()) continue; // skip header

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

            // Step 3: Write inventory map back to Inventory.txt
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

    public void setInventorySystem(InventorySystem1 inventoryPanel) {
        this.inventorySystem = inventoryPanel;
    }
}