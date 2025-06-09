import utils.UserAuth;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    JLabel lblusername, lblpassword, lblTitle;
    JTextField txtusername ;
    JPasswordField txtpassword;
    JPanel leftpanel, rightpanel, Loginpage, Title, btnpanel;
    JButton loginbtn, Regbtn;
    JCheckBox showpass;

    Login() {
        // Use 'this' JFrame methods:
        this.setTitle("Transaction Logging System");
        this.setSize(700, 420);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        rightpanel = new JPanel();
        rightpanel.setBounds(400, 0, 300, 420);


        leftpanel = new JPanel();
        leftpanel.setLayout(null);
        leftpanel.setBounds(0,0,400,420);
        leftpanel.setBackground(Color.WHITE);
        this.add(leftpanel);

        JPanel Title = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
        Title.setBounds(10, 28, 365, 97); // x, y, width, height
        Title.setBackground(Color.WHITE);

        JLabel line1 = new JLabel("Login to your", SwingConstants.CENTER);
        JLabel line2 = new JLabel("account", SwingConstants.CENTER);
        line1.setFont(new Font("Arial", Font.BOLD, 40));
        line2.setFont(new Font("Arial", Font.BOLD, 40));

        Title.add(line1);
        Title.add(line2);

        leftpanel.add(Title);

        Loginpage = new JPanel(new GridBagLayout());
        Loginpage.setBounds(10, 135, 365, 120);
        Loginpage.setBackground(Color.WHITE);
        leftpanel.add(Loginpage);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 15, 6, 15);

        lblusername = new JLabel("USERNAME: ");
        txtusername = new JTextField();
        txtusername.setPreferredSize(new Dimension(210, 30));
        txtusername.setBackground(new Color(231, 231, 231));

        lblpassword = new JLabel("PASSWORD: ");
        txtpassword = new JPasswordField();
        txtpassword.setPreferredSize(new Dimension(210, 30));
        txtpassword.setBackground(new Color(231, 231, 231));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        Loginpage.add(lblusername, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        Loginpage.add(txtusername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        Loginpage.add(lblpassword, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        Loginpage.add(txtpassword, gbc);

        showpass = new JCheckBox("SHOW PASSWORD");
        showpass.setBackground(Color.white);
        showpass.setPreferredSize(new Dimension(170,15));
        showpass.setFocusPainted(false);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        Loginpage.add(showpass, gbc);

        char defaultEchoChar = txtpassword.getEchoChar();

        showpass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showpass.isSelected()) {
                    txtpassword.setEchoChar((char) 0);
                } else {
                    txtpassword.setEchoChar(defaultEchoChar);
                }
            }
        });



        Dimension btnsize = new Dimension(150,40);

        loginbtn = new JButton("LOG IN");
        loginbtn.setBackground(new Color(120, 26, 26));
        loginbtn.setPreferredSize(new Dimension(170,40));
        loginbtn.setFont(new Font("Arial", Font.BOLD,16));
        loginbtn.setFocusPainted(false);
        loginbtn.setFocusPainted(false);
        loginbtn.setForeground(Color.white);
        loginbtn.addActionListener(this);

        Regbtn = new JButton("REGISTER");
        Regbtn.setPreferredSize(btnsize);
        Regbtn.setContentAreaFilled(false);
        Regbtn.setFocusPainted(false);
        Regbtn.setOpaque(false);
        Regbtn.setBorderPainted(false);
        Regbtn.setForeground(Color.blue);
        Regbtn.addActionListener(this);

        btnpanel = new JPanel();
        btnpanel.setLayout(new GridLayout(2,0));
        btnpanel.setBounds(65, 265, 265, 90); // Adjust position and size to center and fit buttons
        btnpanel.setBackground(Color.WHITE);

        loginbtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        Regbtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnpanel.add(loginbtn);
        btnpanel.add(Regbtn);
        leftpanel.add(btnpanel);

        this.add(leftpanel);
        this.add(rightpanel);

        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == Regbtn) {
            // Open your main entry page here
            new Register();
            this.dispose(); // Optionally close login window
        } else if (e.getSource() == loginbtn) {
            String username = txtusername.getText();
            String password = txtpassword.getText();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            UserAuth auth = new UserAuth("users.txt");

            if (auth.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Menu(); // Open main page
                this.dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

