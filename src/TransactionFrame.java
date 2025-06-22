// Full updated TransactionFrame.java with total panel support and search functionality

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import utils.Inputvalidator;
import utils.InventoryItemRecord;
import utils.Item;
import utils.TransactionFileManager;
import utils.TransactionFileManager.TransactionData;

public class TransactionFrame extends JFrame implements ActionListener {
    private String transactionNumber;
    private JTextField txtQuantity, txtSearch;
    private JComboBox<Item> comboItem;
    private JLabel lblNameValue, lblDateValue, line2, lblTotalValue;
    private JTable table;
    public DefaultTableModel model;
    public JButton btnadd, btnedit, btnremove, btnclear, btnsave, btnBack;
    private boolean saved = true;
    private Map<String, InventoryItemRecord> inventoryMap;
    private TableRowSorter<DefaultTableModel> sorter; // Added for searching

    public TransactionFrame(String name, String date) {
        this(name, date, null);
        setNameAndDate(name, date);
    }

    public TransactionFrame(String logname, String date, String filepath) {
        setTitle("Transaction Panel");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainWrapper = new JPanel(new BorderLayout());
        setContentPane(mainWrapper);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(220, 53, 69));
        sidebar.setPreferredSize(new Dimension(100, getHeight()));
        sidebar.setLayout(new GridBagLayout());

        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(80, 30));
        btnBack.setFocusPainted(false);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.BLACK);
        btnBack.addActionListener(this);
        sidebar.add(btnBack);
        mainWrapper.add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(null);
        content.setBackground(Color.WHITE);
        mainWrapper.add(content, BorderLayout.CENTER);

        JPanel logdetails = new JPanel(null);
        logdetails.setBounds(10, 10, 565, 60);
        logdetails.setBackground(new Color(246, 241, 241));
        logdetails.setBorder(BorderFactory.createLineBorder(new Color(201, 42, 42)));

        JLabel lblName = new JLabel("LOG NAME:");
        lblName.setBounds(30, 2, 150, 60);
        lblName.setFont(new Font("Arial", Font.BOLD, 20));
        lblNameValue = new JLabel("");
        lblNameValue.setBounds(150, 2, 150, 60);
        lblNameValue.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblDate = new JLabel("DATE:");
        lblDate.setBounds(310, 2, 150, 60);
        lblDate.setFont(new Font("Arial", Font.BOLD, 20));
        lblDateValue = new JLabel("");
        lblDateValue.setBounds(380, 2, 150, 60);
        lblDateValue.setFont(new Font("Arial", Font.BOLD, 20));

        logdetails.add(lblName);
        logdetails.add(lblNameValue);
        logdetails.add(lblDate);
        logdetails.add(lblDateValue);
        content.add(logdetails);

        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        panelInfo.setBounds(10, 80, 565, 150);
        content.add(panelInfo);

        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(new Color(246, 241, 241));
        JLabel line1 = new JLabel("TRANSACTION NO:");
        line2 = new JLabel(generateRandomTransactionNumber());
        transactionNumber = line2.getText();

        line1.setBounds(17, 7, 300, 100);
        line2.setBounds(100, 50, 300, 100);
        line1.setFont(new Font("Arial", Font.BOLD, 26));
        line2.setFont(new Font("Arial", Font.BOLD, 35));

        leftPanel.add(line1);
        leftPanel.add(line2);
        panelInfo.add(leftPanel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(201, 42, 42));
        panelInfo.add(rightPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblItem = new JLabel("ITEM:");
        lblItem.setFont(new Font("Arial", Font.BOLD, 13));
        lblItem.setForeground(Color.WHITE);
        comboItem = new JComboBox<>();
        for (Item item : loadInventoryItems()) comboItem.addItem(item);
        if (comboItem.getItemCount() > 0) comboItem.setSelectedIndex(0);
        comboItem.setPreferredSize(new Dimension(140, 30));

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblItem, gbc);
        gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(comboItem, gbc);

        JLabel lblQuantity = new JLabel("QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setForeground(Color.WHITE);

        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(140, 30));
        Inputvalidator.makeNumericOnly(txtQuantity);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblQuantity, gbc);
        gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtQuantity, gbc);

        JPanel panelInputmain = new JPanel(new BorderLayout());
        panelInputmain.setBounds(10, 240, 565, 35);
        panelInputmain.setBorder(BorderFactory.createEtchedBorder());
        panelInputmain.setBackground(Color.WHITE);
        content.add(panelInputmain);

        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelbtn.setBackground(Color.WHITE);
        btnadd = new JButton("ADD");
        btnremove = new JButton("REMOVE");
        btnclear = new JButton("CLEAR");
        btnsave = new JButton("SAVE");
        btnadd.addActionListener(this);
        btnremove.addActionListener(this);
        btnclear.addActionListener(this);
        btnsave.addActionListener(this);
        panelbtn.add(btnadd);
        panelbtn.add(btnremove);
        panelbtn.add(btnclear);
        panelbtn.add(btnsave);

        JPanel panelsearch = new JPanel();
        panelsearch.setLayout(new BoxLayout(panelsearch, BoxLayout.X_AXIS));
        panelsearch.setBackground(Color.WHITE);
        panelsearch.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        txtSearch = new JTextField("Search..");
        txtSearch.setPreferredSize(new Dimension(150, 30));
        txtSearch.setMaximumSize(new Dimension(150, 30));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search") || txtSearch.getText().equals("Search..")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Search..");
                }
            }
        });

        // Add DocumentListener to txtSearch for live filtering
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTable(txtSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTable(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTable(txtSearch.getText());
            }
        });


        panelsearch.add(Box.createHorizontalGlue());
        panelsearch.add(txtSearch);

        panelInputmain.add(panelbtn, BorderLayout.WEST);
        panelInputmain.add(panelsearch, BorderLayout.EAST);

        String[] columnNames = {"ITEM", "PRICE", "QUANTITY", "SUBTOTAL"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model); // Initialize sorter
        table.setRowSorter(sorter); // Set sorter on the table

        JScrollPane scrollPane = new JScrollPane(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(10, 285, 565, 330);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        content.add(panelTable);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBounds(10, 620, 565, 30);
        panelTotal.setBackground(Color.WHITE);
        panelTotal.setBorder(BorderFactory.createLineBorder(new Color(201, 42, 42)));
        JLabel lblTotalText = new JLabel("TOTAL: ");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalValue = new JLabel("\u20B10.00");
        lblTotalValue.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotalText);
        panelTotal.add(lblTotalValue);
        content.add(panelTotal);

        if (filepath != null && !filepath.isEmpty()) {
            loadFromFile(filepath);
        }
    }

    private List<Item> loadInventoryItems() {
        List<Item> items = new ArrayList<>();
        inventoryMap = InventoryItemRecord.loadInventory();
        for (InventoryItemRecord inv : inventoryMap.values()) {
            items.add(new Item(inv.getName(), inv.getPrice(), inv.getQuantity()));
        }
        return items;
    }

    public void setNameAndDate(String name, String date) {
        lblNameValue.setText(name);
        lblDateValue.setText(date);
    }

    private void updateTotal() {
        double total = 0.0;
        // Iterate through the actual rows in the table model, not the visible rows if filtered
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);
            try {
                total += Double.parseDouble(value.toString());
            } catch (NumberFormatException ignored) {}
        }
        lblTotalValue.setText("\u20B1" + String.format("%.2f", total));
    }

    // New method for searching the table
    private void searchTable(String query) {
        if (query.equals("Search..") || query.isEmpty()) { // Reset filter if default text or empty
            sorter.setRowFilter(null);
        } else {
            try {
                // Apply a regular expression filter to the first column (ITEM)
                // Pattern.CASE_INSENSITIVE makes the search case-insensitive
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0));
            } catch (java.util.regex.PatternSyntaxException e) {
                // Handle potential regex errors (e.g., if the user types an invalid regex)
                sorter.setRowFilter(null);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnadd) {
            Item selectedItem = (Item) comboItem.getSelectedItem();
            String quantity = txtQuantity.getText().trim();
            if (selectedItem == null || quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.");
                return;
            }
            int quantityInt = Integer.parseInt(quantity);
            if (quantityInt <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }
            if (quantityInt > selectedItem.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Not enough stock. Available: " + selectedItem.getQuantity());
                return;
            }
            double subtotal = selectedItem.getPrice() * quantityInt;

            // Check if item already exists in the table to update quantity and subtotal
            boolean itemFound = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(selectedItem.getName())) {
                    int currentQuantity = (int) model.getValueAt(i, 2);
                    double currentSubtotal = (double) model.getValueAt(i, 3);

                    model.setValueAt(currentQuantity + quantityInt, i, 2); // Update quantity
                    model.setValueAt(currentSubtotal + subtotal, i, 3);    // Update subtotal
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                model.addRow(new Object[]{selectedItem.getName(), selectedItem.getPrice(), quantityInt, subtotal});
            }

            selectedItem.reduceQuantity(quantityInt);
            InventoryItemRecord inv = inventoryMap.get(selectedItem.getName());
            if (inv != null) inv.reduceQuantity(quantityInt);
            int selectedIndex = comboItem.getSelectedIndex();
            ((DefaultComboBoxModel<Item>) comboItem.getModel()).removeElementAt(selectedIndex);
            ((DefaultComboBoxModel<Item>) comboItem.getModel()).insertElementAt(selectedItem, selectedIndex);
            comboItem.setSelectedIndex(selectedIndex);
            clearInputs();
            saved = false;
            updateTotal();
        } else if (source == btnremove) {
            int selectedRowView = table.getSelectedRow();
            if (selectedRowView == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            // Convert view row index to model row index, important if table is filtered/sorted
            int selectedModelRow = table.convertRowIndexToModel(selectedRowView);

            // Get item name and quantity from selected row (from the model, not view)
            String itemName = model.getValueAt(selectedModelRow, 0).toString();
            int quantity = Integer.parseInt(model.getValueAt(selectedModelRow, 2).toString());

            // Restore quantity in inventoryMap
            InventoryItemRecord inv = inventoryMap.get(itemName);
            if (inv != null) {
                inv.addQuantity(quantity);
            }

            // Restore quantity in comboItem (the JComboBox)
            for (int i = 0; i < comboItem.getItemCount(); i++) {
                Item item = comboItem.getItemAt(i);
                if (item.getName().equals(itemName)) {
                    item.addQuantity(quantity);
                    // Force the ComboBox to refresh the display
                    ((DefaultComboBoxModel<Item>) comboItem.getModel()).removeElementAt(i);
                    ((DefaultComboBoxModel<Item>) comboItem.getModel()).insertElementAt(item, i);
                    break;
                }
            }

            // Remove row from table model
            model.removeRow(selectedModelRow);
            clearInputs();
            saved = false;
            updateTotal();

        } else if (source == btnclear) {
            clearInputs();
        } else if (source == btnsave) {
            try {
                saveToFile();
                saved = true;
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        } else if (source == btnBack) {
            if (this.saved) {
                new Menu(); // Assuming Menu is another JFrame or class you want to go back to
                dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        saveToFile();
                        new Menu();
                        dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    new Menu();
                    dispose();
                }
            }
        }
    }

    private void clearInputs() {
        txtQuantity.setText("");
        if (comboItem.getItemCount() > 0) {
            comboItem.setSelectedIndex(0);
        }
    }

    public void saveToFile() throws IOException {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdir();
        String filename = "logs/" + lblNameValue.getText() + ".csv";
        File file = new File(filename);
        TransactionFileManager.saveToFile(file, lblNameValue.getText(), lblDateValue.getText(), transactionNumber, model);
        InventoryItemRecord.saveInventory(inventoryMap);
    }

    public void loadFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "File not found: " + filename);
                return;
            }
            TransactionData data = TransactionFileManager.loadFromFile(file);
            setNameAndDate(data.name, data.date);
            transactionNumber = data.transactionNumber;
            line2.setText(transactionNumber);
            model.setRowCount(0); // Clear existing rows
            for (String[] row : data.rows) {
                if (row.length == model.getColumnCount()) {
                    model.addRow(row);
                }
            }
            updateTotal();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    private String generateRandomTransactionNumber() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransactionFrame frame = new TransactionFrame("SampleLog", "2025-06-11", (args.length > 0 ? args[0] : null));
            frame.setVisible(true);
        });
    }
}