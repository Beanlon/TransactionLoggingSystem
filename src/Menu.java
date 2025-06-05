import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {

    JButton createnew, load, exit;
    JPanel Title, pnlbtn;
    JLabel lblTitle;

    Menu() {
        this.setTitle("Transaction Logging System");
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        // Title panel
        Title = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        Title.setBounds(10, 10, 365, 60);  // Adjusted height for title
        lblTitle = new JLabel("WELCOME");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        Title.add(lblTitle);

        // Button panel with vertical BoxLayout
        pnlbtn = new JPanel();
        pnlbtn.setLayout(new BoxLayout(pnlbtn, BoxLayout.Y_AXIS));
        pnlbtn.setBounds(95, 100, 200, 230);  // Wider & taller to fit bigger buttons
        pnlbtn.setBackground(Color.white);

        createnew = new JButton("CREATE NEW");
        load = new JButton("LOAD");
        exit = new JButton("EXIT");

        createnew.setAlignmentX(Component.CENTER_ALIGNMENT);
        load.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make buttons bigger - set preferred and max size
        Dimension btnSize = new Dimension(180, 60);
        createnew.setPreferredSize(btnSize);
        createnew.setMaximumSize(btnSize);

        load.setPreferredSize(btnSize);
        load.setMaximumSize(btnSize);

        exit.setPreferredSize(btnSize);
        exit.setMaximumSize(btnSize);

        // Add buttons with spacing
        pnlbtn.add(Box.createVerticalStrut(20));
        pnlbtn.add(createnew);
        createnew.addActionListener(this);

        pnlbtn.add(Box.createVerticalStrut(20));
        pnlbtn.add(load);
        load.addActionListener(this);

        pnlbtn.add(Box.createVerticalStrut(20));
        pnlbtn.add(exit);
        exit.addActionListener(this);

        pnlbtn.add(Box.createVerticalGlue());

        // Add panels to frame
        this.add(Title);
        this.add(pnlbtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createnew) {
            new createLog();
            this.dispose();
        } else if (e.getSource() == load) {
            new loadlog();
            this.dispose();
        } else if (e.getSource() == exit) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Menu();
    }
}
