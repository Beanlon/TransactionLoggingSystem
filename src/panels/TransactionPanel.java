package panels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import utils.TransactionFileManager;
import utils.TransactionFileManager.TransactionData;

public class TransactionPanel extends JPanel implements ActionListener {
    private JTextField txtName, txtItem, txtPrice, txtQuantity, txtSearch;
    private JLabel lblNameValue, lblDateValue;
    private JTable table;
    public DefaultTableModel model;
    public JButton btnadd, btnedit, btnremove, btnclear, btnsave;

    public TransactionPanel() {
        setLayout(null);
        setBackground(new Color(189, 189, 189));

        // ===== Title Panel =====
        JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        panelTitle.setBounds(10, 10, 565, 60);
        panelTitle.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelTitle.setBackground(Color.white);

        JLabel lblTitle = new JLabel("TRANSACTION LOGGING SYSTEM", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);
        panelTitle.add(lblTitle);
        add(panelTitle);

        // ===== Info Panel Split into Left and Right =====
        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        panelInfo.setBounds(10, 80, 565, 200);
        add(panelInfo);

        // ===== Left Panel (Displays name and date) =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new GridLayout(4, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblName = new JLabel("NAME:");
        lblNameValue = new JLabel("");

        JLabel lblDate = new JLabel("DATE:");
        lblDateValue = new JLabel("");

        leftPanel.add(lblName);
        leftPanel.add(lblNameValue);
        leftPanel.add(lblDate);
        leftPanel.add(lblDateValue);

        panelInfo.add(leftPanel);

        // ===== Right Panel (Form) =====
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(120, 26, 26)); // Optional styling
        rightPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panelInfo.add(rightPanel); // Add to right half

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        lblName = new JLabel("NAME:");
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        lblName.setForeground(Color.WHITE);
        txtName = new JTextField(15);
        txtName.setPreferredSize(new Dimension(140, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblName, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtName, gbc);

        JLabel lblItem = new JLabel("ITEM:");
        lblItem.setFont(new Font("Arial", Font.BOLD, 13));
        lblItem.setForeground(Color.WHITE);
        txtItem = new JTextField();
        txtItem.setPreferredSize(new Dimension(140, 30));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblItem, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtItem, gbc);

        JLabel lblPrice = new JLabel("PRICE:");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrice.setForeground(Color.WHITE);
        txtPrice = new JTextField();
        txtPrice.setPreferredSize(new Dimension(140, 30));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblPrice, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtPrice, gbc);

        JLabel lblQuantity = new JLabel("QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setForeground(Color.WHITE);
        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(140, 30));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblQuantity, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtQuantity, gbc);

        // ===== Button + Search Panel =====
        JPanel panelInputmain = new JPanel(new BorderLayout());
        panelInputmain.setBounds(10, 290, 565, 35);
        panelInputmain.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panelInputmain.setBackground(new Color(120, 26, 26));
        add(panelInputmain);

        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        panelbtn.setBackground(new Color(120, 26, 26));
        btnadd = new JButton("ADD");
        btnremove = new JButton("REMOVE");
        btnedit = new JButton("EDIT");
        btnclear = new JButton("CLEAR");
        btnsave = new JButton("SAVE");

        btnadd.addActionListener(this);
        btnedit.addActionListener(this);
        btnsave.addActionListener(this);
        btnremove.addActionListener(this);
        btnclear.addActionListener(this);

        panelbtn.add(btnadd);
        panelbtn.add(btnremove);
        panelbtn.add(btnedit);
        panelbtn.add(btnclear);
        panelbtn.add(btnsave);

        JPanel panelsearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 5));
        panelsearch.setBackground(new Color(120, 26, 26));
        txtSearch = new JTextField(15);
        txtSearch.setText("Search..");

        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search..")) {
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

        panelsearch.add(txtSearch);

        panelInputmain.add(panelbtn, BorderLayout.WEST);
        panelInputmain.add(panelsearch, BorderLayout.EAST);

        // ===== Table Panel =====
        String[] columnNames = {"Name", "Item", "Price", "Quantity"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBounds(10, 335, 565, 280);
        panelTable.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelTable.add(scrollPane, BorderLayout.CENTER);
        add(panelTable);
    }

    public void setNameAndDate(String name, String date) {
        lblNameValue.setText(name);
        lblDateValue.setText(date);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnadd) {
            // Add a new row to the table from input fields
            String name = txtName.getText().trim();
            String item = txtItem.getText().trim();
            String price = txtPrice.getText().trim();
            String quantity = txtQuantity.getText().trim();

            if (name.isEmpty() || item.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before adding.");
                return;
            }

            model.addRow(new Object[]{name, item, price, quantity});
            clearInputs();

        } else if (source == btnedit) {
            // Edit selected row
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }
            model.setValueAt(txtName.getText(), selectedRow, 0);
            model.setValueAt(txtItem.getText(), selectedRow, 1);
            model.setValueAt(txtPrice.getText(), selectedRow, 2);
            model.setValueAt(txtQuantity.getText(), selectedRow, 3);
            clearInputs();

        } else if (source == btnremove) {
            // Remove selected row
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            model.removeRow(selectedRow);
            clearInputs();

        } else if (source == btnclear) {
            // Clear input fields
            clearInputs();

        } else if (source == btnsave) {
            try {
                saveToFile();
                JOptionPane.showMessageDialog(this, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }

    // Helper method to clear input fields
    private void clearInputs() {
        txtName.setText("");
        txtItem.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }

    // Updated save method: no filename input, builds filename from name + date
    public void saveToFile() throws IOException {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdir();

        String filename = "logs/" + lblNameValue.getText() + "_" + lblDateValue.getText() + ".csv";
        File file = new File(filename);

        TransactionFileManager.saveToFile(file, lblNameValue.getText(), lblDateValue.getText(), model);
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

            model.setRowCount(0); // Clear table
            for (String[] row : data.rows) {
                if (row.length == model.getColumnCount()) {
                    model.addRow(row);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }
}
