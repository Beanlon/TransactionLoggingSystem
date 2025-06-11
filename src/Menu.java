import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    CardLayout cardLayout;
    JPanel rightpanel;

    public Menu() {
        setTitle("Transaction Logging System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Left red sidebar (dashboard)
        JPanel dashboard = new JPanel();
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));
        dashboard.setBackground(new Color(201, 42, 42));
        dashboard.setPreferredSize(new Dimension(200, 0));
        add(dashboard, BorderLayout.WEST);

        JButton btnmain = new JButton("MAIN");
        btnmain.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnmain.setMaximumSize(new Dimension(160, 40));

        JButton btnInventory = new JButton("INVENTORY");
        btnInventory.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInventory.setMaximumSize(new Dimension(160, 40));

        dashboard.add(Box.createVerticalStrut(100));
        dashboard.add(btnmain);
        dashboard.add(Box.createVerticalStrut(10));
        dashboard.add(btnInventory);

        // Right side with CardLayout
        cardLayout = new CardLayout();
        rightpanel = new JPanel(cardLayout);
        add(rightpanel, BorderLayout.CENTER);

        // Add panels to rightpanel
        JPanel mainPanel = new MainPanel();
        JPanel inventoryPanel = new InventorySystem();  // Make sure this class exists
        rightpanel.add(mainPanel, "MAIN");
        rightpanel.add(inventoryPanel, "INVENTORY");
        cardLayout.show(rightpanel, "MAIN");  // Show initial panel

        // Button actions
        btnmain.addActionListener(e -> cardLayout.show(rightpanel, "MAIN"));
        btnInventory.addActionListener(e -> cardLayout.show(rightpanel, "INVENTORY"));

        setVisible(true);
    }

    public static void main(String[] args) {
        new Menu();
    }
}
