package utils;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFileManager {

    // Save method (used from TransactionPanel)
    public static void saveToFile(File file, String name, String date, String transactionID, DefaultTableModel model) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("LOG NAME," + name);
            writer.newLine();
            writer.write("DATE," + date);
            writer.newLine();
            writer.write("TRANSACTION ID," + transactionID);
            writer.newLine();
            for (int i = 0; i < model.getRowCount(); i++) { // Iterate through each row
                for (int j = 0; j < model.getColumnCount(); j++) { // Iterate through each cell in the row
                    writer.write(model.getValueAt(i, j).toString()); // Write each cell value
                    if (j < model.getColumnCount() - 1) writer.write(","); // Add comma between columns
                }
                writer.newLine();
            }
        }
    }

    // Load method returns name, date, and rows
    public static TransactionData loadFromFile(File file) throws IOException {
        TransactionData data = new TransactionData();
        data.rows = new ArrayList<>(); // Initialize rows list to hold table data

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Read header info (name, date, transaction number)
            data.name = reader.readLine().split(",", 2)[1]; // Split by comma and take the second part
            data.date = reader.readLine().split(",", 2)[1]; // Read the second line and split by comma
            data.transactionID = reader.readLine().split(",", 2)[1]; // Read the thirdline and split by comma

            // Read table data
            while ((line = reader.readLine()) != null) { // Read each line until the end of the file
                String[] row = line.split(","); // Split the line by comma to get each cell value
                data.rows.add(row);
            }
        }
        return data;
    }


    // Helper class to return combined data
    public static class TransactionData {
        public String name;
        public String date;
        public String transactionID;
        public List<String[]> rows;


        public TransactionData() {

        }
    }
}