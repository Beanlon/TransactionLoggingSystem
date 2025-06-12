import utils.InventoryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class InventorySystem extends JPanel {
    private JTextField txtItem, txtQuantity, txtPrice;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnRemove, btnSave, btnEdit;

    public InventorySystem() {
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Item:"));
        txtItem = new JTextField();
        inputPanel.add(txtItem);

        inputPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        inputPanel.add(txtQuantity);

        inputPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        inputPanel.add(txtPrice);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("ADD");
        btnRemove = new JButton("REMOVE");
        btnEdit = new JButton("EDIT");
        btnSave = new JButton("SAVE");

        btnAdd.addActionListener(e -> addRow());
        btnRemove.addActionListener(e -> removeRow());
        btnEdit.addActionListener(e -> editItem());
        btnSave.addActionListener(e -> saveToFile());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);

        // Table setup
        String[] columnNames = {"Item", "Quantity", "Price"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 400));
        JScrollPane scrollPane = new JScrollPane(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                txtItem.setText(model.getValueAt(selectedRow, 0).toString());
                txtQuantity.setText(model.getValueAt(selectedRow, 1).toString());
                txtPrice.setText(model.getValueAt(selectedRow, 2).toString());
            }
        });

        // Combine all into main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadFromFile();
    }

    private void addRow() {
        String item = txtItem.getText().trim();
        String quantityText = txtQuantity.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (item.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            model.addRow(new Object[]{item, quantity, String.format("%.2f", price)});
            clearInputs();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be an integer and price must be a valid number.");
        }
    }

    private void removeRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        } else {
            model.removeRow(selectedRow);
        }
    }

    private void clearInputs() {
        txtItem.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
    }

    private void editItem() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        String item = txtItem.getText().trim();
        String quantityText = txtQuantity.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (item.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields to edit the item.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText.replace(",", ""));

            model.setValueAt(item, selectedRow, 0);
            model.setValueAt(quantity, selectedRow, 1);
            model.setValueAt(String.format("%.2f", price), selectedRow, 2);

            clearInputs();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be and integer and price must be a valid number.");


        }

    }

    private void saveToFile() {
        Map<String, InventoryManager> inventory = new LinkedHashMap<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                String name = model.getValueAt(i, 0).toString();
                int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                double price = Double.parseDouble(model.getValueAt(i, 2).toString());
                inventory.put(name, new InventoryManager(name, quantity, price));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error parsing data in row " + (i + 1));
                return;
            }
        }

        InventoryManager.saveInventory(inventory);
        JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
    }

    private void loadFromFile() {
        Map<String, InventoryManager> inventory = InventoryManager.loadInventory();
        model.setRowCount(0); // Clear existing table rows

        for (InventoryManager item : inventory.values()) {
            model.addRow(new Object[]{
                    item.getName(),
                    item.getQuantity(),
                    String.format("%.2f", item.getPrice())
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventory System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new InventorySystem());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
