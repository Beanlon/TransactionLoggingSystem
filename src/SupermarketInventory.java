import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Calendar;
import java.util.Vector;

public class    SupermarketInventory implements ActionListener {

    JFrame myFrame;
    JPanel panelInfo;
    JLabel lblID, lblName, lblCategory, lblSearch, lblDateAdded;
    JTextField txtID, txtName, txtSearch;
    JButton btnAdd, btnClear, btnEdit, btnDelete, btnClose, btnSearch;
    JTable tblSupply;
    DefaultTableModel supply;
    JComboBox<String> cboCategory;
    JComboBox<String> cboAddedDay, cboAddedMonth, cboAddedYear;

    Vector<String> field = new Vector<>();
    Database db = new Database("SupermarketInventory.txt"); // Changed filename

    public SupermarketInventory() {
        myFrame = new JFrame("Supermarket Inventory");
        myFrame.setLayout(null);
        myFrame.setSize(900, 480);
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setResizable(false);

        // Header Label
        JLabel lblHeader = new JLabel("Manage Supermarket Items");
        lblHeader.setBounds(60, 5, 400, 100);
        lblHeader.setForeground(new Color(200, 0, 0)); // Red color
        lblHeader.setFont(new Font("DM Sans", Font.BOLD, 20));
        myFrame.add(lblHeader);

        // Panel for item information
        panelInfo = new RoundedPanel(30);
        panelInfo.setLayout(null);
        panelInfo.setBounds(50, 80, 400, 350);
        panelInfo.setBackground(Color.WHITE);

        JLabel subheader = new JLabel("Fill Up Item Information");
        subheader.setBounds(20, 30, 300, 25);
        subheader.setFont(new Font("Arial", Font.BOLD, 18));
        subheader.setForeground(new Color(10, 10, 10, 255));
        panelInfo.add(subheader);

        // Input Fields
        lblID = new JLabel("Item ID Number: ");
        txtID = new JTextField();
        txtID.setEditable(false); // Auto-generated
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
        btnAdd.setBackground(new Color(200, 0, 0)); // Red button
        btnClear.setBackground(new Color(200, 0, 0)); // Red button
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
        myFrame.add(panelInfo);

        // Search Panel
        lblSearch = new JLabel("Search:");
        txtSearch = new JTextField();
        btnSearch = new JButton("Search");

        lblSearch.setBounds(460, 60, 60, 30);
        txtSearch.setBounds(520, 60, 200, 30);
        btnSearch.setBounds(740, 60, 80, 25);
        btnSearch.setBackground(new Color(200, 0, 0)); // Red button
        btnSearch.setForeground(Color.white);
        myFrame.add(lblSearch);
        myFrame.add(txtSearch);
        myFrame.add(btnSearch);

        // Table setup
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
        myFrame.add(scrollPane);

        db.displayRecord(supply); // Load existing records

        // Action Buttons for Table
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");

        btnEdit.setBounds(540, 390, 100, 30);
        btnDelete.setBounds(650, 390, 100, 30);
        btnClose.setBounds(760, 390, 100, 30);
        for (JButton b : new JButton[]{btnEdit, btnDelete, btnClose}) {
            b.setBackground(new Color(200, 0, 0)); // Red button
            b.setForeground(Color.white);
            myFrame.add(b);
        }

        // Action Listeners
        btnAdd.addActionListener(this);
        btnClear.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClose.addActionListener(this);
        btnSearch.addActionListener(this);
        txtSearch.addActionListener(this); // Allow searching on Enter key

        // Table row selection listener
        tblSupply.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblSupply.getSelectedRow();
                if (row >= 0) {
                    txtID.setText(supply.getValueAt(row, 0).toString());
                    txtName.setText(supply.getValueAt(row, 1).toString());
                    cboCategory.setSelectedItem(supply.getValueAt(row, 2).toString()); // Category is now at index 2

                    String[] dateAdded = supply.getValueAt(row, 3).toString().split("/"); // Date Added is at index 3
                    if (dateAdded.length == 3) {
                        cboAddedDay.setSelectedItem(dateAdded[0]);
                        cboAddedMonth.setSelectedItem(dateAdded[1]);
                        cboAddedYear.setSelectedItem(dateAdded[2]);
                    }

                    txtID.setEnabled(false); // ID is disabled for editing
                    btnAdd.setEnabled(false); // Cannot add when editing
                }
            }
        });

        // Main background panel
        JPanel background = new JPanel();
        background.setBackground(new Color(246, 243, 243));
        background.setSize(900, 480);
        myFrame.add(background);

        myFrame.setVisible(true);
        autoGenerateID(); // Generate ID on startup
    }

    private JPanel createDatePanel(JComboBox<String> day, JComboBox<String> month, JComboBox<String> year) {
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(day);
        datePanel.add(month);
        datePanel.add(year);
        return datePanel;
    }

    /**
     * Auto-generates an Item ID based on existing IDs in the table.
     * Finds the maximum existing ID and sets the next ID as max + 1.
     */
    private void autoGenerateID() {
        int nextId = 1;
        if (supply.getRowCount() > 0) {
            for (int i = 0; i < supply.getRowCount(); i++) {
                try {
                    int currentId = Integer.parseInt(supply.getValueAt(i, 0).toString());
                    if (currentId >= nextId) {
                        nextId = currentId + 1;
                    }
                } catch (NumberFormatException e) {
                    // Handle cases where ID might not be a valid number if data is corrupted
                    System.err.println("Warning: Non-numeric ID found in table: " + supply.getValueAt(i, 0));
                }
            }
        }
        txtID.setText(String.valueOf(nextId));
    }

    private void reset() {
        txtName.setText("");
        cboCategory.setSelectedIndex(0);
        // Set today's date for date added, or reset to default (e.g., current date)
        Calendar today = Calendar.getInstance();
        cboAddedDay.setSelectedItem(String.valueOf(today.get(Calendar.DAY_OF_MONTH)));
        cboAddedMonth.setSelectedItem(String.valueOf(today.get(Calendar.MONTH) + 1)); // Month is 0-indexed
        cboAddedYear.setSelectedItem(String.valueOf(today.get(Calendar.YEAR)));

        txtID.setEnabled(true);
        btnAdd.setEnabled(true);
        tblSupply.clearSelection();
        autoGenerateID(); // Generate a new ID after clearing
    }

    private boolean isEmptyInput() {
        return txtName.getText().trim().isEmpty(); // Only checking name as ID is auto-generated
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAdd)) {
            if (isEmptyInput()) {
                JOptionPane.showMessageDialog(myFrame, "Please fill in the Item Name.");
                return;
            }
            // ID uniqueness is handled by autoGenerateID ensuring sequential unique IDs
            Vector<String> data = new Vector<>();
            data.add(txtID.getText());
            data.add(txtName.getText());
            data.add(cboCategory.getSelectedItem().toString());
            data.add(cboAddedDay.getSelectedItem() + "/" + cboAddedMonth.getSelectedItem() + "/" + cboAddedYear.getSelectedItem());
            supply.addRow(data);
            reset();
            db.overwriteRecords(supply); // Save after adding
            JOptionPane.showMessageDialog(myFrame, "Item added successfully!");
        } else if (e.getSource().equals(btnClear)) {
            reset();
        } else if (e.getSource().equals(btnEdit)) {
            int row = tblSupply.getSelectedRow();
            if (row >= 0) {
                if (isEmptyInput()) {
                    JOptionPane.showMessageDialog(myFrame, "Please fill in the Item Name before editing.");
                    return;
                }
                supply.setValueAt(txtName.getText(), row, 1);
                supply.setValueAt(cboCategory.getSelectedItem().toString(), row, 2); // Category at index 2
                supply.setValueAt(cboAddedDay.getSelectedItem() + "/" + cboAddedMonth.getSelectedItem() + "/" + cboAddedYear.getSelectedItem(), row, 3); // Date Added at index 3
                reset();
                db.overwriteRecords(supply); // Save after editing
                JOptionPane.showMessageDialog(myFrame, "Item edited successfully!");
            } else {
                JOptionPane.showMessageDialog(myFrame, "Please select a record to edit.");
            }
        } else if (e.getSource().equals(btnDelete)) {
            int selectedRow = tblSupply.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(myFrame, "Are you sure you want to delete this item?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    supply.removeRow(selectedRow);
                    reset();
                    db.overwriteRecords(supply); // Save after deleting
                    JOptionPane.showMessageDialog(myFrame, "Item deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(myFrame, "Please select a record to delete.");
            }
        } else if (e.getSource().equals(btnSearch) || e.getSource().equals(txtSearch)) {
            String searchTerm = txtSearch.getText().trim().toLowerCase();

            // Create a temporary model to display search results
            DefaultTableModel searchResultsModel = new DefaultTableModel();
            searchResultsModel.setColumnIdentifiers(field); // Use the same column identifiers

            boolean found = false;
            for (int i = 0; i < supply.getRowCount(); i++) {
                boolean rowMatches = false;
                Vector<String> rowData = new Vector<>();
                for (int j = 0; j < supply.getColumnCount(); j++) {
                    String value = supply.getValueAt(i, j).toString().toLowerCase();
                    rowData.add(supply.getValueAt(i, j).toString()); // Add original value to rowData
                    if (value.contains(searchTerm)) {
                        rowMatches = true;
                    }
                }
                if (rowMatches) {
                    searchResultsModel.addRow(rowData);
                    found = true;
                }
            }

            tblSupply.setModel(searchResultsModel); // Set the table to display search results

            if (!found && !searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(myFrame, "No matching record found for '" + txtSearch.getText() + "'.");
            } else if (searchTerm.isEmpty()) {
                db.displayRecord(supply); // Reload all records if search term is empty
            }
        } else if (e.getSource().equals(btnClose)) {
            db.overwriteRecords(supply);
            JOptionPane.showMessageDialog(myFrame, "Data saved. Closing application.");
            myFrame.dispose();
            // Assuming AdminDashboard exists for navigation
            // new AdminDashboard();
        }
    }

    /**
     * Inner class to handle file operations for the inventory.
     * This keeps the Database logic self-contained within the SupermarketInventory class.
     */
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
                // File might not exist yet, which is fine for the first run
                System.out.println("Database file not found. A new one will be created upon saving.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(myFrame, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(myFrame, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        // Ensure UI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(SupermarketInventory::new);
    }
}