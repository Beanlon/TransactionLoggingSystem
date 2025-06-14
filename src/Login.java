import utils.UserAuth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login extends JFrame implements ActionListener {

    JLabel lblusername, lblpassword, lblTitle;
    JTextField txtusername ;
    JPasswordField txtpassword;
    JPanel leftpanel, Loginpage, Title, btnpanel;
    JButton loginbtn, Regbtn;
    JCheckBox showpass;

    Login() {
        // Use 'this' JFrame methods:
        this.setTitle("Transaction Logging System");
        this.setSize(700, 420);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        ImageIcon logo = new ImageIcon("Images/Logo.png");
        Image image = logo.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imagelabel = new JLabel(new ImageIcon(image));
        imagelabel.setBounds(394,37, 300, 300);
        add(imagelabel);



        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(400, 0, 300, 420);


        leftpanel = new JPanel();
        leftpanel.setLayout(null);
        leftpanel.setBounds(0,0,400,420);
        leftpanel.setBackground(new Color(201, 42, 42));
        this.add(leftpanel);

        JPanel Title = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
        Title.setBounds(10, 28, 365, 97); // x, y, width, height
        Title.setBackground(new Color(201, 42, 42));

        JLabel line1 = new JLabel("Login to your", SwingConstants.CENTER);
        JLabel line2 = new JLabel("Account", SwingConstants.CENTER);
        line1.setFont(new Font("Arial", Font.BOLD, 40));
        line2.setFont(new Font("Arial", Font.BOLD, 40));
        line1.setForeground(Color.white);
        line2.setForeground(Color.white);

        Title.add(line1);
        Title.add(line2);

        leftpanel.add(Title);

        Loginpage = new JPanel(new GridBagLayout());
        Loginpage.setBounds(17, 135, 365, 120);
        Loginpage.setBackground(new Color(201, 42, 42));
        leftpanel.add(Loginpage);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 15, 6, 15);

        lblusername = new JLabel("USERNAME: ");
        lblusername.setForeground(Color.white);
        txtusername = new JTextField();
        txtusername.setPreferredSize(new Dimension(210, 30));
        txtusername.setBackground(Color.white);

        lblpassword = new JLabel("PASSWORD: ");
        lblpassword.setForeground(Color.white);
        txtpassword = new JPasswordField();
        txtpassword.setPreferredSize(new Dimension(210, 30));
        txtpassword.setBackground(Color.white);

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
        showpass.setForeground(Color.white);
        showpass.setBackground(new Color(201, 42, 42));
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


        btnpanel = new JPanel();
        btnpanel.setLayout(new GridLayout(0,2,10,5));
        btnpanel.setBounds(33, 267, 326, 40); // Adjust position and size to center and fit buttons
        btnpanel.setBackground(new Color(201, 42, 42));

        loginbtn = new JButton("LOG IN");
        loginbtn.setBackground(Color.white);
        loginbtn.setPreferredSize(new Dimension(60,40));
        loginbtn.setFont(new Font("Arial", Font.BOLD,16));
        loginbtn.setFocusPainted(false);
        loginbtn.setFocusPainted(false);
        loginbtn.setForeground(new Color(201, 42, 42));
        loginbtn.addActionListener(this);

        Regbtn = new JButton("REGISTER");
        Regbtn.setBackground(Color.white);
        Regbtn.setPreferredSize(new Dimension(60,40));
        Regbtn.setFont(new Font("Arial", Font.BOLD,16));
        Regbtn.setFocusPainted(false);
        Regbtn.setFocusPainted(false);
        Regbtn.setForeground(new Color(201, 42, 42));
        Regbtn.addActionListener(this);

        btnpanel.add(loginbtn);
        btnpanel.add(Regbtn);
        leftpanel.add(btnpanel);

        this.add(leftpanel);
        this.add(rightPanel);

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

