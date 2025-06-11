import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {
    public SummaryPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(new JLabel("Summary Page Content Here", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
