/*
@author = Karanveer Singh
*/
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FXMLDisplayBalanceController {

    @FXML
    private Label lblAccBalance;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        ArrayList<UserData> users = readUsersFromFile();
        UserData currentUser = findUserByName(users, username);

        if (currentUser != null) {
            double balance = currentUser.getBalance();
            lblAccBalance.setText(String.valueOf(balance));
        } else {
            lblAccBalance.setText("User not found");
        }
    }

    private ArrayList<UserData> readUsersFromFile() {
        ArrayList<UserData> users = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) {
            return users;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    String username = parts[1];
                    String email = parts[2];
                    String password = parts[3];
                    double balance;
                    try {
                        balance = Double.parseDouble(parts[4]);
                    } catch (NumberFormatException e) {
                        balance = 0.0; // Set balance to a default value if parsing fails
                    }
                    UserData user = new UserData(name, username, email, password, balance);
                    users.add(user);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    private UserData findUserByName(ArrayList<UserData> users, String name) {
        for (UserData user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
}