import javax.swing.*;

import java.awt.*;

public class Menu extends JFrame {

    CardLayout cardLayout;
    JPanel rightpanel;


    public Menu() {
        setTitle("Transaction Logging System");
        setSize(920, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ----------------------------------Left red sidebar (dashboard)----------------------------------------------------
        JPanel dashboard = new JPanel();
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));
        dashboard.setBackground(new Color(201, 42, 42));
        dashboard.setPreferredSize(new Dimension(105, 0));
        add(dashboard, BorderLayout.WEST);

        JButton btnmain = new JButton("MAIN");
        btnmain.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnmain.setMaximumSize(new Dimension(105, 40));
        btnmain.setForeground(new Color(255, 255, 255));
        btnmain.setOpaque(false);
        btnmain.setFocusPainted(false);
        btnmain.setContentAreaFilled(false);
        btnmain.setBorderPainted(false);

        JButton btnInventory = new JButton("INVENTORY");
        btnInventory.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInventory.setMaximumSize(new Dimension(105, 40));
        btnInventory.setForeground(new Color(255, 255, 255));
        btnInventory.setContentAreaFilled(false);
        btnInventory.setFocusPainted(false);
        btnInventory.setOpaque(false);
        btnInventory.setBorderPainted(false);

        dashboard.add(Box.createVerticalStrut(130));
        dashboard.add(btnmain);
        dashboard.add(Box.createVerticalStrut(-5));
        dashboard.add(btnInventory);

        //-------------------------------------Right part (CardLayout)-------------------------------------------------------
        cardLayout = new CardLayout();
        rightpanel = new JPanel(cardLayout);
        add(rightpanel, BorderLayout.CENTER);

        // Add panels to rightpanel
        InventorySystem1 inventoryPanel = new InventorySystem1(this);
        MainPanel mainPanel = new MainPanel();

        mainPanel.setInventorySystem(inventoryPanel);

        rightpanel.add(mainPanel, "MAIN"); //shows mainpanel under the constraint "Menu"
        rightpanel.add(inventoryPanel, "INVENTORY"); //shows inventorypanel under the constraint "Inventory"
        cardLayout.show(rightpanel, "MAIN");  // Show initial panel

        // Button actions
        btnmain.addActionListener(e -> cardLayout.show(rightpanel, "MAIN")); //when btnmain is clicked it should show mainpanel
        btnInventory.addActionListener(e -> cardLayout.show(rightpanel, "INVENTORY")); //when btnInventory is clicked it shows the InventoryPanel

        setVisible(true);

    }


    public static void main(String[] args) {
        new Menu();
    }
}
