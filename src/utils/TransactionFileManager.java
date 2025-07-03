package utils;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {

    public static class TransactionData {
        public String name;
        public String date;
        public String transactionID;
        public List<String[]> rows;

        public TransactionData(String name, String date, String transactionID, List<String[]> rows) {
            this.name = name;
            this.date = date;
            this.transactionID = transactionID;
            this.rows = rows;
        }
    }

    public static void saveToFile(File file, String name, String date, String transactionID, DefaultTableModel model) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("LogName:" + name);
            writer.newLine();
            writer.write("Date:" + date);
            writer.newLine();
            writer.write("TransactionID:" + transactionID);
            writer.newLine();
            writer.write("---DATA---"); // Separator for actual table data
            writer.newLine();

            // Write column headers (optional, but good for readability of the CSV)
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Write table data
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(String.valueOf(model.getValueAt(i, j)));
                    if (j < model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        }
    }

    public static TransactionData loadFromFile(File file) throws IOException {
        String name = "";
        String date = "";
        String transactionID = "";
        List<String[]> rows = new ArrayList<>();
        boolean readingData = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                if (line.startsWith("LogName:")) {
                    name = line.substring("LogName:".length());
                } else if (line.startsWith("Date:")) {
                    date = line.substring("Date:".length());
                } else if (line.startsWith("TransactionID:")) {
                    transactionID = line.substring("TransactionID:".length());
                } else if (line.equals("---DATA---")) {
                    readingData = true;
                    reader.readLine(); // Skip header row of the table after "---DATA---"
                } else if (readingData) {
                    rows.add(line.split(","));
                }
            }
        }
        return new TransactionData(name, date, transactionID, rows);
    }
}