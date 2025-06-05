import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class loadlog extends JFrame implements ActionListener {
    private JList<String> logList;
    private DefaultListModel<String> listModel;
    private JButton btnOpen, btnCancel, btnDelete;

    public loadlog() {
        setTitle("Transaction Logging System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();

        // Load all saved CSV filenames from "logs" folder
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdir();
        }

        File[] logFiles = logsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (logFiles != null) {
            for (File f : logFiles) {
                listModel.addElement(f.getName());
            }
        }

        logList = new JList<>(listModel);
        logList.setPreferredSize(new Dimension(365, 300));
        logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(logList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        btnOpen = new JButton("Open");
        btnCancel = new JButton("Cancel");
        btnDelete = new JButton("Delete");
        btnOpen.addActionListener(this);
        btnCancel.addActionListener(this);
        btnDelete.addActionListener(this);

        bottomPanel.add(btnOpen);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnCancel);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOpen) {
            String selectedLog = logList.getSelectedValue();
            if (selectedLog == null) {
                JOptionPane.showMessageDialog(this, "Please select a log to open.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Construct full path to the CSV file inside logs directory
            String filepath = "logs/" + selectedLog;

            // Open inputdata GUI and load selected CSV file
            new inputdata(filepath);

            this.dispose();

        } else if (e.getSource() == btnCancel) {
            new Menu();
            this.dispose();

        } else if (e.getSource() == btnDelete) {
            String selectedLog = logList.getSelectedValue();
            if (selectedLog == null) {
                JOptionPane.showMessageDialog(this, "Please select a log to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this log?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                File fileToDelete = new File("logs/" + selectedLog);
                if (fileToDelete.exists() && fileToDelete.delete()) {
                    listModel.removeElement(selectedLog);
                    JOptionPane.showMessageDialog(this, "Log deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete the log.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


    }

    // For testing this class independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loadlog());
    }
}
