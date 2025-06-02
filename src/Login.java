import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    JLabel lblusername, lblpassword, lblTitle;
    JTextField txtusername ;
    JPasswordField txtpassword;
    JPanel Loginpage, Title, btnpanel;
    JButton loginbtn, Regbtn;

    Login() {
        // Use 'this' JFrame methods:
        this.setTitle("Transaction Logging System");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        Title = new JPanel(new BorderLayout());
        Title.setBounds(10, 28, 365, 80);
        lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 70));

        Title.add(lblTitle, BorderLayout.CENTER);

        Loginpage = new JPanel(new GridBagLayout());
        Loginpage.setBounds(10, 120, 365, 120);
        this.add(Loginpage);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 15, 3, 15);

        lblusername = new JLabel("Username: ");
        txtusername = new JTextField();
        txtusername.setPreferredSize(new Dimension(210, 30));

        lblpassword = new JLabel("Password: ");
        txtpassword = new JPasswordField();
        txtpassword.setPreferredSize(new Dimension(210, 30));

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

        loginbtn = new JButton("Log in");
        loginbtn.setPreferredSize(new Dimension(130, 30));
        loginbtn.addActionListener(this);

        Regbtn = new JButton("REGISTER");
        Regbtn.setPreferredSize(new Dimension(130, 30));
        Regbtn.addActionListener(this);

        btnpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnpanel.setBounds(10, 250, 365, 35);
        btnpanel.add(loginbtn);
        btnpanel.add(Regbtn);

        this.add(Title);
        this.add(btnpanel);

        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String validusername = "Jeric";
        String validpassword = "0408";
        if (e.getSource() == Regbtn) {
            // Open your main entry page here
            new Register();
            this.dispose(); // Optionally close login window
        } else if (e.getSource() == loginbtn) {
            String username = txtusername.getText();
            String password = txtpassword.getText();

            if (username.equals(validusername) && password.equals(validpassword)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new MAINENTRY(); // Open main page
                this.dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

