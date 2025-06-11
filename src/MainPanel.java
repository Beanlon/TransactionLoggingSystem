import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

public class MainPanel extends JPanel implements ActionListener {
    JPanel pnlbtn;
    JButton createnew, load, exit;
    JTable logTable;
    DefaultTableModel tableModel;

    public MainPanel() {
        setLayout(null);

// ==== Top Horizontal Panels with Equal Sizes (3 Panels) ====
        JPanel paneloverview = new JPanel(new GridLayout(1, 3, 10, 0)); // 3 columns with 5px horizontal gap
        paneloverview.setBounds(10, 10, 765, 125); // Keep existing size

        Color panelColor = new Color(243, 243, 243); // Panel background color
        Font titleFont = new Font("SansSerif", Font.BOLD, 23);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 20);
        Color textColor = new Color(201, 42, 42);

// Panel 1 - Total Sales
        JPanel pnlsales = new JPanel();
        pnlsales.setBackground(panelColor);
        pnlsales.setLayout(new BoxLayout(pnlsales, BoxLayout.Y_AXIS));
        pnlsales.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel lblSalesTitle = new JLabel("Total Sales");
        lblSalesTitle.setFont(titleFont);
        lblSalesTitle.setForeground(textColor);
        lblSalesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSalesTitle.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblSalesValue = new JLabel("₱12,000.00"); // Placeholder value
        lblSalesValue.setFont(valueFont);
        lblSalesValue.setForeground(textColor);
        lblSalesValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSalesValue.setAlignmentY(Component.CENTER_ALIGNMENT);

        pnlsales.add(lblSalesTitle);
        pnlsales.add(Box.createVerticalStrut(10));
        pnlsales.add(lblSalesValue);

// Panel 2 - Total Transactions
        JPanel totaltrans = new JPanel();
        totaltrans.setBackground(panelColor);
        totaltrans.setLayout(new BoxLayout(totaltrans, BoxLayout.Y_AXIS));
        totaltrans.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel lblTransTitle = new JLabel("Total Transactions");
        lblTransTitle.setFont(titleFont);
        lblTransTitle.setForeground(textColor);
        lblTransTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTransTitle.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblTransValue = new JLabel("28"); // Placeholder value
        lblTransValue.setFont(valueFont);
        lblTransValue.setForeground(textColor);
        lblTransValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTransValue.setAlignmentY(Component.CENTER_ALIGNMENT);

        totaltrans.add(lblTransTitle);
        totaltrans.add(Box.createVerticalStrut(10));
        totaltrans.add(lblTransValue);

// Panel 3 - Most Bought Item
        JPanel itemssold = new JPanel();
        itemssold.setBackground(panelColor);
        itemssold.setLayout(new BoxLayout(itemssold, BoxLayout.Y_AXIS));
        itemssold.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel lblMostBoughtTitle = new JLabel("Most Bought Item");
        lblMostBoughtTitle.setFont(titleFont);
        lblMostBoughtTitle.setForeground(textColor);
        lblMostBoughtTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMostBoughtTitle.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblMostBoughtValue = new JLabel("Coffee"); // Placeholder value
        lblMostBoughtValue.setFont(valueFont);
        lblMostBoughtValue.setForeground(textColor);
        lblMostBoughtValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMostBoughtTitle.setAlignmentY(Component.CENTER_ALIGNMENT);

        itemssold.add(lblMostBoughtTitle);
        itemssold.add(Box.createVerticalStrut(10));
        itemssold.add(lblMostBoughtValue);

// Add all panels to the container
        paneloverview.add(pnlsales);
        paneloverview.add(totaltrans);
        paneloverview.add(itemssold);

        // ==== Button Panel ====
        pnlbtn = new JPanel();
        pnlbtn.setBounds(10, 140, 480, 26);
        pnlbtn.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlbtn.setOpaque(false);

        Dimension btnSize = new Dimension(90, 26);

        createnew = new JButton("ADD");
        createnew.setPreferredSize(btnSize);
        createnew.addActionListener(this);

        load = new JButton("LOAD");
        load.setPreferredSize(btnSize);
        load.addActionListener(this);

        exit = new JButton("DELETE");
        exit.setPreferredSize(btnSize);
        exit.addActionListener(this);

        pnlbtn.add(createnew);
        pnlbtn.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlbtn.add(load);
        pnlbtn.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlbtn.add(exit);

        // ==== Search and Filter Panel ====
        JPanel Searchfilter = new JPanel();
        Searchfilter.setBounds(495, 140, 280, 26);
        Searchfilter.setLayout(new GridLayout(1, 2, 5, 0));
        Searchfilter.setOpaque(false);

        JComboBox<String> month = new JComboBox<>();
        month.setPreferredSize(new Dimension(150, 30));
        month.setMaximumSize(new Dimension(150, 30));
        month.setBackground(new Color(238, 235, 235));
        Searchfilter.add(month);

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(150, 30));
        txtSearch.setMaximumSize(new Dimension(150, 30));
        txtSearch.setBackground(new Color(238, 235, 235));
        txtSearch.setText("Search..");
        txtSearch.setForeground(Color.GRAY);

        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search")) {
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

        Searchfilter.add(txtSearch);

        // ==== Table for saved logs ====
        String[] columns = { "Log Name", "Transaction No.", "Date", "Last Modified" };
        tableModel = new DefaultTableModel(columns, 0);
        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        logTable.setPreferredScrollableViewportSize(new Dimension(760, 400));

        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setPreferredSize(new Dimension(780, 300));

        JPanel paneltable = new JPanel(new BorderLayout());
        paneltable.setBounds(10, 175, 765, 375);
        paneltable.add(scrollPane, BorderLayout.CENTER);

        loadSavedLogs();

        // ==== Add components to MainPanel ====
        add(paneloverview);
        add(pnlbtn);
        add(Searchfilter);
        add(paneltable);
    }

    private void loadSavedLogs() {
        File dir = new File("logs");
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (File file : files) {
                String filename = file.getName();
                String modified = sdf.format(file.lastModified());
                // You can later add transaction number and date if needed
                tableModel.addRow(new Object[]{filename, "", "", modified});
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createnew) {
            new createLog();  // Open new log creation window
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }

        } else if (e.getSource() == load) {

        }

    }
}
