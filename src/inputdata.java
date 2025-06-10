import panels.SummaryPanel;
import panels.TransactionPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class inputdata extends JFrame implements ActionListener {
    CardLayout cardLayout;
    JPanel rightPanel;
    JButton btnBack, btnTransaction, btnSummary;
    TransactionPanel transactionPanel;

    public inputdata(String name, String date) {
        this();
        transactionPanel.setNameAndDate(name, date);
    }

    // New constructor to load from saved file
    public inputdata(String filename) {
        this();
        transactionPanel.loadFromFile(filename);
    }

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
        panelLeft.setBackground(new Color(201, 42, 42));
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
            if (transactionPanel != null && transactionPanel.isSaved()) {
                new Menu();
                this.dispose();
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        transactionPanel.saveToFile(); // ✅ Save
                        new Menu();
                        this.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Error saving file: " + ex.getMessage(),
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                    new Menu();
                    this.dispose();
                } else if (choice == JOptionPane.NO_OPTION) {
                    new Menu();
                    this.dispose();
                } else if (choice == JOptionPane.CANCEL_OPTION) {

                }
            }
        }
    }
}