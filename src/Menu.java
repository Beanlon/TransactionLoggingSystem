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
        Title.setBounds(10, 10, 365, 365);
        lblTitle = new JLabel("WELCOME");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        Title.add(lblTitle);

        // Panel for buttons with FlowLayout
        pnlbtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlbtn.setBounds(0, 60, 400, 200);
        this.add(pnlbtn);

        // Button
        createnew = new JButton("CREATE NEW");
        createnew.setPreferredSize(new Dimension(130, 40));
        createnew.addActionListener(this);
        pnlbtn.add(createnew);

        // Add panels to frame
        this.add(Title);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createnew) {
            new createLog();
            this.dispose();
        }
    }

}
