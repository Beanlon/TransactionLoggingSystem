import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import utils.InventoryManager;
import utils.Item;

public class MainPanel extends JPanel implements ActionListener {
    private InventorySystem inventorySystem;
    JPanel pnlbtn;
    JButton createnew, load, delete;
    JTable logTable;
    DefaultTableModel tableModel;
    private JLabel lblTotalSales;
    private JLabel lblTotalTransactions;
    private JLabel lblMostBought;

    public MainPanel() {
        setLayout(null);
        
        

        // Overview panels
        JPanel paneloverview = new JPanel(new GridLayout(1, 3, 10, 0));
        paneloverview.setBounds(10, 10, 765, 125);
        add(paneloverview);

        lblTotalSales = createInfoPanel("Total Sales", "₱0.00", paneloverview);
        lblTotalTransactions = createInfoPanel("Total Transactions", "0", paneloverview);
        lblMostBought = createInfoPanel("Most Bought Item", "-", paneloverview);
        // Button panel
        pnlbtn = new JPanel();
        pnlbtn.setBounds(10, 140, 480, 26);
        pnlbtn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlbtn.setOpaque(false);

        createnew = new JButton("ADD");
        createnew.setPreferredSize(new Dimension(90, 26));
        createnew.addActionListener(this);

        load = new JButton("LOAD");
        load.setPreferredSize(new Dimension(90, 26));
        load.addActionListener(this);

        delete = new JButton("DELETE");
        delete.setPreferredSize(new Dimension(90, 26));
        delete.addActionListener(this);

        pnlbtn.add(createnew);
        pnlbtn.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlbtn.add(load);
        pnlbtn.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlbtn.add(delete);
        add(pnlbtn);

        // Search panel
        JPanel Searchfilter = new JPanel();
        Searchfilter.setBounds(495, 140, 280, 26);
        Searchfilter.setLayout(new GridLayout(1, 2, 5, 0));
        Searchfilter.setOpaque(false);

        JComboBox<String> month = new JComboBox<>();
        Searchfilter.add(month);

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
        Searchfilter.add(txtSearch);
        add(Searchfilter);

        // Table
        String[] columns = { "Log Name", "Transaction No.", "Date Created", "Last Modified", "Full Filename" };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        logTable.removeColumn(logTable.getColumnModel().getColumn(4)); // hide full filename

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) logTable.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scrollPane = new JScrollPane(logTable);
        JPanel paneltable = new JPanel(new BorderLayout());
        paneltable.setBounds(10, 175, 765, 375);
        paneltable.add(scrollPane);
        add(paneltable);

        loadSavedLogs();
        updateOverviewStats();
    }

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

    private void loadSavedLogs() {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files != null) {
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
                        FileTime creationTime = attr.creationTime();
                        creationDate = sdf.format(creationTime.toMillis());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tableModel.addRow(new Object[]{filename, transactionNo, creationDate, modified, filepath});
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (e.getSource() == createnew) {
            new createLog();
            if (parentWindow != null) parentWindow.dispose();

        } else if (e.getSource() == load) {
            int selectedrow = logTable.getSelectedRow();
            if (selectedrow != -1) {
                int modelRow = logTable.convertRowIndexToModel(selectedrow);
                String logname = ((String) tableModel.getValueAt(modelRow, 0)).replace(".csv", "");
                String filepath = (String) tableModel.getValueAt(modelRow, 4);
                String date = extractDateFromCSV(filepath); // ✅ Get the date from CSV

                new TransactionFrame(logname, date, filepath).setVisible(true); // ✅ Fixed
                if (parentWindow != null) parentWindow.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a log file to load.");
            }

        } else if (e.getSource() == delete) {
            int selectedRow = logTable.getSelectedRow();
            if (selectedRow >= 0) {
                String filepath = (String) tableModel.getValueAt(selectedRow, 4);
                File fileToDelete = new File(filepath);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this log?\nInventory will be restocked.",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (fileToDelete.exists()) {
                        restockFromDeletedLog(fileToDelete); // ✅ Restock inventory first
                        inventorySystem.refreshInventory();

                        if (fileToDelete.delete()) {
                            tableModel.removeRow(selectedRow);
                            updateOverviewStats();
                            JOptionPane.showMessageDialog(this, "Log file deleted and inventory restocked.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete log file.");
                        }
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

    } private void restockFromDeletedLog(File deletedFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(deletedFile))) {
            String line;
            int lineCounter = 0;

            while ((line = reader.readLine()) != null) {
                lineCounter++;
                if (lineCounter <= 3 || line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String itemName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[2].trim());
                    // Call InventoryManager to restock
                    utils.InventoryManager.restockItem(itemName, quantity);
                }
            }
            // Save the updated inventory to file
            InventoryManager.saveInventory(InventoryManager.loadInventory());

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error restocking inventory from deleted log.");
        }
    } private void updateOverviewStats() {
        double totalSales = 0;
        int totalTransactions = 0; // now counts log files
        HashMap<String, Integer> itemCount = new HashMap<>();

        File dir = new File("logs");
        if (!dir.exists()) return;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files != null) {
            totalTransactions = files.length; // ✅ counts number of logs

            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineCounter = 0;
                    while ((line = reader.readLine()) != null) {
                        lineCounter++;
                        if (lineCounter <= 3 || line.trim().isEmpty()) continue;

                        String[] parts = line.split(",");
                        if (parts.length >= 4) {
                            String itemName = parts[0].trim();
                            int quantity = Integer.parseInt(parts[2].trim());
                            double price = Double.parseDouble(parts[3].trim());

                            totalSales += price * quantity;
                            itemCount.put(itemName, itemCount.getOrDefault(itemName, 0) + quantity);
                        }
                    }
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        lblTotalSales.setText("₱" + String.format("%,.2f", totalSales));
        lblTotalTransactions.setText(String.valueOf(totalTransactions)); // ✅ now shows number of CSV logs

        String mostBought = "-";
        int max = 0;
        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            if (entry.getValue() > max) {
                mostBought = entry.getKey();
                max = entry.getValue();
            }
        }
        lblMostBought.setText(mostBought);
    }

    public void setInventorySystem(InventorySystem inventoryPanel) {
        this.inventorySystem = inventoryPanel; // ✅ Correct: assign parameter to the field
    }
}
