package utils;

import java.io.*;
import java.util.Scanner;

public class UserAuth {
    private final String filename;

    //has a parameter filename which is the name of the file that will be created when using this class
    public UserAuth(String filename) {
        this.filename = filename;
        try {
            File file = new File(filename); // creates a file that will be named later on
            if (!file.exists()) { //if file does not exist it creates a new file under that name
                file.createNewFile();
            }
        } catch (IOException e) { // checks for any errors
            e.printStackTrace();
        }
    }

    //the register function is under boolean
    public boolean register(String username, String password) { //uses parameter username and password
        if (userExists(username)) { // checks if the user already exists if it doesnt returns false
            return false;
        }

        // if it is a new user it writes a new line on the file which records the name and the password
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(username + "," + password + "\n");
            return true; // it returns true meaning it will do the action and the try catch
        } catch (IOException e) {
            return false;
        }
    }

    // function login also uses username and login as parameters
    public boolean login(String username, String password) {
        try (Scanner scanner = new Scanner(new File(filename))) { //Scans the file created
            while (scanner.hasNextLine()) { //ues the while loop checking if the file still has another line
                String[] parts = scanner.nextLine().split(","); // creates a string array called parts that splits the line into comma
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) { // if the length of the line is two string and the indexes are under username and password
                    return true; //It returns true
                }
            }
        } catch (FileNotFoundException e) { //catches error
            return false;
        }
        return false;
    }

    private boolean userExists(String username) { // refers to the function from line 24
        try (Scanner scanner = new Scanner(new File(filename))) { //checks on the file created
            while (scanner.hasNextLine()) { // uses while loop to check all the line so long as there is still a line after that
                String[] parts = scanner.nextLine().split(","); // creates a string array named parts that seperates using comma
                if (parts.length >= 1 && parts[0].equals(username)) { // if the length of the line is greater than 1 & matches the username on that line
                    return true; // it returns true meaning an action will be done
                }
            }
        } catch (FileNotFoundException e) { //reads error
            return false;
        }
        return false;
    }
}
