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
import java.util.Vector;
import java.text.DecimalFormat; // Import DecimalFormat

public class SupplyPurchase extends JFrame implements ActionListener {

    private RoundedPanel panelSupplierInfo;
    private JLabel lblSupplyID, lblSupplierName, lblSupplierCode;
    private JTextField txtSupplyID, txtSupplierName, txtSupplierCode;
    private RoundedPanel panelSelectedItemInfo;
    private JLabel lblSelectedItem, lblQuantity, lblCost, lblProfitIncrease, lblDateSupplied;
    private JTextField txtSelectedItem, txtQuantity, txtCost;
    private JComboBox<String> cboProfitIncrease;
    private JTextField txtDateSupplied;
    private JButton btnAddItemToPurchase, btnRemovePurchaseItem, btnProcessPurchase, btnClose;
    private JTable tblInventoryItems;
    private DefaultTableModel inventoryModel;
    private JTable tblPurchaseDetails;
    private DefaultTableModel purchaseModel;
    private Menu menuRef;
    private Database inventoryDb = new Database("Items.txt");
    private Database purchaseDb = new Database("PurchaseRecords.txt");
    private InventoryManager inventoryManager = new InventoryManager("Inventory.txt");
    private InventorySystem1 inventorySystem1Ref;

    //=====================================|Supply Purchase with parameters|===========================================
    public SupplyPurchase(InventorySystem1 inventorySystem1Ref, Menu menuRef) {
        this.inventorySystem1Ref = inventorySystem1Ref;
        this.menuRef = menuRef;

        //Dimensions and the intial output
        setTitle("Supply and Purchase Management");
        setLayout(null);
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Header label
        JLabel lblHeader = new JLabel("Supply & Purchase Management");
        lblHeader.setBounds(40, 15, 500, 30);
        lblHeader.setForeground(new Color(200, 0, 0));
        lblHeader.setFont(new Font("DM Sans", Font.BOLD, 20));
        add(lblHeader);

        // Supplier Information Panel
        panelSupplierInfo = new RoundedPanel(25);
        panelSupplierInfo.setLayout(null);
        panelSupplierInfo.setBounds(40, 60, 380, 180);
        panelSupplierInfo.setBackground(Color.WHITE);

        JLabel subheaderSupplier = new JLabel("Supplier Information");
        subheaderSupplier.setBounds(20, 15, 300, 25);
        subheaderSupplier.setFont(new Font("Arial", Font.BOLD, 16));
        panelSupplierInfo.add(subheaderSupplier);

        lblSupplyID = new JLabel("Supply ID:");
        txtSupplyID = new JTextField();
        txtSupplyID.setEditable(false);

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
        add(panelSupplierInfo);

        // Selected Item Panel
        panelSelectedItemInfo = new RoundedPanel(25);
        panelSelectedItemInfo.setLayout(null);
        panelSelectedItemInfo.setBounds(40, 260, 380, 250);
        panelSelectedItemInfo.setBackground(Color.WHITE);

        JLabel subheaderSelectedItem = new JLabel("Selected Item Details");
        subheaderSelectedItem.setBounds(20, 15, 300, 25);
        subheaderSelectedItem.setFont(new Font("Arial", Font.BOLD, 16));
        panelSelectedItemInfo.add(subheaderSelectedItem);

        JPanel panelselecteddetails = new JPanel(new GridBagLayout());
        panelselecteddetails.setBounds(20, 50, 340, 180);
        panelselecteddetails.setOpaque(false);
        panelSelectedItemInfo.add(panelselecteddetails);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        lblSelectedItem = new JLabel("Selected Item:");
        txtSelectedItem = new JTextField();
        txtSelectedItem.setEditable(false);

        lblQuantity = new JLabel("Quantity:");
        txtQuantity = new JTextField();

        lblCost = new JLabel("Cost (per item):");
        txtCost = new JTextField();

        lblProfitIncrease = new JLabel("Profit Increase:");
        String[] profitOptions = {"10%", "20%", "30%", "40%", "50%"};
        cboProfitIncrease = new JComboBox<>(profitOptions);

        lblDateSupplied = new JLabel("Date Supplied:");
        txtDateSupplied = new JTextField();
        txtDateSupplied.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0;
        panelselecteddetails.add(lblSelectedItem, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(txtSelectedItem, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelselecteddetails.add(lblQuantity, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelselecteddetails.add(lblCost, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(txtCost, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelselecteddetails.add(lblProfitIncrease, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(cboProfitIncrease, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelselecteddetails.add(lblDateSupplied, gbc);
        gbc.gridx = 1;
        panelselecteddetails.add(txtDateSupplied, gbc);

        setRealTimeDate();
        add(panelSelectedItemInfo);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.setBounds(40, 515, 400, 30);
        buttonPanel.setBackground(new Color(246, 243, 243));

        btnAddItemToPurchase = new JButton("Add to Purchase");
        btnRemovePurchaseItem = new JButton("Remove");
        btnProcessPurchase = new JButton("Process");

        JButton[] actionButtons = {btnAddItemToPurchase, btnRemovePurchaseItem, btnProcessPurchase};
        for (JButton b : actionButtons) {
            b.setBackground(new Color(200, 0, 0));
            b.setForeground(Color.white);
            b.addActionListener(this);
            buttonPanel.add(b);
        }
        add(buttonPanel);

        // Inventory Items Table
        JLabel lblInventoryTable = new JLabel("Available Inventory Items");
        lblInventoryTable.setBounds(440, 17, 300, 25);
        lblInventoryTable.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblInventoryTable);

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
        tblInventoryItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblInventoryItems.setRowHeight(25);
        tblInventoryItems.getTableHeader().setBackground(new Color(200, 0, 0));
        tblInventoryItems.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPaneInventory = new JScrollPane(tblInventoryItems);
        scrollPaneInventory.setBounds(440, 60, 520, 185);
        add(scrollPaneInventory);

        inventoryDb.displayRecord(inventoryModel);

        tblInventoryItems.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblInventoryItems.getSelectedRow();
                if (row >= 0) {
                    txtSelectedItem.setText(inventoryModel.getValueAt(row, 1).toString());
                    txtQuantity.setText("");
                    txtCost.setText("");
                }
            }
        });

        // Purchase Details Table
        JLabel lblPurchaseTable = new JLabel("Current Purchase List");
        lblPurchaseTable.setBounds(440, 250, 500, 25);
        lblPurchaseTable.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblPurchaseTable);

        Vector<String> purchaseField = new Vector<>();
        purchaseField.add("Supply ID");
        purchaseField.add("Supplier Name");
        purchaseField.add("Supplier Code");
        purchaseField.add("Item Name");
        purchaseField.add("Quantity");
        purchaseField.add("Cost");
        purchaseField.add("Profit %");
        purchaseField.add("Selling Price");
        purchaseField.add("Date Supplied");

        purchaseModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        purchaseModel.setColumnIdentifiers(purchaseField);

        tblPurchaseDetails = new JTable(purchaseModel);
        tblPurchaseDetails.setRowHeight(25);
        tblPurchaseDetails.getTableHeader().setBackground(new Color(200, 0, 0));
        tblPurchaseDetails.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPanePurchase = new JScrollPane(tblPurchaseDetails);
        scrollPanePurchase.setBounds(440, 280, 520, 215);
        add(scrollPanePurchase);

        // Close Button
        btnClose = new JButton("Close");
        btnClose.setBounds(860, 15, 100, 25);
        btnClose.setBackground(new Color(200, 0, 0));
        btnClose.setForeground(Color.white);
        btnClose.addActionListener(this);
        add(btnClose);

        // Background
        JPanel background = new JPanel();
        background.setBackground(new Color(246, 243, 243));
        background.setSize(1000, 750);
        add(background);

        setVisible(true);
        autoGenerateSupplyID();
    }

    private void setRealTimeDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        txtDateSupplied.setText(now.format(formatter));
    }

    private void autoGenerateSupplyID() {
        int nextId = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(purchaseDb.filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    try {
                        int currentId = Integer.parseInt(data[0].trim());
                        if (currentId >= nextId) {
                            nextId = currentId + 1;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Non-numeric Supply ID found in PurchaseRecords.txt: " + data[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("PurchaseRecords.txt not found. Starting Supply ID from 1.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading PurchaseRecords.txt for Supply ID generation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        txtSupplyID.setText(String.valueOf(nextId));
    }

    private void resetSupplierInputs() {
        txtSupplierName.setText("");
        txtSupplierCode.setText("");
    }

    private void resetSelectedItemInputs() {
        txtSelectedItem.setText("");
        txtQuantity.setText("");
        txtCost.setText("");
        cboProfitIncrease.setSelectedIndex(0);
        tblInventoryItems.clearSelection();
        setRealTimeDate();
    }

    private boolean isEmptySupplierInfo() {
        return txtSupplierName.getText().trim().isEmpty() ||
                txtSupplierCode.getText().trim().isEmpty();
    }

    private boolean isEmptySelectedItemInfo() {
        return txtSelectedItem.getText().trim().isEmpty() ||
                txtQuantity.getText().trim().isEmpty() ||
                txtCost.getText().trim().isEmpty();
    }

    private void saveRestockSummary() {
        File directory = new File("RestockSummaries");
        if (!directory.exists()) {
            directory.mkdirs();
        }

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
                String itemName = purchaseModel.getValueAt(i, 3).toString();
                String quantity = purchaseModel.getValueAt(i, 4).toString();
                String cost = purchaseModel.getValueAt(i, 5).toString();
                String sellingPrice = purchaseModel.getValueAt(i, 7).toString();

                bw.write(String.format(" - Item: %s, Quantity: %s, Cost/Item: %s, Selling Price/Item: %s\n",
                        itemName, quantity, cost, sellingPrice));

                try {
                    totalPurchaseCost += Double.parseDouble(quantity) * Double.parseDouble(cost);
                } catch (NumberFormatException ex) {
                    System.err.println("Error parsing quantity or cost for summary calculation: " + ex.getMessage());
                }
            }
            bw.write("\nTotal Purchase Cost: " + String.format("%.2f", totalPurchaseCost) + "\n");
            bw.write("--------------------------------\n");
            JOptionPane.showMessageDialog(this, "Restock summary saved to: " + filename, "Summary Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving restock summary: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInventoryQuantities() {
        for (int i = 0; i < purchaseModel.getRowCount(); i++) {
            String itemName = purchaseModel.getValueAt(i, 3).toString();
            int quantity = Integer.parseInt(purchaseModel.getValueAt(i, 4).toString());
            double sellingPrice = Double.parseDouble(purchaseModel.getValueAt(i, 7).toString());

            inventoryManager.updateItem(itemName, quantity, sellingPrice);
        }

        // Refresh the inventory system display if reference exists
        if (inventorySystem1Ref != null) {
            inventorySystem1Ref.refreshInventory();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAddItemToPurchase)) {
            if (isEmptySupplierInfo()) {
                JOptionPane.showMessageDialog(this, "Please fill in all Supplier Information fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (isEmptySelectedItemInfo()) {
                JOptionPane.showMessageDialog(this, "Please select an Item and fill in Quantity and Cost.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(txtQuantity.getText().trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be a positive whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double cost = Double.parseDouble(txtCost.getText().trim());
                if (cost <= 0) {
                    JOptionPane.showMessageDialog(this, "Cost must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String profitPercentageStr = cboProfitIncrease.getSelectedItem().toString().replace("%", "");
                double profitPercentage = Double.parseDouble(profitPercentageStr) / 100.0;

                double rawSellingPrice = cost * (1 + profitPercentage);
                // Round to the nearest whole number first
                double roundedSellingPrice = Math.round(rawSellingPrice);

                // Format to two decimal places for display
                DecimalFormat df = new DecimalFormat("0.00");
                String formattedSellingPrice = df.format(roundedSellingPrice);


                Vector<String> purchaseData = new Vector<>();
                purchaseData.add(txtSupplyID.getText());
                purchaseData.add(txtSupplierName.getText());
                purchaseData.add(txtSupplierCode.getText());
                purchaseData.add(txtSelectedItem.getText());
                purchaseData.add(String.valueOf(quantity));
                purchaseData.add(String.format("%.2f", cost));
                purchaseData.add(cboProfitIncrease.getSelectedItem().toString());
                purchaseData.add(formattedSellingPrice); // Use the formatted selling price here
                purchaseData.add(txtDateSupplied.getText());

                purchaseModel.addRow(purchaseData);
                resetSelectedItemInputs();
                JOptionPane.showMessageDialog(this, "Item added to purchase list.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity and Cost must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource().equals(btnRemovePurchaseItem)) {
            int selectedRow = tblPurchaseDetails.getSelectedRow();
            if (selectedRow >= 0) {
                purchaseModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Item removed from purchase list.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource().equals(btnProcessPurchase)) {
            if (purchaseModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items to process.", "No Items", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Process this purchase?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                purchaseDb.appendRecords(purchaseModel);
                updateInventoryQuantities();
                saveRestockSummary();

                // This will refresh the inventory display
                if (inventorySystem1Ref != null) {
                    inventorySystem1Ref.refreshInventory();
                }

                JOptionPane.showMessageDialog(this, "Purchase processed successfully!", "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);

                purchaseModel.setRowCount(0);
                resetSupplierInputs();
                resetSelectedItemInputs();
                autoGenerateSupplyID();
            }
        } else if (e.getSource().equals(btnClose)) {
            if (purchaseModel.getRowCount() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Save changes before closing?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    purchaseDb.appendRecords(purchaseModel);
                    updateInventoryQuantities();
                    JOptionPane.showMessageDialog(this, "Data saved. Closing window.", "Saved", JOptionPane.INFORMATION_MESSAGE);
                    if (inventorySystem1Ref != null) {
                        inventorySystem1Ref.refreshInventory();
                    }
                    menuRef.setVisible(true);
                    this.dispose();
                } else if (confirm == JOptionPane.NO_OPTION) {
                    menuRef.setVisible(true);
                    this.dispose();
                }
            } else {
                menuRef.setVisible(true);
                this.dispose();
            }
        }
    }

    private class Database {
        private String filename;

        public Database(String filename) {
            this.filename = filename;
        }

        public void displayRecord(DefaultTableModel model) {
            model.setRowCount(0);
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
                System.out.println("Database file '" + filename + "' not found.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(rootPane, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void appendRecords(DefaultTableModel model) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
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
                JOptionPane.showMessageDialog(rootPane, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplyPurchase(null, null));
    }
}