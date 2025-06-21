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
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMMyyyy"); // Corrected to use 'yyyy' for year

    public MainPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(785, 630));

        setupOverviewPanel();
        setupButtons();
        setupFilterAndSearch();
        setupTablePanel();
        loadSavedLogs();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        updateFilteredOverview(selectedMonth);
    }

    private void setupOverviewPanel() {
        int panelOverviewX = 20;
        int panelOverviewY = 20;
        int panelOverviewWidth = 765;
        int panelOverviewHeight = 125;
        int gap = 17    ;
        int infoPanelWidth = (panelOverviewWidth - (2 * gap)) / 3;

        int cornerRadius = 25;

        RoundedPanel salesPanelContainer = new RoundedPanel(cornerRadius);
        RoundedPanel transactionsPanelContainer = new RoundedPanel(cornerRadius);
        RoundedPanel mostBoughtPanelContainer = new RoundedPanel(cornerRadius);

        salesPanelContainer.setBounds(panelOverviewX, panelOverviewY, infoPanelWidth, panelOverviewHeight);
        transactionsPanelContainer.setBounds(panelOverviewX + infoPanelWidth + gap, panelOverviewY, infoPanelWidth, panelOverviewHeight);
        mostBoughtPanelContainer.setBounds(panelOverviewX + (infoPanelWidth + gap) * 2, panelOverviewY, infoPanelWidth, panelOverviewHeight);

        salesPanelContainer.setBackground(Color.WHITE);
        transactionsPanelContainer.setBackground(Color.WHITE);
        mostBoughtPanelContainer.setBackground(Color.WHITE);

        add(salesPanelContainer);
        add(transactionsPanelContainer);
        add(mostBoughtPanelContainer);

        lblTotalSales = createInfoPanel("Total Sales", "₱0.00", salesPanelContainer);
        lblTotalTransactions = createInfoPanel("Total Transactions", "0", transactionsPanelContainer);
        lblMostBought = createInfoPanel("Most Bought Item", "-", mostBoughtPanelContainer);
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
        String[] columns = { "Log Name", "Transaction No.", "Date Created", "Last Modified", "Full Filename" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        logTable.setAutoCreateRowSorter(true);

        if (logTable.getColumnModel().getColumnCount() > 4) {
            logTable.removeColumn(logTable.getColumnModel().getColumn(4));
        }

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

    private JLabel createInfoPanel(String title, String value, JPanel container) {
        container.setLayout(null); // Ensure the container uses null layout

        int containerHeight = container.getHeight();
        int lineHeight = 100;
        int y = (containerHeight - lineHeight) / 2;

        JPanel line = new JPanel();
        line.setBackground(new Color(201, 42, 42));
        line.setBounds(0, y, 5, lineHeight); // Vertically centered
        container.add(line);

        // 2. Create the content panel for labels
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        // content.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // Remove fixed border for flexible centering
        content.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 23));
        lblTitle.setForeground(new Color(201, 42, 42));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblValue.setForeground(new Color(201, 42, 42));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(lblValue);

        // Calculate preferred height of the content to center it
        content.setPreferredSize(content.getPreferredSize()); // Ensure preferred size is calculated
        int contentWidth = container.getWidth() - 5; // Remaining width after the line
        int contentHeight = content.getPreferredSize().height; // Get the height the content would naturally take

        // Calculate the Y position to center it
        int yPos = (container.getHeight() - contentHeight) / 2;
        if (yPos < 0) yPos = 0; // Ensure it doesn't go off the top if content is too tall

        content.setBounds(5, yPos, contentWidth, contentHeight);
        container.add(content);

        return lblValue;
    }

    private void loadSavedLogs() {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (File file : files) {
            String filename = file.getName();
            String filepath = file.getPath();
            String modified = sdf.format(file.lastModified());

            String transactionNo = "", creationDate = "";

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
                    if (parts.length > 1) transactionNo = parts[1].trim();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Path path = file.toPath();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                if (creationDate.isEmpty()) {
                    creationDate = sdf.format(attr.creationTime().toMillis());
                }
            } catch (IOException e) {
                e.printStackTrace();
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

        if (query.isEmpty() || query.equals("search..")) {
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
                    return selected.equals(fileToMonthYear.get(filepath));
                }
            });
        }

        updateFilteredOverview(selected);
    }

    private void updateFilteredOverview(String selectedMonthYear) {
        double totalSales = 0;
        int totalTransactions = 0;
        HashMap<String, Integer> itemCount = new HashMap<>();

        for (Map.Entry<String, String> entry : fileToMonthYear.entrySet()) {
            String filepath = entry.getKey();
            String monthYear = entry.getValue();

            if (!selectedMonthYear.equals("Overall") && !monthYear.equals(selectedMonthYear)) continue;

            File file = new File(filepath);
            if (file.exists() && file.isFile()) {
                totalTransactions++;
            } else {
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCounter = 0;
                while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    if (lineCounter <= 3 || line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String item = parts[0].trim();
                        int qty = 0;
                        double price = 0.0;
                        try {
                            qty = Integer.parseInt(parts[2].trim());
                            price = Double.parseDouble(parts[3].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed data in CSV for quantity or price in file " + file.getName() + ": " + line);
                            continue;
                        }

                        totalSales += (double) qty * price;
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

    public void actionPerformed(ActionEvent e) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (e.getSource() == createnew) {
            new createLog();
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
                JOptionPane.showMessageDialog(this, "Please select a log file to load.");
            }

        } else if (e.getSource() == delete) {
            int selectedRowView = logTable.getSelectedRow();
            if (selectedRowView >= 0) {
                int selectedRowModel = logTable.convertRowIndexToModel(selectedRowView);
                String filepath = (String) tableModel.getValueAt(selectedRowModel, 4);
                File fileToDelete = new File(filepath);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this log?\nInventory will be restocked.",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION && fileToDelete.exists()) {
                    restockFromDeletedLog(fileToDelete);
                    if (inventorySystem != null) {
                        //inventorySystem.refreshInventory();
                    }
                    if (fileToDelete.delete()) {
                        tableModel.removeRow(selectedRowModel);
                        fileToMonthYear.remove(filepath);
                        updateFilteredOverview((String) monthComboBox.getSelectedItem());
                        JOptionPane.showMessageDialog(this, "Log deleted and inventory restocked.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete log.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log to delete.");
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
        }
        return "";
    }

    private void restockFromDeletedLog(File file) {
        try {
            // Step 1: Load inventory into a Map
            Map<String, String[]> inventoryMap = new LinkedHashMap<>();
            File inventoryFile = new File("PurchaseRecords.txt");
            if (inventoryFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(inventoryFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            inventoryMap.put(parts[0].trim(), parts);
                        }
                    }
                }
            }

            // Step 2: Read deleted log and restock
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCount = 0;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    if (lineCount <= 3 || line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String itemName = parts[0].trim();
                        int quantity = Integer.parseInt(parts[2].trim());

                        if (inventoryMap.containsKey(itemName)) {
                            String[] itemData = inventoryMap.get(itemName);
                            int currentStock = Integer.parseInt(itemData[2].trim());
                            itemData[2] = String.valueOf(currentStock + quantity);
                            itemData[4] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // update restock date
                        }
                    }
                }
            }

            // Step 3: Save updated inventory back
            try (PrintWriter writer = new PrintWriter(new FileWriter("PurchaseRecords.txt"))) {
                for (String[] item : inventoryMap.values()) {
                    writer.println(String.join(",", item));
                }
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error restocking inventory: " + e.getMessage());
        }
    }

    public void setInventorySystem(InventorySystem1 inventoryPanel) {
        this.inventorySystem = inventoryPanel;
    }
}