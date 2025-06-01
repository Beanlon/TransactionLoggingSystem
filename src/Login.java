import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Login extends JFrame {

    JLabel lblusername, lblpassword, lblTitle;
    JTextField txtusername, txtpassword;
    JPanel Loginpage,Title;


    Login() {
        setTitle("Transaction Logging System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        Title = new JPanel(new FlowLayout());
        Title.setBounds(10,10,365,80);
        lblTitle = new JLabel("Login",SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD,50));
        Title.add(lblTitle);


        Loginpage = new JPanel(new GridBagLayout());
        Loginpage.setBounds(10,120,365,160);
        Loginpage.setBorder(BorderFactory.createEtchedBorder());
        add(Loginpage);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets= new Insets(5,15,5,15);

        lblusername = new JLabel("Username: ");
        txtusername = new JTextField();
        txtusername.setPreferredSize(new Dimension(210,30));

        lblpassword = new JLabel("Password: ");
        txtpassword = new JTextField();
        txtpassword.setPreferredSize(new Dimension(210,30));

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        Loginpage.add(lblusername, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        Loginpage.add(txtusername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        Loginpage.add(lblpassword, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        Loginpage.add(txtpassword, gbc);

        add(Title);
        add(Loginpage);
        setVisible(true);

    }

    public static void main(String[] args) {
        new Login();
    }

}
