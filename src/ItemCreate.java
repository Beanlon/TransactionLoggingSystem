import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Vector;

public class ItemCreate extends JFrame implements ActionListener {


    JPanel panelInfo;
    JLabel lblID, lblName, lblCategory, lblSearch, lblDateAdded;
    JTextField txtID, txtName, txtSearch;
    JButton btnAdd, btnClear, btnEdit, btnDelete, btnClose, btnSearch;
    JTable tblSupply;
    DefaultTableModel supply;
    JComboBox<String> cboCategory;
    JComboBox<String> cboAddedDay, cboAddedMonth, cboAddedYear;

    Vector<String> field = new Vector<>();
    Database db = new Database("Items.txt");

    public ItemCreate() {
        this.setLayout(null);
        this.setSize(900, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        JLabel lblHeader = new JLabel("Manage Supermarket Items");
        lblHeader.setBounds(60, 5, 400, 100);
        lblHeader.setForeground(new Color(200, 0, 0));
        lblHeader.setFont(new Font("DM Sans", Font.BOLD, 20));
        this.add(lblHeader);

        panelInfo = new RoundedPanel(30);
        panelInfo.setLayout(null);
        panelInfo.setBounds(50, 80, 400, 350);
        panelInfo.setBackground(Color.WHITE);

        JLabel subheader = new JLabel("Fill Up Item Information");
        subheader.setBounds(20, 30, 300, 25);
        subheader.setFont(new Font("Arial", Font.BOLD, 18));
        subheader.setForeground(new Color(10, 10, 10));
        panelInfo.add(subheader);

        lblID = new JLabel("Item ID Number: ");
        txtID = new JTextField();
        txtID.setEditable(false);
        lblName = new JLabel("Item Name:");
        txtName = new JTextField();
        lblCategory = new JLabel("Item Category:");

        String[] categories = {
                "Canned Goods", "Beverages", "Snacks", "Dairy & Chilled Goods",
                "Frozen Foods", "Produce", "Bakery", "Meat & Seafood",
                "Pantry Staples", "Household Essentials", "Personal Care"
        };
        cboCategory = new JComboBox<>(categories);

        lblDateAdded = new JLabel("Date Added:");

        String[] days = new String[31];
        String[] months = new String[12];
        String[] years = new String[50];
        for (int i = 0; i < 31; i++) days[i] = String.valueOf(i + 1);
        for (int i = 0; i < 12; i++) months[i] = String.valueOf(i + 1);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 50; i++) years[i] = String.valueOf(currentYear - i);

        cboAddedDay = new JComboBox<>(days);
        cboAddedMonth = new JComboBox<>(months);
        cboAddedYear = new JComboBox<>(years);

        lblID.setBounds(20, 80, 120, 25);
        txtID.setBounds(150, 80, 200, 25);
        lblName.setBounds(20, 115, 120, 25);
        txtName.setBounds(150, 115, 200, 25);
        lblCategory.setBounds(20, 150, 120, 25);
        cboCategory.setBounds(150, 150, 200, 25);
        lblDateAdded.setBounds(20, 185, 120, 25);

        JPanel addedDatePanel = createDatePanel(cboAddedDay, cboAddedMonth, cboAddedYear);
        addedDatePanel.setBounds(150, 185, 200, 25);
        addedDatePanel.setBackground(Color.white);

        btnAdd = new JButton("Add");
        btnClear = new JButton("Clear");
        btnAdd.setBounds(20, 260, 360, 30);
        btnClear.setBounds(20, 300, 360, 30);
        btnAdd.setBackground(new Color(200, 0, 0));
        btnClear.setBackground(new Color(200, 0, 0));
        btnAdd.setForeground(Color.white);
        btnClear.setForeground(Color.white);

        panelInfo.add(lblID);
        panelInfo.add(txtID);
        panelInfo.add(lblName);
        panelInfo.add(txtName);
        panelInfo.add(lblCategory);
        panelInfo.add(cboCategory);
        panelInfo.add(lblDateAdded);
        panelInfo.add(addedDatePanel);
        panelInfo.add(btnAdd);
        panelInfo.add(btnClear);
        this.add(panelInfo);

        lblSearch = new JLabel("Search:");
        txtSearch = new JTextField();
        btnSearch = new JButton("Search");

        lblSearch.setBounds(460, 60, 60, 30);
        txtSearch.setBounds(520, 60, 200, 30);
        btnSearch.setBounds(740, 60, 80, 25);
        btnSearch.setBackground(new Color(200, 0, 0));
        btnSearch.setForeground(Color.white);
        this.add(lblSearch);
        this.add(txtSearch);
        this.add(btnSearch);

        field.add("ID");
        field.add("Name");
        field.add("Category");
        field.add("Date Added");

        supply = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supply.setColumnIdentifiers(field);

        tblSupply = new JTable(supply);
        tblSupply.setBackground(Color.WHITE);
        tblSupply.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSupply.setShowGrid(true);
        tblSupply.setGridColor(Color.LIGHT_GRAY);
        tblSupply.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tblSupply);
        scrollPane.setBounds(460, 100, 400, 280);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane);

        db.displayRecord(supply); // Initial load
        tblSupply.setModel(supply); // Ensure correct model is active

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");

        btnEdit.setBounds(540, 390, 100, 30);
        btnDelete.setBounds(650, 390, 100, 30);
        btnClose.setBounds(760, 390, 100, 30);
        for (JButton b : new JButton[]{btnEdit, btnDelete, btnClose}) {
            b.setBackground(new Color(200, 0, 0));
            b.setForeground(Color.white);
            this.add(b);
        }

        btnAdd.addActionListener(this);
        btnClear.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClose.addActionListener(this);
        btnSearch.addActionListener(this);
        txtSearch.addActionListener(this);

        tblSupply.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblSupply.getSelectedRow();
                if (row >= 0) {
                    txtID.setText(supply.getValueAt(row, 0).toString());
                    txtName.setText(supply.getValueAt(row, 1).toString());
                    cboCategory.setSelectedItem(supply.getValueAt(row, 2).toString());
                    String[] dateAdded = supply.getValueAt(row, 3).toString().split("/");
                    if (dateAdded.length == 3) {
                        cboAddedDay.setSelectedItem(dateAdded[0]);
                        cboAddedMonth.setSelectedItem(dateAdded[1]);
                        cboAddedYear.setSelectedItem(dateAdded[2]);
                    }
                    txtID.setEnabled(false);
                    btnAdd.setEnabled(false);
                }
            }
        });

        JPanel background = new JPanel();
        background.setBackground(new Color(246, 243, 243));
        background.setSize(900, 480);
        this.add(background);

        this.setVisible(true);
        autoGenerateID();
    }

    private JPanel createDatePanel(JComboBox<String> day, JComboBox<String> month, JComboBox<String> year) {
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(day);
        datePanel.add(month);
        datePanel.add(year);
        return datePanel;
    }

    private void autoGenerateID() {
        int nextId = 1;
        for (int i = 0; i < supply.getRowCount(); i++) {
            try {
                int currentId = Integer.parseInt(supply.getValueAt(i, 0).toString());
                if (currentId >= nextId) {
                    nextId = currentId + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Warning: Non-numeric ID found: " + supply.getValueAt(i, 0));
            }
        }
        txtID.setText(String.valueOf(nextId));
    }

    private void reset() {
        txtName.setText("");
        cboCategory.setSelectedIndex(0);
        Calendar today = Calendar.getInstance();
        cboAddedDay.setSelectedItem(String.valueOf(today.get(Calendar.DAY_OF_MONTH)));
        cboAddedMonth.setSelectedItem(String.valueOf(today.get(Calendar.MONTH) + 1));
        cboAddedYear.setSelectedItem(String.valueOf(today.get(Calendar.YEAR)));
        txtID.setEnabled(true);
        btnAdd.setEnabled(true);
        tblSupply.clearSelection();
        autoGenerateID();
    }

    private boolean isEmptyInput() {
        return txtName.getText().trim().isEmpty();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAdd)) {
            if (isEmptyInput()) {
                JOptionPane.showMessageDialog(this, "Please fill in the Item Name.");
                return;
            }
            Vector<String> data = new Vector<>();
            data.add(txtID.getText());
            data.add(txtName.getText());
            data.add(cboCategory.getSelectedItem().toString());
            data.add(cboAddedDay.getSelectedItem() + "/" + cboAddedMonth.getSelectedItem() + "/" + cboAddedYear.getSelectedItem());
            supply.addRow(data);
            reset();
            db.overwriteRecords(supply);
            JOptionPane.showMessageDialog(this, "Item added successfully!");
        } else if (e.getSource().equals(btnClear)) {
            reset();
        } else if (e.getSource().equals(btnEdit)) {
            int row = tblSupply.getSelectedRow();
            if (row >= 0) {
                if (isEmptyInput()) {
                    JOptionPane.showMessageDialog(this, "Please fill in the Item Name.");
                    return;
                }
                supply.setValueAt(txtName.getText(), row, 1);
                supply.setValueAt(cboCategory.getSelectedItem().toString(), row, 2);
                supply.setValueAt(cboAddedDay.getSelectedItem() + "/" + cboAddedMonth.getSelectedItem() + "/" + cboAddedYear.getSelectedItem(), row, 3);
                reset();
                db.overwriteRecords(supply);
                JOptionPane.showMessageDialog(this, "Item edited successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a record to edit.");
            }
        } else if (e.getSource().equals(btnDelete)) {
            int selectedRow = tblSupply.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    supply.removeRow(selectedRow);
                    reset();
                    db.overwriteRecords(supply);
                    JOptionPane.showMessageDialog(this, "Item deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            }
        } else if (e.getSource().equals(btnSearch) || e.getSource().equals(txtSearch)) {
            String searchTerm = txtSearch.getText().trim().toLowerCase();

            if (searchTerm.isEmpty()) {
                tblSupply.setModel(supply); // Restore main model
                db.displayRecord(supply);
                return;
            }

            DefaultTableModel searchResultsModel = new DefaultTableModel();
            searchResultsModel.setColumnIdentifiers(field);

            boolean found = false;
            for (int i = 0; i < supply.getRowCount(); i++) {
                Vector<String> rowData = new Vector<>();
                boolean match = false;
                for (int j = 0; j < supply.getColumnCount(); j++) {
                    String cell = supply.getValueAt(i, j).toString();
                    rowData.add(cell);
                    if (cell.toLowerCase().contains(searchTerm)) {
                        match = true;
                    }
                }
                if (match) {
                    searchResultsModel.addRow(rowData);
                    found = true;
                }
            }

            tblSupply.setModel(searchResultsModel);
            if (!found) {
                JOptionPane.showMessageDialog(this, "No match found for '" + searchTerm + "'.");
            }
        } else if (e.getSource().equals(btnClose)) {
            db.overwriteRecords(supply);
            this.dispose();
        }
    }

    private class Database {
        private final String filename;

        public Database(String filename) {
            this.filename = filename;
        }

        public void displayRecord(DefaultTableModel model) {
            model.setRowCount(0);
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        Vector<String> row = new Vector<>();
                        for (int i = 0; i < 4; i++) {
                            row.add(data[i].trim());
                        }
                        model.addRow(row);
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not load file: " + e.getMessage());
            }
        }

        public void overwriteRecords(DefaultTableModel model) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        bw.write(model.getValueAt(i, j).toString());
                        if (j < model.getColumnCount() - 1) {
                            bw.write(",");
                        }
                    }
                    bw.newLine();
                }
            } catch (IOException e) {
                System.err.println("Could not save file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemCreate::new);
    }
}
