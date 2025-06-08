package utils;

import java.io.*;
import java.util.Scanner;

public class UserAuth {
    private final String filename;

    public UserAuth(String filename) {
        this.filename = filename;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        if (userExists(username)) {
            return false;
        }

        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(username + "," + password + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean login(String username, String password) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    private boolean userExists(String username) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }
}
