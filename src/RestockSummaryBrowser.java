import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RestockSummaryBrowser extends JFrame {
    public RestockSummaryBrowser() {
        setTitle("Restock Summaries");
        setSize(800, 600); // Make the frame bigger
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // We'll use absolute positioning for the two panels

        File dir = new File("restockSummaries");
        if (!dir.exists()) dir.mkdirs();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);

        // List txt files
        File[] txtFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
        if (txtFiles != null) {
            for (File f : txtFiles) listModel.addElement(f.getName());
        }

        JTextArea fileContent = new JTextArea();
        fileContent.setEditable(false);
        JScrollPane fileListScroll = new JScrollPane(fileList);
        JScrollPane contentScroll = new JScrollPane(fileContent);

        // Position and size for file list
        fileListScroll.setBounds(20, 20, 250, 520);

        // Position and size for file content area
        contentScroll.setBounds(290, 20, 480, 520);

        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = fileList.getSelectedValue();
                if (selected != null) {
                    File f = new File(dir, selected);
                    try {
                        fileContent.setText(new String(Files.readAllBytes(f.toPath())));
                    } catch (IOException ex) {
                        fileContent.setText("Failed to read file: " + ex.getMessage());
                    }
                }
            }
        });

        add(fileListScroll);
        add(contentScroll);

        setVisible(true);
    }
}