import utils.NoSpaceKeyListener;
import utils.UserAuth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame implements ActionListener {

    JPanel Title, Register, btnPanel;
    JLabel lbltitle, username, createpassword, enterpassword;
    JTextField txtusername;
    JPasswordField txtpassword1, txtpassword2;
    JButton btnreg, back;

    // Declare UserAuth instance
    UserAuth auth;

    Register() {
        this.setTitle("Transaction Logging System");
        this.setSize(400, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        // Initialize UserAuth with filename
        auth = new UserAuth("users.txt");

        Title = new JPanel(new FlowLayout());
        Title.setBounds(10, 34, 365, 80);
        lbltitle = new JLabel("REGISTER", SwingConstants.CENTER);
        lbltitle.setFont(new Font("Arial", Font.BOLD, 40));
        Title.add(lbltitle);

        Register = new JPanel(new GridBagLayout());
        Register.setBounds(10, 100, 365, 200);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        username = new JLabel("Create Username:");
        txtusername = new JTextField();
        txtusername.addKeyListener(new NoSpaceKeyListener());
        txtusername.setPreferredSize(new Dimension(210, 30));

        createpassword = new JLabel("Create Password:");
        txtpassword1 = new JPasswordField();
        txtpassword1.addKeyListener(new NoSpaceKeyListener());
        txtpassword1.setPreferredSize(new Dimension(210, 30));

        enterpassword = new JLabel("Enter Password:");
        txtpassword2 = new JPasswordField();
        txtpassword2.addKeyListener(new NoSpaceKeyListener());
        txtpassword2.setPreferredSize(new Dimension(210, 30));

        btnreg = new JButton("REGISTER");
        btnreg.setPreferredSize(new Dimension(130, 30));
        btnreg.setBackground(new Color(201, 42, 42));
        btnreg.setForeground(Color.white);
        btnreg.addActionListener(this);

        back = new JButton("BACK");
        back.setPreferredSize(new Dimension(130, 30));
        back.setBackground(new Color(201, 42, 42));
        back.setForeground(Color.white);
        back.addActionListener(this);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
        Register.add(username, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        Register.add(txtusername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
        Register.add(createpassword, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        Register.add(txtpassword1, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        Register.add(enterpassword, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        Register.add(txtpassword2, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        Register.add(back, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        Register.add(btnreg, gbc);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBounds(10, 310, 365, 35);
        btnPanel.add(back);
        btnPanel.add(btnreg);

        this.add(Title);
        this.add(Register);
        this.add(btnPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            new Login(); // it goes back to the login GUI
            this.dispose(); //disposes the current frame displayed
        }

        if (e.getSource() == btnreg) {
            String user = txtusername.getText().trim(); // creates the string user which gets the username entered
            String pass1 = txtpassword1.getText().trim(); // creates the string password that gets the unrevealed password
            String pass2 = new String(txtpassword2.getPassword()).trim(); // creates the string pass2 which is gets from the passwordfield

            if (user.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) { // if condition that checks if either of the string are empty then it shows a prompt
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pass1.equals(pass2)) { //if the pass1(unrevealed password) is not equal to pass2(hidden password) then it shows the prompt
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (auth.register(user, pass1)) { //if conditionn that uses the register function from UserAuth and getting the required parameters which is user and pass1
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Login(); // opens the login page after the registrationis successful
                this.dispose();
            } else { //shows this prompt when the user already exists
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
