import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
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
        setSize(447, 400);  // Recommended size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        // ===== Top Panel with Back Button =====
        JPanel topPanel = new JPanel(null);
        topPanel.setBounds(10, 10, 400, 50);
        btnBack = new JButton("← BACK");
        btnBack.setBounds(10, 10, 100, 30);  // Top-left corner
        btnBack.addActionListener(this);
        topPanel.add(btnBack);
        add(topPanel);

        // ===== Title =====
        JLabel lblTitle = new JLabel("CREATE LOG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setBounds(20, 70, 400, 40);
        add(lblTitle);

        // ===== Input Log Panel =====
        JPanel inputlog = new JPanel(new GridBagLayout());
        inputlog.setBounds(20, 130, 400, 100);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Increased padding

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

        JLabel lblDate = new JLabel("DATE:");
        lblDate.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputlog.add(lblDate, gbc);

        // ===== Date Dropdowns =====
        JPanel dropdate = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.format("%02d", i);
        String[] months = {
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"
        };
        String[] years = new String[11];
        int startYear = 2025;
        for (int i = 0; i < 11; i++)
            years[i] = Integer.toString(startYear + i);

        dayCombo = new JComboBox<>(days);
        dayCombo.setBackground(Color.WHITE);
        dayCombo.setForeground(Color.BLACK);
        dropdate.add(dayCombo);

        monthCombo = new JComboBox<>(months);
        monthCombo.setBackground(Color.WHITE);
        monthCombo.setForeground(Color.BLACK);
        dropdate.add(monthCombo);

        yearCombo = new JComboBox<>(years);
        yearCombo.setBackground(Color.WHITE);
        yearCombo.setForeground(Color.BLACK);
        dropdate.add(yearCombo);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputlog.add(dropdate, gbc);

        add(inputlog);

        // ===== Create Button =====
        btnCreate = new JButton("CREATE LOG");
        btnCreate.setBackground(new Color(201, 42, 42));
        btnCreate.setFont(new Font("Arial",Font.BOLD,17));
        btnCreate.setBorderPainted(false);
        btnCreate.setForeground(Color.white);
        btnCreate.setBounds(117, 240, 220, 40);
        btnCreate.addActionListener(this);

        add(btnCreate);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnBack) {
            new Menu();
            this.dispose();
        } else if (source == btnCreate) {
            String name = txtName.getText().trim();
            String day = (String) dayCombo.getSelectedItem();
            String month = (String) monthCombo.getSelectedItem();
            String year = (String) yearCombo.getSelectedItem();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name.");
                return;
            }
            String date = year + "-" + month + "-" + day;
            JOptionPane.showMessageDialog(this, "Log created for " + name + " on " + date);
            // Open inputdata and pass name/date
            new inputdata(name, date);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new createLog();
    }
}