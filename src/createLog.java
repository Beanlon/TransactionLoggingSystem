import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class createLog extends JFrame implements ActionListener {
    private JTextField txtName;
    private JComboBox<String> dayCombo, monthCombo, yearCombo;
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
        inputlog.setBounds(20, 130, 400, 100);
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
        txtName.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputlog.add(txtName, gbc);

        // Date input (day, month, year combo boxes)
        JLabel lblDate = new JLabel("DATE:");
        lblDate.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputlog.add(lblDate, gbc);

        JPanel dropdate = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.format("%02d", i);

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        String[] years = new String[11];
        int startYear = 2025;
        for (int i = 0; i < 11; i++)
            years[i] = Integer.toString(startYear + i);

        dayCombo = new JComboBox<>(days);
        monthCombo = new JComboBox<>(months);
        yearCombo = new JComboBox<>(years);
        dropdate.add(dayCombo);
        dropdate.add(monthCombo);
        dropdate.add(yearCombo);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputlog.add(dropdate, gbc);

        add(inputlog);

        // Create log button
        btnCreate = new JButton("CREATE LOG");
        btnCreate.setBackground(new Color(201, 42, 42));
        btnCreate.setFont(new Font("Arial", Font.BOLD, 17));
        btnCreate.setBorderPainted(false);
        btnCreate.setForeground(Color.white);
        btnCreate.setBounds(117, 240, 220, 40);
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
                String day = (String) dayCombo.getSelectedItem();
                String month = (String) monthCombo.getSelectedItem();
                String year = (String) yearCombo.getSelectedItem();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a name.");
                    return;
                }

                String date = year + "-" + month + "-" + day;

                // Show message
                JOptionPane.showMessageDialog(this, "Log created for " + name + " on " + date);

                // ✅ Corrected: Use TransactionFrame, not inputdata
                TransactionFrame transactionFrame = new TransactionFrame(name, date);
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
