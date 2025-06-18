import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Vector;

public class SupplyPurchasePanel implements ActionListener {

    JFrame myFrame;
    // Supplier Info Panel components
    RoundedPanel panelSupplierInfo;
    JLabel lblSupplyID, lblSupplierName, lblSupplierCode;
    JTextField txtSupplyID, txtSupplierName, txtSupplierCode;

    // Selected Item Info Panel components
    RoundedPanel panelSelectedItemInfo;
    JLabel lblSelectedItem, lblQuantity, lblCost, lblProfitIncrease, lblDateSupplied;
    JTextField txtSelectedItem, txtQuantity, txtCost;
    JComboBox<String> cboProfitIncrease;
    JTextField txtDateSupplied; // For displaying real-time date

    // Buttons
    JButton btnAddItemToPurchase, btnRemovePurchaseItem, btnProcessPurchase, btnClose;

    // Tables
    JTable tblInventoryItems; // Displays items from SupermarketInventory.txt
    DefaultTableModel inventoryModel;

    JTable tblPurchaseDetails; // Displays items added for purchase
    DefaultTableModel purchaseModel;

    // Database access for inventory items
    Database inventoryDb = new Database("Items.txt");
    // Database for supply/purchase records (new file)
    Database purchaseDb = new Database("PurchaseRecords.txt");

    // NEW: Reference to InventorySystem1 to refresh its table
    private InventorySystem1 inventorySystem1Ref;


