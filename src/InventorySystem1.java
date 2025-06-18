import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InventorySystem1 extends JPanel {

    InventorySystem1() {
        setLayout(null);
        setPreferredSize(new Dimension(900, 520));

        RoundedPanel panel1 = new RoundedPanel(30);
        panel1.setBounds(40, 50, 340, 180);
        panel1.setBackground(new Color(255, 255, 255, 255));
        panel1.setLayout(null);
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventorySystem1.this);
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                InventorySystem inventoryFrame = new InventorySystem();
                inventoryFrame.setTitle("Inventory System");
                inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // or EXIT_ON_CLOSE if needed
                inventoryFrame.setVisible(true);

                new InventorySystem();
            }
        });
        add(panel1);

        RoundedPanel panel2 = new RoundedPanel(30);
        panel2.setBounds(415, 50, 340, 180);
        panel2.setBackground(new Color(255, 255, 255, 255));
        panel2.setLayout(null);
        add(panel2);

        JPanel paneltable = new JPanel();
        paneltable.setBounds(40, 300, 717, 250);
        paneltable.setBackground(Color.white);
        paneltable.setLayout(null);
        add(paneltable);
    }
}
