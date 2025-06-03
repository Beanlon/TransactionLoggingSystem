package panels;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TransactionPanel extends JPanel implements ActionListener {
    public JTextField txtName, txtItem, txtPrice, txtQuantity, txtSearch;
    public JButton btnadd, btnedit, btnremove, btnclear, btnsave;
    public JTable table;
    public DefaultTableModel model;

    public TransactionPanel() {
        setLayout(null);

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

        // ===== Info Panel =====
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBounds(10, 80, 565, 200);
        panelInfo.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(panelInfo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);

        JLabel lblName = new JLabel("ENTER NAME:");
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        txtName = new JTextField(15);
        txtName.setPreferredSize(new Dimension(200,30));

        JLabel lblItem = new JLabel("ENTER ITEM:");
        lblItem.setFont(new Font("Arial", Font.BOLD, 13));
        txtItem = new JTextField(15);
        txtItem.setPreferredSize(new Dimension(200,30));

        JLabel lblPrice = new JLabel("ENTER PRICE:");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 13));
        txtPrice = new JTextField(15);
        txtPrice.setPreferredSize(new Dimension(200,30));

        JLabel lblQuantity = new JLabel("ENTER QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        txtQuantity = new JTextField(7);
        txtQuantity.setPreferredSize(new Dimension(200,30));

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panelInfo.add(lblName, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelInfo.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panelInfo.add(lblItem, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelInfo.add(txtItem, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panelInfo.add(lblPrice, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelInfo.add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panelInfo.add(lblQuantity, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelInfo.add(txtQuantity, gbc);

        // ===== Button + Search Panel =====
        JPanel panelInputmain = new JPanel(new BorderLayout());
        panelInputmain.setBounds(10, 290, 565, 35);
        panelInputmain.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(panelInputmain);

        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
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
        btnsave.addActionListener(this);


        panelbtn.add(btnadd);
        panelbtn.add(btnremove);
        panelbtn.add(btnedit);
        panelbtn.add(btnclear);
        panelbtn.add(btnsave);



        JPanel panelsearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 5));
        txtSearch = new JTextField(15);
        txtSearch.setForeground(Color.GRAY);
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
            // For now, just show a message - implement your save logic here
            JOptionPane.showMessageDialog(this, "Save function not implemented yet.");
        }
    }

    // Helper method to clear input fields
    private void clearInputs() {
        txtName.setText("");
        txtItem.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }


}

