    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.attribute.BasicFileAttributes;
    import java.nio.file.attribute.FileTime;
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

            Color panelColor = new Color(255, 255, 255); // Panel background color
            Font titleFont = new Font("SansSerif", Font.BOLD, 23);
            Font valueFont = new Font("SansSerif", Font.PLAIN, 20);
            Color textColor = new Color(201, 42, 42);

            // ==== Panel 1 - Total Sales ====
            JPanel pnlsales = new JPanel(new BorderLayout());
            pnlsales.setBackground(panelColor);

            JPanel pnlline1 = new JPanel();
            pnlline1.setPreferredSize(new Dimension(5, 125));
            pnlline1.setBackground(textColor);
            pnlsales.add(pnlline1, BorderLayout.WEST);

            JPanel pnlSalesContent = new JPanel();
            pnlSalesContent.setLayout(new BoxLayout(pnlSalesContent, BoxLayout.Y_AXIS));
            pnlSalesContent.setBackground(panelColor);
            pnlSalesContent.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

            JLabel lblSalesTitle = new JLabel("Total Sales");
            lblSalesTitle.setFont(titleFont);
            lblSalesTitle.setForeground(textColor);
            lblSalesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblSalesValue = new JLabel("₱12,000.00");
            lblSalesValue.setFont(valueFont);
            lblSalesValue.setForeground(textColor);
            lblSalesValue.setAlignmentX(Component.CENTER_ALIGNMENT);

            pnlSalesContent.add(lblSalesTitle);
            pnlSalesContent.add(Box.createVerticalStrut(10));
            pnlSalesContent.add(lblSalesValue);

            pnlsales.add(pnlSalesContent, BorderLayout.CENTER);

            // ==== Panel 2 - Total Transactions ====
            JPanel totaltrans = new JPanel(new BorderLayout());
            totaltrans.setBackground(panelColor);

            JPanel pnlline2 = new JPanel();
            pnlline2.setPreferredSize(new Dimension(5, 125));
            pnlline2.setBackground(textColor);
            totaltrans.add(pnlline2, BorderLayout.WEST);

            JPanel pnlTransContent = new JPanel();
            pnlTransContent.setLayout(new BoxLayout(pnlTransContent, BoxLayout.Y_AXIS));
            pnlTransContent.setBackground(panelColor);
            pnlTransContent.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

            JLabel lblTransTitle = new JLabel("Total Transactions");
            lblTransTitle.setFont(titleFont);
            lblTransTitle.setForeground(textColor);
            lblTransTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblTransValue = new JLabel("28");
            lblTransValue.setFont(valueFont);
            lblTransValue.setForeground(textColor);
            lblTransValue.setAlignmentX(Component.CENTER_ALIGNMENT);

            pnlTransContent.add(lblTransTitle);
            pnlTransContent.add(Box.createVerticalStrut(10));
            pnlTransContent.add(lblTransValue);

            totaltrans.add(pnlTransContent, BorderLayout.CENTER);

            // ==== Panel 3 - Most Bought Item ====
            JPanel itemssold = new JPanel(new BorderLayout());
            itemssold.setBackground(panelColor);

            JPanel pnlline3 = new JPanel();
            pnlline3.setPreferredSize(new Dimension(5, 125));
            pnlline3.setBackground(textColor);
            itemssold.add(pnlline3, BorderLayout.WEST);

            JPanel pnlItemContent = new JPanel();
            pnlItemContent.setLayout(new BoxLayout(pnlItemContent, BoxLayout.Y_AXIS));
            pnlItemContent.setBackground(panelColor);
            pnlItemContent.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

            JLabel lblMostBoughtTitle = new JLabel("Most Bought Item");
            lblMostBoughtTitle.setFont(titleFont);
            lblMostBoughtTitle.setForeground(textColor);
            lblMostBoughtTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblMostBoughtValue = new JLabel("Coffee");
            lblMostBoughtValue.setFont(valueFont);
            lblMostBoughtValue.setForeground(textColor);
            lblMostBoughtValue.setAlignmentX(Component.CENTER_ALIGNMENT);

            pnlItemContent.add(lblMostBoughtTitle);
            pnlItemContent.add(Box.createVerticalStrut(10));
            pnlItemContent.add(lblMostBoughtValue);

            itemssold.add(pnlItemContent, BorderLayout.CENTER);

            // ==== Add overview panels ====
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
            String[] columns = { "Log Name", "Transaction No.", "Date Created", "Last Modified" };
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
                    String filepath = file.getPath();
                    String modified = sdf.format(file.lastModified());

                    String transactionNo = "";
                    String creationDate = "";

                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        // Read the first three lines
                        String logNameLine = reader.readLine(); // LOG NAME,test1
                        String dateLine = reader.readLine();    // DATE,2025-01-01
                        String transLine = reader.readLine();   // TRANSACTION NUMBER,7460

                        if (dateLine != null && dateLine.contains(",")) {
                            String[] parts = dateLine.split(",");
                            if (parts.length > 1) {
                                creationDate = parts[1].trim();
                            }
                        }

                        if (transLine != null && transLine.contains(",")) {
                            String[] parts = transLine.split(",");
                            if (parts.length > 1) {
                                transactionNo = parts[1].trim();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // If file creation time is available, use it for creationDate (optional)
                    try {
                        Path path = file.toPath();
                        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                        FileTime creationTime = attr.creationTime();
                        if (creationDate.isEmpty()) {
                            creationDate = sdf.format(creationTime.toMillis());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Add row to the table
                    tableModel.addRow(new Object[]{filename, transactionNo, creationDate, modified});
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);

            if (e.getSource() == createnew) {
                new createLog();  // Open new log creation window
                if (parentWindow != null) {
                    parentWindow.dispose();
                }

            } else if (e.getSource() == load) {
                int selectedrow = logTable.getSelectedRow();

                if (selectedrow != -1) {
                    String filename = (String) tableModel.getValueAt(selectedrow, 0);
                    String logname = filename.replace(".csv","");
                    String filepath = "logs/" + filename;

                    String date = extractDateFromCSV(filepath);

                    new TransactionFrame(logname, date, filepath).setVisible(true);
                    if (parentWindow != null) {
                        parentWindow.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a log file to load.");

                    }
                }

            } else if (e.getSource() == exit) {
                int selectedrow = logTable.getSelectedRow();

                if (selectedrow != -1) {
                    String filename = (String) tableModel.getValueAt(selectedrow,0);
                    int confrim = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete " + filename + " ?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confrim == JOptionPane.YES_OPTION) {
                        File filetodelete = new File("logs/" + filename);
                        if (filetodelete.exists() && filetodelete.delete()) {
                            tableModel.removeRow(selectedrow);
                            JOptionPane.showMessageDialog(this, "Log deleted successfully");
                        } else {
                            JOptionPane.showMessageDialog(this,"Failed to delete log file.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select log file to delete");
                }
            }

        }

        private String extractDateFromCSV(String filepath) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
                String headerLine = reader.readLine(); // read the header
                String dataLine = reader.readLine();   // read the first data row

                if (dataLine != null) {
                    String[] values = dataLine.split(",");
                    if (values.length >= 2) {
                        return values[1].trim(); // assuming the 2nd column is the date
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ""; // return empty string if date couldn't be extracted
        }
    }



