import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class InventorySystem extends JPanel {
    private JTextField txtItem, txtQuantity, txtPrice;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnRemove, btnSave;

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
        btnAdd = new JButton("Add");
        btnRemove = new JButton("Remove");
        btnSave = new JButton("Save");

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnSave);

        // Table for displaying inventory
        String[] columnNames = {"Item", "Quantity", "Price"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(400, 400));
        JScrollPane scrollPane = new JScrollPane(table);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadFromFile();
    }

    private void addRow() {
        String item = txtItem.getText().trim();
        String quantity = txtQuantity.getText().trim();
        String price = txtPrice.getText().trim();

        if (item.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.");
            return;
        }

        try {
            int quantityInt = Integer.parseInt(quantity);
            double priceDouble = Double.parseDouble(price);
            model.addRow(new Object[]{item, quantityInt, priceDouble});
            clearInputs();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be an integer and price must be a number.");
        }
    }

    private void removeRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove.");
            return;
        }
        model.removeRow(selectedRow);
    }

    private void clearInputs() {
        txtItem.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("inventory.txt"))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.println(model.getValueAt(i, 0) + "," + model.getValueAt(i, 1) + "," + model.getValueAt(i, 2));
            }
            JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving inventory: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File("inventory.txt");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventorySystem().setVisible(true);
            }
        });
    }
}