    // MODIFIED CONSTRUCTOR: Now accepts an InventorySystem1 object
    public SupplyPurchasePanel(InventorySystem1 inventorySystem1Ref) {
        this.inventorySystem1Ref = inventorySystem1Ref; // Store the reference

        myFrame = new JFrame("Supply and Purchase Management");
        myFrame.setLayout(null);
        myFrame.setSize(1000, 620);
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE for better application flow
        myFrame.setResizable(false);

        // Header Label
        JLabel lblHeader = new JLabel("Supply & Purchase Management");
        lblHeader.setBounds(40, 15, 500, 30);
        lblHeader.setForeground(new Color(200, 0, 0)); // Red color
        lblHeader.setFont(new Font("DM Sans", Font.BOLD, 20));
        myFrame.add(lblHeader);

        // --- Supplier Information Panel (panelInfo) ---
        panelSupplierInfo = new RoundedPanel(25);
        panelSupplierInfo.setLayout(null);
        panelSupplierInfo.setBounds(40, 60, 380, 180);
        panelSupplierInfo.setBackground(Color.WHITE);

        JLabel subheaderSupplier = new JLabel("Supplier Information");
        subheaderSupplier.setBounds(20, 15, 300, 25);
        subheaderSupplier.setFont(new Font("Arial", Font.BOLD, 16));
        subheaderSupplier.setForeground(new Color(10, 10, 10, 255));
        panelSupplierInfo.add(subheaderSupplier);

        lblSupplyID = new JLabel("Supply ID:");
        txtSupplyID = new JTextField();
        txtSupplyID.setEditable(false); // Auto-generate

        lblSupplierName = new JLabel("Supplier Name:");
        txtSupplierName = new JTextField();

        lblSupplierCode = new JLabel("Supplier Code:");
        txtSupplierCode = new JTextField();

        lblSupplyID.setBounds(20, 50, 120, 25);
        txtSupplyID.setBounds(150, 50, 200, 25);
        lblSupplierName.setBounds(20, 85, 120, 25);
        txtSupplierName.setBounds(150, 85, 200, 25);
        lblSupplierCode.setBounds(20, 120, 120, 25);
        txtSupplierCode.setBounds(150, 120, 200, 25);

        panelSupplierInfo.add(lblSupplyID);
        panelSupplierInfo.add(txtSupplyID);
        panelSupplierInfo.add(lblSupplierName);
        panelSupplierInfo.add(txtSupplierName);
        panelSupplierInfo.add(lblSupplierCode);
        panelSupplierInfo.add(txtSupplierCode);
        myFrame.add(panelSupplierInfo);

        // --- Selected Item Information Panel (panelInfo2) ---
        panelSelectedItemInfo = new RoundedPanel(25);
        panelSelectedItemInfo.setLayout(null);
        panelSelectedItemInfo.setBounds(40, 260, 380, 250); // Positioned below supplier info
        panelSelectedItemInfo.setBackground(Color.WHITE);

        JLabel subheaderSelectedItem = new JLabel("Selected Item Details");
        subheaderSelectedItem.setBounds(20, 15, 300, 25);
        subheaderSelectedItem.setFont(new Font("Arial", Font.BOLD, 16));
        subheaderSelectedItem.setForeground(new Color(10, 10, 10, 255));
        panelSelectedItemInfo.add(subheaderSelectedItem);

        JPanel panelselecteddetails = new JPanel(new GridBagLayout());
        panelselecteddetails.setBounds(20,50,340,180);
        panelselecteddetails.setOpaque(false);
        panelSelectedItemInfo.add(panelselecteddetails);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        lblSelectedItem = new JLabel("Selected Item:");
        txtSelectedItem = new JTextField();
        txtSelectedItem.setEditable(false); // Display only

        gbc.gridx = 0; gbc.gridy = 0;
        panelselecteddetails.add(lblSelectedItem, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(txtSelectedItem, gbc);

        lblQuantity = new JLabel("Quantity:");
        txtQuantity = new JTextField();

        gbc.gridx = 0; gbc.gridy = 1;
        panelselecteddetails.add(lblQuantity, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panelselecteddetails.add(txtQuantity, gbc);

        lblCost = new JLabel("Cost (per item):");
        txtCost = new JTextField();

        gbc.gridx = 0; gbc.gridy = 2;
        panelselecteddetails.add(lblCost, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panelselecteddetails.add(txtCost, gbc);

        lblProfitIncrease = new JLabel("Profit Increase:");
        String[] profitOptions = {"10%", "20%", "30%", "40%", "50%"};
        cboProfitIncrease = new JComboBox<>(profitOptions);

        gbc.gridx = 0; gbc.gridy = 3;
        panelselecteddetails.add(lblProfitIncrease, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panelselecteddetails.add(cboProfitIncrease, gbc);

        lblDateSupplied = new JLabel("Date Supplied:");
        txtDateSupplied = new JTextField();
        txtDateSupplied.setEditable(false); // Displays real-time date

        gbc.gridx = 0; gbc.gridy = 4;
        panelselecteddetails.add(lblDateSupplied, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panelselecteddetails.add(txtDateSupplied, gbc);

        setRealTimeDate(); // Set initial real-time date

        myFrame.add(panelSelectedItemInfo);

        // --- Buttons for Purchase Actions ---
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,5,0));
        buttonPanel.setBounds(40, 515, 400, 30);
        buttonPanel.setBackground(new Color(246, 243, 243));

        btnAddItemToPurchase = new JButton("Add to Purchase");
        btnRemovePurchaseItem = new JButton("Remove");
        btnProcessPurchase = new JButton("Process");

        // Styling buttons
        JButton[] actionButtons = {btnAddItemToPurchase, btnRemovePurchaseItem, btnProcessPurchase};
        for (JButton b : actionButtons) {
            b.setBackground(new Color(200, 0, 0)); // Red button
            b.setForeground(Color.white);
            b.addActionListener(this);
            buttonPanel.add(b);
        }
        myFrame.add(buttonPanel);

        JLabel lblInventoryTable = new JLabel("Available Inventory Items");
        lblInventoryTable.setBounds(440, 17, 300, 25);
        lblInventoryTable.setFont(new Font("Arial", Font.BOLD, 20));
        lblInventoryTable.setForeground(new Color(10, 10, 10, 255));
        myFrame.add(lblInventoryTable);

        Vector<String> inventoryField = new Vector<>();
        inventoryField.add("ID");
        inventoryField.add("Name");
        inventoryField.add("Category");
        inventoryField.add("Date Added");

        inventoryModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryModel.setColumnIdentifiers(inventoryField);

        tblInventoryItems = new JTable(inventoryModel);
        tblInventoryItems.setBackground(Color.WHITE);
        tblInventoryItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInventoryItems.setShowGrid(true);
        tblInventoryItems.setGridColor(Color.LIGHT_GRAY);
        tblInventoryItems.setFillsViewportHeight(true);
        tblInventoryItems.getTableHeader().setBackground(new Color(200, 0, 0)); // Red header
        tblInventoryItems.getTableHeader().setForeground(Color.WHITE);
        tblInventoryItems.setRowHeight(25); // Make rows a bit taller

        JScrollPane scrollPaneInventory = new JScrollPane(tblInventoryItems);
        scrollPaneInventory.setBounds(440, 60, 520, 185); // Increased height for better visibility
        scrollPaneInventory.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        scrollPaneInventory.getViewport().setBackground(Color.WHITE);
        myFrame.add(scrollPaneInventory);

        inventoryDb.displayRecord(inventoryModel); // Load inventory items

        // Listener for tblInventoryItems to populate selected item panel
        tblInventoryItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblInventoryItems.getSelectedRow();
                if (row >= 0) {
                    txtSelectedItem.setText(inventoryModel.getValueAt(row, 1).toString()); // Item Name
                    // Clear quantity and cost for new selection
                    txtQuantity.setText("");
                    txtCost.setText("");
                }
            }
        });

        JLabel lblPurchaseTable = new JLabel("Current Purchase List");
        lblPurchaseTable.setBounds(440, 170, 500, 200);
        lblPurchaseTable.setFont(new Font("Arial", Font.BOLD, 16));
        lblPurchaseTable.setForeground(new Color(10, 10, 10, 255));
        myFrame.add(lblPurchaseTable);

        Vector<String> purchaseField = new Vector<>();
        purchaseField.add("Supply ID");
        purchaseField.add("Supplier Name");
        purchaseField.add("Supplier Code");
        purchaseField.add("Item Name");
        purchaseField.add("Quantity");
        purchaseField.add("Cost");
        purchaseField.add("Profit %");
        purchaseField.add("Selling Price"); // New calculated column
        purchaseField.add("Date Supplied");

        purchaseModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        purchaseModel.setColumnIdentifiers(purchaseField);

        tblPurchaseDetails = new JTable(purchaseModel);
        tblPurchaseDetails.setBackground(Color.WHITE);
        tblPurchaseDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPurchaseDetails.setShowGrid(true);
        tblPurchaseDetails.setGridColor(Color.LIGHT_GRAY);
        tblPurchaseDetails.setFillsViewportHeight(true);
        tblPurchaseDetails.getTableHeader().setBackground(new Color(200, 0, 0)); // Red header
        tblPurchaseDetails.getTableHeader().setForeground(Color.WHITE);
        tblPurchaseDetails.setRowHeight(25); // Make rows a bit taller

        JScrollPane scrollPanePurchase = new JScrollPane(tblPurchaseDetails);
        scrollPanePurchase.setBounds(440, 290, 520, 215);
        scrollPanePurchase.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        scrollPanePurchase.getViewport().setBackground(Color.WHITE);
        myFrame.add(scrollPanePurchase);

        purchaseDb.displayRecord(purchaseModel); // Load existing purchase records


        // Close button
        btnClose = new JButton("Close");
        btnClose.setBounds(860, 15, 100, 25);
        btnClose.setBackground(new Color(200, 0, 0));
        btnClose.setForeground(Color.white);
        btnClose.addActionListener(this);
        myFrame.add(btnClose);

        // Main background panel
        JPanel background = new JPanel();
        background.setBackground(new Color(246, 243, 243));
        background.setSize(1000, 750);
        myFrame.add(background);

        myFrame.setVisible(true);
        autoGenerateSupplyID(); // Generate initial Supply ID
    }

