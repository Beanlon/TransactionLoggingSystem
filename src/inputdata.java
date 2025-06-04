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
    JButton btnBack, btnTransaction, btnSummary;
    TransactionPanel transactionPanel;  // Keep reference

    // New constructor with parameters
    public inputdata(String name, String date) {
        this();  // Call default constructor to setup UI

        // After UI is set up, pass data to transactionPanel
        transactionPanel.setNameAndDate(name, date);
    }

    // Default constructor (unchanged except make public)
    public inputdata() {
        setTitle("Transaction Logging System");
        setSize(800, 663);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panelLeft.setPreferredSize(new Dimension(200, 0));
        panelLeft.setBackground(new Color(120, 26, 26));
        add(panelLeft, BorderLayout.WEST);

        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        backButtonPanel.setOpaque(false);
        backButtonPanel.setMaximumSize(new Dimension(panelLeft.getPreferredSize().width, 40));
        btnBack = new JButton("← BACK");
        btnBack.setPreferredSize(new Dimension(100, 30));
        backButtonPanel.add(btnBack);
        panelLeft.add(Box.createVerticalStrut(15));
        panelLeft.add(backButtonPanel);

        btnTransaction = new JButton("TRANSACTION");
        btnTransaction.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTransaction.setMaximumSize(new Dimension(160, 40));
        btnTransaction.setPreferredSize(new Dimension(160, 40));

        btnSummary = new JButton("SUMMARY");
        btnSummary.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSummary.setMaximumSize(new Dimension(160, 40));
        btnSummary.setPreferredSize(new Dimension(160, 40));

        panelLeft.add(Box.createVerticalStrut(0));
        panelLeft.add(btnTransaction);
        panelLeft.add(Box.createVerticalStrut(10));
        panelLeft.add(btnSummary);

        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        add(rightPanel, BorderLayout.CENTER);

        transactionPanel = new TransactionPanel();
        SummaryPanel summaryPanel = new SummaryPanel();

        rightPanel.add(transactionPanel, "TRANSACTION");
        rightPanel.add(summaryPanel, "SUMMARY");

        cardLayout.show(rightPanel, "TRANSACTION");

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
