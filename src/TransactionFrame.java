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
    private final RoundedPanel panelLogDetails;
    private String transactionNumber;
    private JTextField txtQuantity, txtSearch, TxtNameValue, TxtDateValue, txtTransactionIDValue;
    private JComboBox<Item> comboItem;
    private JLabel  lblTotalValue;
    private JTable table;
    public DefaultTableModel model;
    public JButton btnadd, btnedit, btnremove, btnclear, btnsave, btnClose;
    private boolean saved = true;
    private Map<String, InventoryItemRecord> inventoryMap;
    private TableRowSorter<DefaultTableModel> sorter; // Added for searching

    public TransactionFrame(String name, String date) {
        this(name, date, null);
        setNameAndDate(name, date);
    }

    public TransactionFrame(String logname, String date, String filepath) {
        setTitle("Transaction Panel");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPane = new JPanel(null);
        setContentPane(contentPane);

        // Top-right Close Button
        btnClose = new JButton("Close");
        btnClose.setFocusPainted(false);
        btnClose.setBackground(new Color(201, 42, 42));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBounds(750, 12, 80, 32);
        btnClose.addActionListener(this);
        contentPane.add(btnClose);


        panelLogDetails = new RoundedPanel(25);
        panelLogDetails.setLayout(null);
        panelLogDetails.setBounds(35, 45, 365, 220);
        panelLogDetails.setBackground(Color.white);

        // Subheader (optional)
        JLabel subheaderSelectedItem = new JLabel("Transaction Details");
        subheaderSelectedItem.setBounds(20, 15, 300, 25);
        subheaderSelectedItem.setFont(new Font("Arial", Font.BOLD, 16));
        panelLogDetails.add(subheaderSelectedItem);

// Inner panel
        JPanel panelinside = new JPanel(new GridBagLayout());
        panelinside.setBounds(20, 48, 320, 145);
        panelinside.setOpaque(false); // Make sure the background looks like the parent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;

        JLabel lblName = new JLabel("LOG NAME:");
        lblName.setPreferredSize(new Dimension(120, 28));
        TxtNameValue = new JTextField("");
        TxtNameValue.setEditable(false);
        TxtNameValue.setPreferredSize(new Dimension(140, 28));

        JLabel lblDate = new JLabel("DATE:");
        lblDate.setPreferredSize(new Dimension(120, 28));
        TxtDateValue = new JTextField("");
        TxtDateValue.setPreferredSize(new Dimension(150, 28));
        TxtDateValue.setEditable(false);

        JLabel lblTransactionID = new JLabel("TRANSACTION NO:");
        String generatedID = generateRandomTransactionID();
        txtTransactionIDValue = new JTextField(generatedID); // <-- THIS IS THE LINE TO CHANGE
        txtTransactionIDValue.setEditable(false);
        txtTransactionIDValue.setPreferredSize(new Dimension(140, 28));

// Add components to the inside panel
        gbc.gridx = 0; gbc.gridy = 0;
        panelinside.add(lblName, gbc);
        gbc.gridx = 1;
        panelinside.add(TxtNameValue, gbc);

        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 1;
        panelinside.add(lblDate, gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        panelinside.add(TxtDateValue, gbc);

        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 2;
        panelinside.add(lblTransactionID, gbc);
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        panelinside.add(txtTransactionIDValue, gbc);

// Add inner panel to outer panel using BorderLayout.CENTER for perfect fit
        panelLogDetails.add(panelinside);
        contentPane.add(panelLogDetails);



        RoundedPanel panelItemInput = new RoundedPanel(25);
        panelItemInput.setBounds(35, 320, 365, 150); // Move so it doesn't overlap panelLogDetails and is in the visible area
        panelItemInput.setBackground(new Color(201, 42, 42)); // Give it a visible background
        panelItemInput.setLayout(null); // Use absolute positioning for child panel

// Inner grid panel for input fields
        JPanel panelItemGrid = new JPanel(new GridBagLayout());
        panelItemGrid.setBounds(15, 15, 335, 120); // Position within panelItemInput
        panelItemGrid.setOpaque(false); // Let the rounded panel background show through

// Add components to panelItemGrid as before
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 10, 5, 10);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;

        JLabel lblItem = new JLabel("ITEM:");
        lblItem.setFont(new Font("Arial", Font.BOLD, 13));
        lblItem.setForeground(Color.WHITE);
        comboItem = new JComboBox<>();
        for (Item item : loadInventoryItems()) comboItem.addItem(item);
        if (comboItem.getItemCount() > 0) comboItem.setSelectedIndex(0);
        comboItem.setPreferredSize(new Dimension(140, 30));

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.anchor = GridBagConstraints.EAST;
        panelItemGrid.add(lblItem, gbc2);
        gbc2.gridy = 1; gbc2.anchor = GridBagConstraints.WEST;
        panelItemGrid.add(comboItem, gbc2);

        JLabel lblQuantity = new JLabel("QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setForeground(Color.WHITE);

        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(140, 30));
        Inputvalidator.makeNumericOnly(txtQuantity);

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.anchor = GridBagConstraints.EAST;
        panelItemGrid.add(lblQuantity, gbc2);
        gbc2.gridy = 3; gbc2.anchor = GridBagConstraints.WEST;
        panelItemGrid.add(txtQuantity, gbc2);

// Add the grid to the rounded panel and the rounded panel to the content pane
        panelItemInput.add(panelItemGrid);
        contentPane.add(panelItemInput);

        // Input and Search Panel
        JPanel panelInputMain = new JPanel(new BorderLayout());
        panelInputMain.setBounds(10, 190, 565, 35);
        panelInputMain.setBorder(BorderFactory.createEtchedBorder());
        panelInputMain.setBackground(Color.WHITE);
        contentPane.add(panelInputMain);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelBtn.setBackground(Color.WHITE);
        btnadd = new JButton("ADD");
        btnremove = new JButton("REMOVE");
        btnclear = new JButton("CLEAR");
        btnsave = new JButton("SAVE");
        btnadd.addActionListener(this);
        btnremove.addActionListener(this);
        btnclear.addActionListener(this);
        btnsave.addActionListener(this);
        panelBtn.add(btnadd);
        panelBtn.add(btnremove);
        panelBtn.add(btnclear);
        panelBtn.add(btnsave);

        JPanel panelSearch = new JPanel();
        panelSearch.setLayout(new BoxLayout(panelSearch, BoxLayout.X_AXIS));
        panelSearch.setBackground(Color.WHITE);
        panelSearch.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
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

        panelSearch.add(Box.createHorizontalGlue());
        panelSearch.add(txtSearch);

        panelInputMain.add(panelBtn, BorderLayout.WEST);
        panelInputMain.add(panelSearch, BorderLayout.EAST);

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
        panelTable.setBounds(200, 240, 565, 330);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panelTable);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBounds(10, 580, 565, 30);
        panelTotal.setBackground(Color.WHITE);
        panelTotal.setBorder(BorderFactory.createLineBorder(new Color(201, 42, 42)));
        JLabel lblTotalText = new JLabel("TOTAL: ");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalValue = new JLabel("\u20B10.00");
        lblTotalValue.setFont(new Font("Arial", Font.BOLD, 16));
        panelTotal.add(lblTotalText);
        panelTotal.add(lblTotalValue);
        contentPane.add(panelTotal);

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
        TxtNameValue.setText(name);
        TxtDateValue.setText(date);
    }

    private void updateTotal() {
        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);
            try {
                total += Double.parseDouble(value.toString());
            } catch (NumberFormatException ignored) {}
        }
        lblTotalValue.setText("\u20B1" + String.format("%.2f", total));
    }

    private void searchTable(String query) {
        if (query.equals("Search..") || query.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0));
            } catch (java.util.regex.PatternSyntaxException e) {
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
        } else if (source == btnClose) {
            if (this.saved) {
                new Menu();
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
        String filename = "logs/" + TxtNameValue.getText() + ".csv";
        File file = new File(filename);
        TransactionFileManager.saveToFile(file, TxtNameValue.getText(), TxtDateValue.getText(),txtTransactionIDValue.getText(), model);
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
            txtTransactionIDValue.setText(transactionNumber);
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

    private String generateRandomTransactionID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransactionFrame frame = new TransactionFrame("SampleLog", "2025-06-11", (args.length > 0 ? args[0] : null));
            frame.setVisible(true);
        });
    }
}