    private void setRealTimeDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        txtDateSupplied.setText(now.format(formatter));
    }

    private void autoGenerateSupplyID() {
        int nextId = 1;
        if (purchaseModel.getRowCount() > 0) {
            for (int i = 0; i < purchaseModel.getRowCount(); i++) {
                try {
                    // Assuming Supply ID is the first column in purchaseModel (index 0)
                    int currentId = Integer.parseInt(purchaseModel.getValueAt(i, 0).toString());
                    if (currentId >= nextId) {
                        nextId = currentId + 1;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Non-numeric Supply ID found in purchase records: " + purchaseModel.getValueAt(i, 0));
                }
            }
        }
        txtSupplyID.setText(String.valueOf(nextId));
    }

    /**
     * Clears input fields related to supplier information and regenerates a new Supply ID.
     */
    private void resetSupplierInputs() {
        txtSupplierName.setText("");
        txtSupplierCode.setText("");
        autoGenerateSupplyID();
    }

    /**
     * Clears input fields related to selected item details and clears the inventory table selection.
     */
    private void resetSelectedItemInputs() {
        txtSelectedItem.setText("");
        txtQuantity.setText("");
        txtCost.setText("");
        cboProfitIncrease.setSelectedIndex(0);
        tblInventoryItems.clearSelection(); // Clear inventory table selection
        setRealTimeDate(); // Update real-time date for the next item
    }

    /**
     * Checks if any essential supplier information fields are empty.
     *
     * @return true if any supplier info field is empty, false otherwise.
     */
    private boolean isEmptySupplierInfo() {
        return txtSupplierName.getText().trim().isEmpty() ||
                txtSupplierCode.getText().trim().isEmpty();
    }

    /**
     * Checks if any essential selected item information fields are empty.
     *
     * @return true if any selected item info field is empty, false otherwise.
     */
    private boolean isEmptySelectedItemInfo() {
        return txtSelectedItem.getText().trim().isEmpty() ||
                txtQuantity.getText().trim().isEmpty() ||
                txtCost.getText().trim().isEmpty();
    }

    /**
     * Saves a summary of the current restock purchase to a text file
     * within a "RestockSummaries" directory.
     */
    private void saveRestockSummary() {
        // Create the directory if it doesn't exist
        File directory = new File("RestockSummaries");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate filename based on current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String filename = "RestockSummaries/RestockSummary_" + now.format(formatter) + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("--- Restock Purchase Summary ---\n");
            bw.write("Supply ID: " + txtSupplyID.getText() + "\n");
            bw.write("Supplier Name: " + txtSupplierName.getText() + "\n");
            bw.write("Supplier Code: " + txtSupplierCode.getText() + "\n");
            bw.write("Date Processed: " + txtDateSupplied.getText() + "\n");
            bw.write("\nItems Restocked:\n");

            double totalPurchaseCost = 0.0;
            for (int i = 0; i < purchaseModel.getRowCount(); i++) {
                String itemName = purchaseModel.getValueAt(i, 3).toString(); // Item Name
                String quantity = purchaseModel.getValueAt(i, 4).toString(); // Quantity
                String cost = purchaseModel.getValueAt(i, 5).toString();     // Cost
                String sellingPrice = purchaseModel.getValueAt(i, 7).toString(); // Selling Price

                bw.write(String.format("  - Item: %s, Quantity: %s, Cost/Item: %s, Selling Price/Item: %s\n",
                        itemName, quantity, cost, sellingPrice));

                try {
                    totalPurchaseCost += Double.parseDouble(quantity) * Double.parseDouble(cost);
                } catch (NumberFormatException ex) {
                    // Handle cases where quantity or cost might not be valid numbers (though validated earlier)
                    System.err.println("Error parsing quantity or cost for summary calculation: " + ex.getMessage());
                }
            }
            bw.write("\nTotal Purchase Cost: " + String.format("%.2f", totalPurchaseCost) + "\n");
            bw.write("--------------------------------\n");
            JOptionPane.showMessageDialog(myFrame, "Restock summary saved to: " + filename, "Summary Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(myFrame, "Error saving restock summary: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAddItemToPurchase)) {
            // Validate supplier info first
            if (isEmptySupplierInfo()) {
                JOptionPane.showMessageDialog(myFrame, "Please fill in all Supplier Information fields (Supplier Name, Supplier Code).", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Validate selected item info
            if (isEmptySelectedItemInfo()) {
                JOptionPane.showMessageDialog(myFrame, "Please select an Item from the inventory and fill in Quantity and Cost.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(myFrame, "Quantity must be a positive whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double cost = Double.parseDouble(txtCost.getText().trim());
                if (cost <= 0) {
                    JOptionPane.showMessageDialog(myFrame, "Cost must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String profitPercentageStr = cboProfitIncrease.getSelectedItem().toString().replace("%", "");
                double profitPercentage = Double.parseDouble(profitPercentageStr) / 100.0;

                double sellingPrice = cost * (1 + profitPercentage);

                Vector<String> purchaseData = new Vector<>();
                purchaseData.add(txtSupplyID.getText());
                purchaseData.add(txtSupplierName.getText());
                purchaseData.add(txtSupplierCode.getText());
                purchaseData.add(txtSelectedItem.getText());
                purchaseData.add(String.valueOf(quantity));
                purchaseData.add(String.format("%.2f", cost));
                purchaseData.add(cboProfitIncrease.getSelectedItem().toString());
                purchaseData.add(String.format("%.2f", sellingPrice)); // Add selling price
                purchaseData.add(txtDateSupplied.getText()); // Use the real-time date

                purchaseModel.addRow(purchaseData);
                resetSelectedItemInputs(); // Clear item-specific inputs after adding
                JOptionPane.showMessageDialog(myFrame, "Item added to the purchase list.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(myFrame, "Quantity and Cost must be valid numbers. Please check your input.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource().equals(btnRemovePurchaseItem)) {
            int selectedRow = tblPurchaseDetails.getSelectedRow();
            if (selectedRow >= 0) {
                purchaseModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(myFrame, "Selected item removed from the purchase list.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(myFrame, "Please select an item from the purchase list to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource().equals(btnProcessPurchase)) {
            if (purchaseModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(myFrame, "No items in the purchase list to process.", "No Items", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirm with the user before processing a purchase
            int confirm = JOptionPane.showConfirmDialog(myFrame, "Are you sure you want to process this purchase?\n" +
                    "This will save all items in the current list.", "Confirm Purchase", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                purchaseDb.overwriteRecords(purchaseModel); // 1. Save all current purchase records (to PurchaseRecords.txt)

                // NEW: Save the restock summary
                saveRestockSummary(); // 2. Save a summary of this restock

                JOptionPane.showMessageDialog(myFrame, "Purchase processed successfully! All items saved.", "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);

                // NEW: Refresh the InventorySystem1 table after processing purchase
                if (inventorySystem1Ref != null) {
                    inventorySystem1Ref.loadInventoryData(); // 3. Tell InventorySystem1 to reload its inventory (from Items.txt)
                }

                // Reset the purchase table and inputs for a new purchase
                purchaseModel.setRowCount(0); // 4. THIS IS THE LINE THAT CLEARS YOUR 'CURRENT PURCHASE LIST' TABLE
                resetSupplierInputs();       // 5. Clear supplier info fields
                resetSelectedItemInputs();   // 6. Clear selected item fields
                autoGenerateSupplyID();      // 7. Generate a new Supply ID for the next purchase
            }
            // REMOVE THIS EXTRA CLOSING CURLY BRACE '}' HERE
        } else if (e.getSource().equals(btnClose)) {
            if (purchaseModel.getRowCount() > 0) {
                int confirm = JOptionPane.showConfirmDialog(myFrame, "You have unsaved items in the purchase list. Do you want to save them before closing?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    purchaseDb.overwriteRecords(purchaseModel);
                    // Decide if you want to save a summary on close as well.
                    // For this request, it's only on "Process" click, so not adding here.
                    JOptionPane.showMessageDialog(myFrame, "Purchase data saved. Closing application.", "Saved", JOptionPane.INFORMATION_MESSAGE);
                    // NEW: Refresh the InventorySystem1 table after saving and closing
                    if (inventorySystem1Ref != null) {
                        inventorySystem1Ref.loadInventoryData();
                    }
                    myFrame.dispose();
                } else if (confirm == JOptionPane.NO_OPTION) {
                    myFrame.dispose();
                }
            } else {
                myFrame.dispose();
            }
        }
    }
    private class Database {
        private String filename;

        public Database(String filename) {
            this.filename = filename;
        }

        public void displayRecord(DefaultTableModel model) {
            model.setRowCount(0); // Clear existing data
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    Vector<String> row = new Vector<>();
                    for (String s : data) {
                        row.add(s.trim());
                    }
                    model.addRow(row);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Database file '" + filename + "' not found. A new one will be created upon saving.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(myFrame, "Error loading data from " + filename + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void overwriteRecords(DefaultTableModel model) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        line.append(model.getValueAt(i, j).toString());
                        if (j < model.getColumnCount() - 1) {
                            line.append(",");
                        }
                    }
                    bw.write(line.toString());
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(myFrame, "Error saving data to " + filename + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // When running SupplyPurchasePanel directly for testing,
        // you won't have an InventorySystem1 instance.
        // For a full application, you would create InventorySystem1 first
        // and then pass its instance to SupplyPurchasePanel.
        SwingUtilities.invokeLater(() -> new SupplyPurchasePanel(null)); // Pass null for now if testing standalone
    }
}