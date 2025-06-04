import javax.swing.*;
import javax.swing.border.BevelBorder;
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
        setSize(500, 400);  // Recommended size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        // ===== Top Panel with Back Button =====
        JPanel topPanel = new JPanel(null);
        topPanel.setBounds(10, 10, 665, 50);

        btnBack = new JButton("← BACK");
        btnBack.setBounds(10, 10, 80, 30);  // Top-left corner
        btnBack.addActionListener(this);
        topPanel.add(btnBack);

        add(topPanel);

        // ===== Title =====
        JLabel lblTitle = new JLabel("CREATE LOG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setBounds(10, 70, 465, 40);
        add(lblTitle);

        // ===== Name Field =====
        JLabel lblName = new JLabel("ENTER NAME:", SwingConstants.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setBounds(50, 140, 120, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(180, 140, 250, 30);
        add(txtName);

        // ===== Date Dropdowns =====
        JLabel lblDate = new JLabel("DATE:", SwingConstants.CENTER);
        lblDate.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDate.setBounds(50, 190, 120, 25);
        add(lblDate);

        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.format("%02d", i);

        String[] months = {
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12"
        };

        String[] years = new String[76];
        int startYear = 1950;
        for (int i = 0; i < 76; i++)
            years[i] = Integer.toString(startYear + i);

        dayCombo = new JComboBox<>(days);
        dayCombo.setBounds(180, 190, 60, 30);
        add(dayCombo);

        monthCombo = new JComboBox<>(months);
        monthCombo.setBounds(250, 190, 60, 30);
        add(monthCombo);

        yearCombo = new JComboBox<>(years);
        yearCombo.setBounds(320, 190, 80, 30);
        add(yearCombo);

        // ===== Create Button =====
        btnCreate = new JButton("Create Log");
        btnCreate.setBounds(180, 250, 150, 35);
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
        }
    }


}
