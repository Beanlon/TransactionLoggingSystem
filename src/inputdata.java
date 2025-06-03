import panels.SummaryPanel;
import panels.TransactionPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class inputdata extends JFrame implements ActionListener {
    CardLayout cardLayout;
    JPanel rightPanel;
    JButton btnBack, btnTransaction, btnSummary ;

    inputdata() {
        setTitle("Transaction Logging System");
        setSize(800, 663);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Left Panel setup
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelLeft.setPreferredSize(new Dimension(200, 0)); // Width is 200, height adapts
        panelLeft.setBackground(Color.LIGHT_GRAY);
        add(panelLeft, BorderLayout.WEST);

        // --- FIX START ---

        // Consistent alignment for all buttons in the BoxLayout
        // The most robust way to left-align items in a Y_AXIS BoxLayout is to set
        // their X_ALIGNMENT to LEFT_ALIGNMENT.
        // Also, for buttons, set their preferred size and a reasonable maximum size
        // so they don't stretch excessively but can still respect the alignment.

        // Use a wrapper panel for the back button to control its alignment more precisely
        // without affecting the overall BoxLayout behavior of panelLeft as much.
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0)); // Align left within its own panel
        backButtonPanel.setOpaque(false); // Make it transparent so panelLeft's background shows through
        backButtonPanel.setMaximumSize(new Dimension(panelLeft.getPreferredSize().width, 40)); // Constrain its height

        btnBack = new JButton("← BACK");
        btnBack.setPreferredSize(new Dimension(100, 30)); // Give it a fixed preferred size
        backButtonPanel.add(btnBack);

        panelLeft.add(Box.createVerticalStrut(15)); // Top padding
        panelLeft.add(backButtonPanel); // Add the wrapper panel

        // For "Transaction" and "Summary" buttons, we'll keep them centered.
        // To make them align correctly with a centered layout within a BoxLayout.Y_AXIS,
        // it's best to wrap them in a panel that itself has CENTER_ALIGNMENT
        // or ensure their own maximum size doesn't prevent centering.
        // Since you want the 'Back' button left and others centered, a mix of approaches is needed.

        // To reliably center elements in a BoxLayout.Y_AXIS, they should have
        // Component.CENTER_ALIGNMENT and their maximum width should be limited
        // so they don't fill the entire width of the parent panel.
        // Your current maximumSize and preferredSize help with this for Transaction/Summary.

        btnTransaction = new JButton("Transaction");
        btnTransaction.setAlignmentX(Component.CENTER_ALIGNMENT); // Keep centered
        btnTransaction.setMaximumSize(new Dimension(160, 40));
        btnTransaction.setPreferredSize(new Dimension(160, 40));

        btnSummary = new JButton("Summary");
        btnSummary.setAlignmentX(Component.CENTER_ALIGNMENT); // Keep centered
        btnSummary.setMaximumSize(new Dimension(160, 40));
        btnSummary.setPreferredSize(new Dimension(160, 40));

        panelLeft.add(Box.createVerticalStrut(0));
        panelLeft.add(btnTransaction);
        panelLeft.add(Box.createVerticalStrut(10));
        panelLeft.add(btnSummary);

        // --- FIX END ---


        // Right Panel with CardLayout
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        add(rightPanel, BorderLayout.CENTER);

        // Create and add panels using constructors
        TransactionPanel transactionPanel = new TransactionPanel();
        SummaryPanel summaryPanel = new SummaryPanel();

        rightPanel.add(transactionPanel, "TRANSACTION");
        rightPanel.add(summaryPanel, "SUMMARY");

        // Show default panel
        cardLayout.show(rightPanel, "TRANSACTION");

        // Button Actions
        btnTransaction.addActionListener(e -> cardLayout.show(rightPanel, "TRANSACTION"));
        btnSummary.addActionListener(e -> cardLayout.show(rightPanel, "SUMMARY"));
        btnBack.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            new Menu();
            this.dispose();

        }
    }
}