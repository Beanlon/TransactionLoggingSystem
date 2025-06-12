import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import utils.Inputvalidator;
import utils.InventoryManager;
import utils.Item;
import utils.TransactionFileManager;
import utils.TransactionFileManager.TransactionData;

public class TransactionFrame extends JFrame implements ActionListener {

    private String transactionNumber;
    private JTextField txtQuantity, txtSearch;
    private JComboBox<Item> comboItem;
    private JLabel lblNameValue, lblDateValue, line2;
    private JTable table;
    public DefaultTableModel model;
    public JButton btnadd, btnedit, btnremove, btnclear, btnsave, btnBack;
    private boolean saved = true;
    private Map<String, InventoryManager> inventoryMap;

    // Constructor for new log
    public TransactionFrame(String name, String date) {
        this(name, date, null);
        setNameAndDate(name, date);
    }

    // Constructor for existing log file
    public TransactionFrame(String logname, String date, String filepath) {
        setTitle("Transaction Panel");
        setSize(700, 680);
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
        content.setBackground(new Color(255, 255, 255));
        mainWrapper.add(content, BorderLayout.CENTER);

        JPanel logdetails = new JPanel();
        logdetails.setLayout(null);
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

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(246, 241, 241));
        leftPanel.setLayout(null);

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblItem, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(comboItem, gbc);

        JLabel lblQuantity = new JLabel("QUANTITY:");
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setForeground(Color.WHITE);

        txtQuantity = new JTextField();
        txtQuantity.setPreferredSize(new Dimension(140, 30));
        Inputvalidator.makeNumericOnly(txtQuantity);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(lblQuantity, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(txtQuantity, gbc);

        JPanel panelInputmain = new JPanel(new BorderLayout());
        panelInputmain.setBounds(10, 240, 565, 35);
        panelInputmain.setBorder(BorderFactory.createEtchedBorder());
        panelInputmain.setBackground(new Color(255, 255, 255));
        content.add(panelInputmain);

        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelbtn.setBackground(new Color(255, 255, 255));

        btnadd = new JButton("ADD");
        btnremove = new JButton("REMOVE");
        btnclear = new JButton("CLEAR");
        btnsave = new JButton("SAVE");

        btnadd.addActionListener(this);
        btnsave.addActionListener(this);
        btnremove.addActionListener(this);
        btnclear.addActionListener(this);

        panelbtn.add(btnadd);
        panelbtn.add(btnremove);
        panelbtn.add(btnclear);
        panelbtn.add(btnsave);

        JPanel panelsearch = new JPanel();
        panelsearch.setLayout(new BoxLayout(panelsearch, BoxLayout.X_AXIS));
        panelsearch.setBackground(new Color(255, 255, 255));
        panelsearch.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(150, 30));
        txtSearch.setMaximumSize(new Dimension(150, 30));
        txtSearch.setBackground(new Color(238, 235, 235));
        txtSearch.setText("Search..");
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

        panelsearch.add(Box.createHorizontalGlue());
        panelsearch.add(txtSearch);

        panelInputmain.add(panelbtn, BorderLayout.WEST);
        panelInputmain.add(panelsearch, BorderLayout.EAST);

        String[] columnNames = {"ITEM", "PRICE", "QUANTITY", "SUBTOTAL"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
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

        if (filepath != null && !filepath.isEmpty()) {
            loadFromFile(filepath);
        }
    }

    private List<Item> loadInventoryItems() {
        List<Item> items = new ArrayList<>();
        inventoryMap = InventoryManager.loadInventory();  // load and store for updating later

        for (InventoryManager inv : inventoryMap.values()) {
            items.add(new Item(inv.getName(), inv.getPrice(), inv.getQuantity()));
        }

        return items;
    }

    public void setNameAndDate(String name, String date) {
        lblNameValue.setText(name);
        lblDateValue.setText(date);
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
            if (quantityInt > selectedItem.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Not enough stock. Available: " + selectedItem.getQuantity());
                return;
            }

            double subtotal = selectedItem.getPrice() * quantityInt;
            model.addRow(new Object[]{selectedItem.getName(), selectedItem.getPrice(), quantityInt, subtotal});

            selectedItem.reduceQuantity(quantityInt);
            InventoryManager inv = inventoryMap.get(selectedItem.getName());
            if (inv != null) {
                inv.reduceQuantity(quantityInt);
            }

            int selectedIndex = comboItem.getSelectedIndex();
            ((DefaultComboBoxModel<Item>) comboItem.getModel()).removeElementAt(selectedIndex);
            ((DefaultComboBoxModel<Item>) comboItem.getModel()).insertElementAt(selectedItem, selectedIndex);
            comboItem.setSelectedIndex(selectedIndex);

            clearInputs();
            saved = false;
        } else if (source == btnremove) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
                return;
            }
            model.removeRow(selectedRow);
            clearInputs();
            saved = false;
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
                new Menu();
                dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        this.saveToFile();
                        new Menu();
                        this.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    new Menu();
                    this.dispose();
                }
            }
        }
    }

    private void clearInputs() {
        txtQuantity.setText("");
        comboItem.setSelectedIndex(0);
    }

    public void saveToFile() throws IOException {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdir();
        String filename = "logs/" + lblNameValue.getText() + ".csv";
        File file = new File(filename);

        TransactionFileManager.saveToFile(file, lblNameValue.getText(), lblDateValue.getText(), transactionNumber, model);
        InventoryManager.saveInventory(inventoryMap);
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
            model.setRowCount(0);
            for (String[] row : data.rows) {
                if (row.length == model.getColumnCount()) {
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private String generateRandomTransactionNumber() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String logname = "SampleLog";
            String date = "2025-06-11";
            String filepath = (args.length > 0) ? args[0] : null;
            TransactionFrame frame = new TransactionFrame(logname, date, filepath);
            frame.setVisible(true);
        });
    }
}
