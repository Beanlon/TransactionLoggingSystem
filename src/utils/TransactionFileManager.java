package utils;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {

    // Save method (used from TransactionPanel)
    public static void saveToFile(File file, String name, String date, DefaultTableModel model) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(name + "," + date);
            bw.newLine();

            // Write headers
            for (int c = 0; c < model.getColumnCount(); c++) {
                bw.write(model.getColumnName(c));
                if (c < model.getColumnCount() - 1) bw.write(",");
            }
            bw.newLine();

            // Write rows
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    bw.write(model.getValueAt(r, c).toString());
                    if (c < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }
        }
    }

    // Load method returns name, date, and rows
    public static TransactionData loadFromFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String name = "", date = "";
            List<String[]> rows = new ArrayList<>();

            String line = br.readLine();  // First line: name,date
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    name = parts[0];
                    date = parts[1];
                }
            }

            br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }

            return new TransactionData(name, date, rows);
        }
    }

    // Helper class to return combined data
    public static class TransactionData {
        public String name;
        public String date;
        public List<String[]> rows;

        public TransactionData(String name, String date, List<String[]> rows) {
            this.name = name;
            this.date = date;
            this.rows = rows;
        }
    }
}