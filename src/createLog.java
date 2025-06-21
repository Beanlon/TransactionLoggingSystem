    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.File;
    import java.time.LocalDate;

    public class createLog extends JFrame implements ActionListener {
        private JTextField txtName;
        private JTextField txtDate;
        private JButton btnBack, btnCreate;

        public createLog() {
            setTitle("Create Log");
            setLayout(null);
            setSize(447, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Top panel with back button
            JPanel topPanel = new JPanel(null);
            topPanel.setBounds(10, 10, 400, 50);
            btnBack = new JButton("← BACK");
            btnBack.setBounds(10, 10, 100, 30);
            btnBack.addActionListener(this);
            topPanel.add(btnBack);
            add(topPanel);

            // Title label
            JLabel lblTitle = new JLabel("CREATE LOG", SwingConstants.CENTER);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
            lblTitle.setBounds(20, 70, 400, 40);
            add(lblTitle);

            // Input panel
            JPanel inputlog = new JPanel(new GridBagLayout());
            inputlog.setBounds(20, 130, 400, 120);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);

            // Log name input
            JLabel lblName = new JLabel("LOG NAME:");
            lblName.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            inputlog.add(lblName, gbc);

            txtName = new JTextField();
            txtName.setBackground(Color.white);
            txtName.setPreferredSize(new Dimension(200, 30));
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            inputlog.add(txtName, gbc);

            // Auto-filled current date in a disabled text field
            JLabel lblDate = new JLabel("DATE:");
            lblDate.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            inputlog.add(lblDate, gbc);

            txtDate = new JTextField(LocalDate.now().toString());
            txtDate.setPreferredSize(new Dimension(200, 30));
            txtDate.setBackground(Color.white);
            txtDate.setEditable(false);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            inputlog.add(txtDate, gbc);

            add(inputlog);

            // Create log button
            btnCreate = new JButton("CREATE LOG");
            btnCreate.setBackground(new Color(201, 42, 42));
            btnCreate.setFont(new Font("Arial", Font.BOLD, 17));
            btnCreate.setBorderPainted(false);
            btnCreate.setForeground(Color.white);
            btnCreate.setBounds(117, 270, 220, 40);
            btnCreate.addActionListener(this);
            add(btnCreate);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == btnBack) {
                    new Menu(); // Replace with your actual menu class
                    this.dispose();
                } else if (e.getSource() == btnCreate) {
                    String name = txtName.getText().trim();
                    String date = txtDate.getText().trim();

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter a name.");
                        return;
                    }

                    // Ensure logs folder exists
                    File logDir = new File("logs");
                    if (!logDir.exists()) {
                        logDir.mkdirs();
                    }

                    // Prepare unique file name
                    String fileName = name;
                    File file = new File(logDir, fileName + ".csv");
                    int counter = 1;
                    while (file.exists()) {
                        fileName = name + "(" + counter + ")";
                        file = new File(logDir, fileName + ".csv");
                        counter++;
                    }

                    // Show message using original name
                    JOptionPane.showMessageDialog(this, "Log created for " + name + " on " + date);

                    // Open transaction frame using actual unique file name
                    TransactionFrame transactionFrame = new TransactionFrame(fileName, date);
                    transactionFrame.setVisible(true);

                    this.dispose();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(createLog::new);
        }
    }
