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
    JButton btnadd, btnedit, btnremove, btnclear, btnsave;

    inputdata() {
        setTitle("Transaction Logging System");
        setSize(900, 663);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Left Panel setup
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setPreferredSize(new Dimension(200, 0));
        panelLeft.setBackground(Color.LIGHT_GRAY);
        add(panelLeft, BorderLayout.WEST);

// Create buttons with fixed preferred size
        JButton btnTransaction = new JButton("Transaction");
        btnTransaction.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTransaction.setMaximumSize(new Dimension(160, 40)); // width, height
        btnTransaction.setPreferredSize(new Dimension(160, 40));

        JButton btnSummary = new JButton("Summary");
        btnSummary.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSummary.setMaximumSize(new Dimension(160, 40));
        btnSummary.setPreferredSize(new Dimension(160, 40));

// Add spacing and buttons to panel
        panelLeft.add(Box.createVerticalStrut(30));
        panelLeft.add(btnTransaction);
        panelLeft.add(Box.createVerticalStrut(10));
        panelLeft.add(btnSummary);

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

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If needed, you can interact with buttons from the TransactionPanel
    }

}

