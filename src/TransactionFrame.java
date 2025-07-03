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

import utils.*; // Import all utilities classes
import utils.TransactionFileManager.TransactionData;

public class TransactionFrame extends JFrame implements ActionListener {
    private final RoundedPanel panelLogDetails;
    private String transactionID;
    private JTextField txtQuantity, txtSearch, TxtNameValue, TxtDateValue, txtTransactionIDValue;
    private JComboBox<Item> comboItem;
    private JLabel  lblTotalValue;
    private JTable table;
    public DefaultTableModel model;
    public JButton btnadd, btnremove, btnclear, btnsave, btnClose;
    private boolean saved = true;
    private Map<String, InventoryItemRecord> inventoryMap; //an arraylist map that requires a string and has values from InventoryItemRecord
    private TableRowSorter<DefaultTableModel> sorter; // Added for searching

    // this constructor takes parameters name and date
    public TransactionFrame(String name, String date) {
        this(name, date, null);
        setNameAndDate(name, date); //Gets the name and date to be used later
    }

    //initializes the panel
    public TransactionFrame(String logname, String date, String filepath) {
        setTitle("Transaction Panel");
        setSize(1000, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load inventory once at the start of the application
        // This ensures inventoryMap always reflects the file system's current state
        inventoryMap = InventoryItemRecord.loadInventory(); // <--- Moved this here and it's crucial

        JPanel contentPane = new JPanel(null);
        setContentPane(contentPane);

        panelLogDetails = new RoundedPanel(25);
        panelLogDetails.setLayout(null);
        panelLogDetails.setBounds(35, 60, 365, 220);
        panelLogDetails.setBackground(Color.white);

        // Subheader (optional)
        JLabel subheaderSelectedItem = new JLabel("TRANSACTION DETAILS");
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
        txtTransactionIDValue = new JTextField(generatedID);
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

        panelLogDetails.add(panelinside);
        contentPane.add(panelLogDetails);

        RoundedPanel panelItemInput = new RoundedPanel(25);
        panelItemInput.setBounds(35, 305, 365, 160);
        panelItemInput.setBackground(new Color(201, 42, 42));
        panelItemInput.setLayout(null);

        JLabel panelitemtitle = new JLabel("ITEM LIST");
        panelitemtitle.setBounds(20, 15, 300, 25);
        panelitemtitle.setFont(new Font("Arial", Font.BOLD, 16));
        panelitemtitle.setForeground(Color.white);
        panelItemInput.add(panelitemtitle);
        // Inner grid panel for input fields
        JPanel panelItemGrid = new JPanel(new GridBagLayout());
        panelItemGrid.setBounds(15, 25, 335, 120); // Position within panelItemInput
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
        loadItemsIntoComboBox(); // Initial population of combo box based on the loaded inventoryMap
        comboItem.setPreferredSize(new Dimension(140, 30));

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.anchor = GridBagConstraints.EAST;
        panelItemGrid.add(lblItem, gbc2);
        gbc2.gridx = 1; gbc2.anchor = GridBagConstraints.WEST;
        panelItemGrid.add(comboItem, gbc2);

        JLabel lblQuantity = new JLabel("QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setForeground(Color.WHITE);

        txtQuantity = new JTextField();
        txtQuantity.addKeyListener(new NoSpaceKeyListener());
        txtQuantity.setPreferredSize(new Dimension(140, 30));
        Inputvalidator.makeNumericOnly(txtQuantity);

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.anchor = GridBagConstraints.EAST;
        panelItemGrid.add(lblQuantity, gbc2);
        gbc2.gridx = 1; gbc2.anchor = GridBagConstraints.WEST;
        panelItemGrid.add(txtQuantity, gbc2);

        panelItemInput.add(panelItemGrid);
        contentPane.add(panelItemInput);

        JPanel panelBtn = new JPanel(new GridLayout(1, 5, 12, 0));
        panelBtn.setBounds(230, 490, 500, 30);

        btnadd = new JButton("ADD");
        btnremove = new JButton("REMOVE");
        btnclear = new JButton("CLEAR");
        btnsave = new JButton("SAVE");
        btnClose = new JButton("CLOSE"); // Moved to panelBtn

        Dimension smallBtnSize = new Dimension(50, 25);
        btnadd.setPreferredSize(smallBtnSize);
        btnremove.setPreferredSize(smallBtnSize);
        btnclear.setPreferredSize(smallBtnSize);
        btnsave.setPreferredSize(smallBtnSize);
        btnClose.setPreferredSize(smallBtnSize);

        Font btnFont = new Font("Arial", Font.PLAIN, 13);
        btnadd.setFont(btnFont);
        btnremove.setFont(btnFont);
        btnclear.setFont(btnFont);
        btnsave.setFont(btnFont);
        btnClose.setFont(btnFont);

        btnadd.addActionListener(this);
        btnremove.addActionListener(this);
        btnclear.addActionListener(this);
        btnsave.addActionListener(this);
        btnClose.addActionListener(this);

        btnClose.setFocusPainted(false);
        btnClose.setBackground(new Color(201, 42, 42));
        btnClose.setForeground(Color.WHITE);

        btnadd.setFocusPainted(false);
        btnadd.setBackground(new Color(201, 42, 42));
        btnadd.setForeground(Color.WHITE);

        btnremove.setFocusPainted(false);
        btnremove.setBackground(new Color(201, 42, 42));
        btnremove.setForeground(Color.WHITE);

        btnsave.setFocusPainted(false);
        btnsave.setBackground(new Color(201, 42, 42));
        btnsave.setForeground(Color.WHITE);

        btnclear.setFocusPainted(false);
        btnclear.setBackground(new Color(201, 42, 42));
        btnclear.setForeground(Color.WHITE);

        panelBtn.add(btnadd);
        panelBtn.add(btnremove);
        panelBtn.add(btnclear);
        panelBtn.add(btnsave);
        panelBtn.add(btnClose);

        contentPane.add(panelBtn);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSearch.setBounds(790, 27, 180, 30);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(170, 30));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Search");

        //It allows you have an interaction with the search textfield
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Only clear if the textfield is being clicked and follows this conditions if it does the sets the empty text
                if (txtSearch.getForeground().equals(Color.GRAY) && txtSearch.getText().trim().equals("Search")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK); //it turns the text to black
                }
            }
            //If the focus from the textfield dissapears it puts back the foreground and it puts back the text inside
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Search");
                }
            }
        });

        //This will enable for the item to be searched when it has already been scaned for customer purchase
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            private boolean isPlaceholder() {
                return txtSearch.getForeground().equals(Color.GRAY) && txtSearch.getText().trim().equals("Search");
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isPlaceholder()) searchTable(txtSearch.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isPlaceholder()) searchTable(txtSearch.getText());
                else searchTable("");
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        panelSearch.add(txtSearch);
        contentPane.add(panelSearch);

        // Creates a tablemodel which based on the string array as the bases of the column to put the details in
        String[] columnNames = {"ITEM", "PRICE", "QUANTITY", "SUBTOTAL"};
        model = new DefaultTableModel(columnNames, 0);  //Starts the row at zero meaning empty
        table = new JTable(model); // Creates a table based on the model
        sorter = new TableRowSorter<>(model); //Allows us to sort the rows of the table based on the columns
        table.setRowSorter(sorter); //Enables to us to sort based on the sorter created

        //Allows us to scroll on the table created when rows overlap the height of the panel
        JScrollPane scrollPane = new JScrollPane(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); //Allows the text to be centered
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // the horizontal alignment is centered
        for (int i = 0; i < table.getColumnCount(); i++) { //creates the loop that checks all columns, it only ends when there is no more column left
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer); //for every column it takes every detail from that column makes the text centered
        }

        //Creates a panelTable where the scrollpane will be added to easily set the table's coordinates and size
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(430, 65, 530, 350);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panelTable);

        //Creates a panel to display the total of the transcation
        RoundedPanel panelTotal = new RoundedPanel(15);
        panelTotal.setBounds(735, 430, 225, 35);
        panelTotal.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelTotal.setBackground(Color.WHITE);
        JLabel lblTotalText = new JLabel("TOTAL: ");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotalValue = new JLabel("₱0.00"); //creates a unicode of pesos with the 0.00 as initial total
        lblTotalValue.setFont(new Font("Arial", Font.BOLD, 20));
        panelTotal.add(lblTotalText);
        panelTotal.add(lblTotalValue);
        contentPane.add(panelTotal);

        //It checks if the filepath is not null and is not empty meaning it will load this file if the conditions are met
        if (filepath != null && !filepath.isEmpty()) {
            loadFromFile(filepath);
        }
    }

    // New method to load and filter inventory items into the JComboBox
    // This method now *only* populates the combo box based on the *current* inventoryMap
    private void loadItemsIntoComboBox() {
        comboItem.removeAllItems(); // Clear existing items
        for (InventoryItemRecord inv : inventoryMap.values()) { // Iterate over the already loaded inventoryMap
            if (inv.getQuantity() > 0) { // Only add if quantity is greater than 0
                comboItem.addItem(new Item(inv.getName(), inv.getPrice(), inv.getQuantity()));
            }
        }
        if (comboItem.getItemCount() > 0) {
            comboItem.setSelectedIndex(0);
        }
    }

    // The name and date that was made from createlog will be placed inside the TxtNameValue and TxtDateValue
    public void setNameAndDate(String name, String date) {
        TxtNameValue.setText(name);
        TxtDateValue.setText(date);
    }

    //Updates the total cost of the transaction
    private void updateTotal() {
        double total = 0.0; //intializes as 0
        for (int i = 0; i < model.getRowCount(); i++) {// For loop to check all of the rows
            Object value = model.getValueAt(i, 3);// Gets the value at column 4(Subtotal) which has an index of 3
            try { //uses try catch to get the total of all rows and uses NumberFormatException to detect the error
                total += Double.parseDouble(value.toString()); // Parses the value to a double and adds it to the total
            } catch (NumberFormatException ignored) {}
        }
        lblTotalValue.setText("₱" + String.format("%.2f", total)); //Updates the Total value using string format with the total value with two decimals
    }

    //Used to filter or search and uses query as the parameter
    private void searchTable(String query) {
        if (query.trim().isEmpty() || query.trim().equals("Search")) {
            sorter.setRowFilter(null); // it will show all rows in the table
        } else { // else it will try to sort rows
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0)); //sorts rows and they are case sensitive with the string query which filters the first column
            } catch (java.util.regex.PatternSyntaxException e) {
                sorter.setRowFilter(null);
            }
        }
    }

    //@Override used for button when implementing ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnadd) { //If btnAdd is clicked it adds the item and quantity unto the column
            Item selectedItemInCombo = (Item) comboItem.getSelectedItem(); // The Item object from the JComboBox
            String quantityStr = txtQuantity.getText().trim();

            if (selectedItemInCombo == null || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an item and enter a quantity.");
                return;
            }

            int quantityInt = 0;
            try {
                quantityInt = Integer.parseInt(quantityStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid number.");
                return;
            }

            if (quantityInt <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }

            // Get the authoritative InventoryItemRecord from the inventoryMap
            InventoryItemRecord invRecord = inventoryMap.get(selectedItemInCombo.getName());

            if (invRecord == null) {
                JOptionPane.showMessageDialog(this, "Item not found in inventory: " + selectedItemInCombo.getName() + "\nPlease check inventory.csv.");
                loadItemsIntoComboBox(); // Refresh combo box in case of discrepancy
                return;
            }

            if (quantityInt > invRecord.getQuantity()) { // Check against the actual inventory stock
                JOptionPane.showMessageDialog(this, "Not enough stock. Available: " + invRecord.getQuantity());
                return;
            }

            double subtotal = invRecord.getPrice() * quantityInt; // Calculate subtotal based on inventory price

            // Check if item already exists in the table to update quantity and subtotal
            boolean itemFoundInTable = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(selectedItemInCombo.getName())) {
                    int currentQuantityInTable = Integer.parseInt(model.getValueAt(i, 2).toString());
                    double currentSubtotalInTable = Double.parseDouble(model.getValueAt(i, 3).toString());

                    model.setValueAt(currentQuantityInTable + quantityInt, i, 2);
                    model.setValueAt(currentSubtotalInTable + subtotal, i, 3);
                    itemFoundInTable = true;
                    break;
                }
            }

            if (!itemFoundInTable) {
                model.addRow(new Object[]{selectedItemInCombo.getName(), invRecord.getPrice(), quantityInt, subtotal});
            }

            // Reduce quantity in the authoritative inventory record
            invRecord.reduceQuantity(quantityInt);

            // Save inventory changes immediately after reducing quantity
            try {
                InventoryItemRecord.saveInventory(inventoryMap);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving inventory after adding item: " + ex.getMessage());
            }

            // Re-load items into combo box to reflect updated quantity or removal if quantity is 0
            loadItemsIntoComboBox();

            clearInputs();
            saved = false;
            updateTotal();

        } else if (e.getSource() == btnremove) {
            int selectedRowView = table.getSelectedRow();
            if (selectedRowView == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            // Convert view row index to model row index, important if table is filtered/sorted
            int selectedModelRow = table.convertRowIndexToModel(selectedRowView);

            // Get item name and quantity from selected row (from the model)
            String itemName = model.getValueAt(selectedModelRow, 0).toString();
            int quantityToRemove = Integer.parseInt(model.getValueAt(selectedModelRow, 2).toString());

            // Restore quantity in the authoritative inventoryMap
            InventoryItemRecord invRecord = inventoryMap.get(itemName);
            if (invRecord != null) {
                invRecord.addQuantity(quantityToRemove);
            } else {
                // This shouldn't happen if inventory is consistent, but handle defensively
                System.err.println("Error: Item " + itemName + " not found in inventory map when trying to restore quantity.");
            }

            // Save inventory changes immediately after restoring quantity
            try {
                InventoryItemRecord.saveInventory(inventoryMap);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving inventory after removing item: " + ex.getMessage());
            }

            // Re-load items into combo box to reflect updated quantity or addition if quantity becomes > 0
            loadItemsIntoComboBox();

            // Remove row from table model
            model.removeRow(selectedModelRow);
            clearInputs();
            saved = false;
            updateTotal();

        } else if (e.getSource() == btnclear) {
            clearInputs();
        } else if (e.getSource() == btnsave) {
            // Transaction save only, inventory is saved incrementally
            try {
                saveToFile();
                saved = true;
                JOptionPane.showMessageDialog(this, "Transaction log saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving transaction log: " + ex.getMessage());
            }
        } else if (e.getSource() == btnClose) {
            if (this.saved) {
                new Menu();
                dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to save transaction changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        saveToFile();
                        // No need to save inventory here again as it's saved on add/remove
                        new Menu();
                        dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving transaction log on close: " + ex.getMessage());
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    new Menu();
                    dispose();
                } // If CANCEL_OPTION, do nothing and stay on the current frame
            }
        }
    }

    //a method to remove everything placed in the textfields
    private void clearInputs() {
        txtQuantity.setText("");
        if (comboItem.getItemCount() > 0) {
            comboItem.setSelectedIndex(0);
        }
    }

    //Methods that saves the transaction log file (not inventory)
    public void saveToFile() throws IOException {
        File dir = new File("logs"); //creates a file directory named logs
        if (!dir.exists()) dir.mkdir(); //if the directory does not exist it creates that directory
        String filename = "logs/" + TxtNameValue.getText() + ".csv"; //gets the name of the transaction as the name of the csv file
        File file = new File(filename); //creates the file under that filename
        TransactionFileManager.saveToFile(file, TxtNameValue.getText(), TxtDateValue.getText(),txtTransactionIDValue.getText(), model); //Saves file using
        // Inventory is now saved incrementally on add/remove. No need to save it here.
    }

    //loads file
    public void loadFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "Transaction log file not found: " + filename);
                return;
            }
            TransactionData data = TransactionFileManager.loadFromFile(file);
            setNameAndDate(data.name, data.date);
            transactionID = data.transactionID;
            txtTransactionIDValue.setText(transactionID);
            model.setRowCount(0); // Clear existing rows

            // When loading a transaction, the inventoryMap is considered the live, current stock.
            // This method simply populates the table with historical transaction data.
            for (String[] row : data.rows) {
                if (row.length == model.getColumnCount()) {
                    model.addRow(row);
                }
            }
            updateTotal();
            // No need to loadItemsIntoComboBox here as the inventoryMap is the true source of truth
            // and is loaded at application start, and updated on add/remove.
            // The combo box will already reflect the current inventory.
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading transaction log file: " + e.getMessage());
        }
    }

    //a method in generating the Transaction ID using letters and numbers
    private String generateRandomTransactionID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //a string full of numbers and characters
        Random random = new Random(); //creates an object
        StringBuilder sb = new StringBuilder(6); //Creates a string out of the given numbers and letters and number with 6 max
        for (int i = 0; i < 6; i++) {  //loops until it fulfills for 6 values
            sb.append(chars.charAt(random.nextInt(chars.length()))); //picks from the string chars, adds it to the end of string builder
        }
        return sb.toString(); //returns the string builder to the string itself
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ensure inventory.csv exists or is created by your InventoryItemRecord.loadInventory()
            // before running TransactionFrame to avoid NullPointerException on inventoryMap.
            TransactionFrame frame = new TransactionFrame("SampleLog", "2025-07-04", (args.length > 0 ? args[0] : null));
            frame.setVisible(true);
        });
    }
}