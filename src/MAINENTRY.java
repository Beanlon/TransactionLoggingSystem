import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MAINENTRY extends JFrame {
    JLabel lblName, lblPrice, lblItem, lblQuantity, lblTitle;
    JTextField txtName, txtPrice, txtItem, txtQuantity;
    JPanel panelInfo, panelTitle,panelInputmain,panelbtn,panelsearch, panelTable;
    JButton btnadd, btnremove, btnedit, btnexport;
    JTextField txtSearch;
    JTable table;


    MAINENTRY() {
        // Frame setup
        setTitle("Transaction Logging System");
        setSize(600, 663);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // ====== Title Panel (Top) ======
        panelTitle = new JPanel(new FlowLayout());
        panelTitle.setBounds(10, 10, 565, 60);
        lblTitle = new JLabel("Transaction Logging System", SwingConstants.CENTER);

        lblTitle = new JLabel("Transaction Logging System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panelTitle.add(lblTitle);
        add(panelTitle, BorderLayout.NORTH);
        panelTitle.setPreferredSize(new Dimension(567, 60));
        panelTitle.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelTitle.setBackground(Color.white);
        // Optional styling

        //PanelINFO
        panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panelInfo.setBounds(10, 80, 565, 200);  // <== Set proper bounds (Y=80)
        add(panelInfo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15); // spacing

        // Labels and text fields
        lblName = new JLabel("Name:");
        txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(210, 30));

        lblItem = new JLabel("Item:");
        txtItem = new JTextField();
        txtItem.setPreferredSize(new Dimension(210, 30));

        lblPrice = new JLabel("Price:");
        txtPrice = new JTextField();
        txtPrice.setPreferredSize(new Dimension(210, 30));

        lblQuantity = new JLabel("Quantity:");
        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(70, 30));


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

        // ====== Button + Search Panel ======
        panelInputmain = new JPanel(new BorderLayout());
        panelInputmain.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panelInputmain.setBounds(10, 290, 565, 35);  // <== Set below panelInfo
        add(panelInputmain);

        panelbtn = new JPanel(new FlowLayout(FlowLayout.LEFT,6,2));
        btnadd = new JButton("ADD");
        btnedit = new JButton("EDIT");
        btnremove = new JButton("REMOVE");
        btnexport = new JButton("EXPORT");

        panelbtn.add(btnadd);
        panelbtn.add(btnremove);
        panelbtn.add(btnedit);
        panelbtn.add(btnexport);

        panelsearch = new JPanel(new FlowLayout(FlowLayout.RIGHT,4,5));
        txtSearch = new JTextField(15);
        panelsearch.add(txtSearch);
        String placeholder = "Search..";
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText(placeholder);

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals(placeholder)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText(placeholder);
                }
            }
        });



        panelInputmain.add(panelbtn, BorderLayout.WEST);
        panelInputmain.add(panelsearch, BorderLayout.EAST);

        String[] columnNames = { "Name", "Item", "Price", "Quantity" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0); // No data
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelTable.setBounds(10, 335, 565, 280);
        panelTable.add(scrollPane, BorderLayout.CENTER);
        add(panelTable);


        add(panelTitle);
        add(panelInfo);
        add(panelInputmain);
        add(panelTable);
        setVisible(true);
    }


}
