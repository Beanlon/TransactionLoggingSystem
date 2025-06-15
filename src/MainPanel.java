import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.*;
import java.util.*;
import javax.swing.table.*;

import utils.InventoryManager;
import utils.Item;

public class MainPanel extends JPanel implements ActionListener {

    // ========== Fields ==========
    private InventorySystem inventorySystem;
    private JPanel pnlbtn;
    private JButton createnew, load, delete;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSales, lblTotalTransactions, lblMostBought;
    private JComboBox<String> monthComboBox;
    private final Map<String, String> fileToMonthYear = new HashMap<>();
    private final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");

    // ========== Constructor ==========
    public MainPanel() {
        setLayout(null);
        setupOverviewPanel();
        setupButtons();
        setupFilterAndSearch();
        setupTablePanel();
        loadSavedLogs();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        updateFilteredOverview(selectedMonth);
    }

    // ========== UI Setup ==========
    private void setupOverviewPanel() {
        JPanel paneloverview = new JPanel(new GridLayout(1, 3, 10, 0));
        paneloverview.setBounds(10, 10, 765, 125);
        add(paneloverview);

        lblTotalSales = createInfoPanel("Total Sales", "₱0.00", paneloverview);
        lblTotalTransactions = createInfoPanel("Total Transactions", "0", paneloverview);
        lblMostBought = createInfoPanel("Most Bought Item", "-", paneloverview);
    }

    private void setupButtons() {
        pnlbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlbtn.setBounds(10, 140, 480, 26);
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
        filterPanel.setBounds(495, 140, 280, 26);
        filterPanel.setOpaque(false);

        monthComboBox = new JComboBox<>();
        monthComboBox.addItem("Overall");
        monthComboBox.addActionListener(e -> filterTableByMonthYear((String) monthComboBox.getSelectedItem()));
        filterPanel.add(monthComboBox);

        JTextField txtSearch = new JTextField("Search..");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search..")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search..");
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
        logTable.removeColumn(logTable.getColumnModel().getColumn(4)); // hide filename

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) logTable.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scrollPane = new JScrollPane(logTable);
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(10, 175, 765, 375);
        panelTable.add(scrollPane);
        add(panelTable);
    }

    // ========== Info Panel Builder ==========
    private JLabel createInfoPanel(String title, String value, JPanel container) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel line = new JPanel();
        line.setPreferredSize(new Dimension(5, 125));
        line.setBackground(new Color(201, 42, 42));
        panel.add(line, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

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

        panel.add(content, BorderLayout.CENTER);
        container.add(panel);

        return lblValue;
    }

    // ========== Log File Loading ==========
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

            String monthYear = monthYearFormat.format(file.lastModified());
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

    // ========== Filtering and Stats ==========
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

    private void updateOverviewStats() {
        updateFilteredOverview("Overall");
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
            totalTransactions++;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int lineCounter = 0;
                while ((line = reader.readLine()) != null) {
                    lineCounter++;
                    if (lineCounter <= 3 || line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String item = parts[0].trim();
                        int qty = Integer.parseInt(parts[2].trim());
                        double price = Double.parseDouble(parts[3].trim());
                        totalSales += qty * price;
                        itemCount.put(item, itemCount.getOrDefault(item, 0) + qty);
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        lblTotalSales.setText("₱" + String.format("%,.2f", totalSales));
        lblTotalTransactions.setText(String.valueOf(totalTransactions));

        String mostBought = "-";
        int maxQty = 0;
        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            if (entry.getValue() > maxQty) {
                mostBought = entry.getKey();
                maxQty = entry.getValue();
            }
        }
        lblMostBought.setText(mostBought);
    }

    // ========== Action Handling ==========
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (src == createnew) {
            new createLog();
            if (parentWindow != null) parentWindow.dispose();

        } else if (src == load) {
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

        } else if (src == delete) {
            int selectedRow = logTable.getSelectedRow();
            if (selectedRow >= 0) {
                String filepath = (String) tableModel.getValueAt(selectedRow, 4);
                File fileToDelete = new File(filepath);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this log?\nInventory will be restocked.",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION && fileToDelete.exists()) {
                    restockFromDeletedLog(fileToDelete);
                    inventorySystem.refreshInventory();
                    if (fileToDelete.delete()) {
                        tableModel.removeRow(selectedRow);
                        fileToMonthYear.remove(filepath); // Also remove from month filter map
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

    // ========== Helper Methods ==========
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
                    InventoryManager.restockItem(itemName, quantity);
                }
            }
            InventoryManager.saveInventory(InventoryManager.loadInventory());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error restocking inventory.");
        }
    }

    public void setInventorySystem(InventorySystem inventoryPanel) {
        this.inventorySystem = inventoryPanel;
    }
}
