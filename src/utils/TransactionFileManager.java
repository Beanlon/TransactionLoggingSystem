package utils;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {

    // Save method (used from TransactionPanel)
    public static void saveToFile(File file, String name, String date, String transactionNumber, DefaultTableModel model) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("LOG NAME," + name);
            writer.newLine();
            writer.write("DATE," + date);
            writer.newLine();
            writer.write("TRANSACTION NUMBER," + transactionNumber);
            writer.newLine();
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) writer.write(",");
                }
                writer.newLine();
            }
        }
    }

    // Load method returns name, date, and rows
    public static TransactionData loadFromFile(File file) throws IOException {
        TransactionData data = new TransactionData();
        data.rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Read header info (name, date, transaction number)
            data.name = reader.readLine().split(",", 2)[1];
            data.date = reader.readLine().split(",", 2)[1];
            data.transactionNumber = reader.readLine().split(",", 2)[1];

            // Read table data
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.rows.add(row);
            }
        }
        return data;
    }


    // Helper class to return combined data
    public static class TransactionData {
        public String name;
        public String date;
        public String transactionNumber;
        public List<String[]> rows;

        public TransactionData(String name, String date, List<String[]> rows) {
            this.name = name;
            this.date = date;
            this.rows = rows;
        }

        public TransactionData() {

        }
    }
}