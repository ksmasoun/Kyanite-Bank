/*
@author = Karanveer Singh
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXMLWithdrawMoneyController {

    @FXML
    private GridPane WithdrawMoney;

    @FXML
    private Button btnWithdrawMoneyAnchor;

    @FXML
    private TextField txtAmtWithdraw;

    @FXML
    private TextField txtPassword;

    @FXML
    void WithdrawMoney(ActionEvent event) {
        String password = txtPassword.getText();
        double amount;
        try {
            amount = Double.parseDouble(txtAmtWithdraw.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount.");
            alert.showAndWait();
            return;
        }

        // Find the user with the entered password
        ArrayList<UserData> users = readUsersFromFile();
        UserData currentUser = null;
        for (UserData user : users) {
            if (user.getPassword().equals(password)) {
                currentUser = user;
                break;
            }
        }

        if (password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a password.");
            alert.showAndWait();
            return;
        }


        // If the user is not found, show an error message
        if (currentUser == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect password. Please try again.");
            alert.showAndWait();
            return;
        }

        // If the user has insufficient balance, show an error message
        if (currentUser.getBalance() < amount) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Insufficient balance. Please try again.");
            alert.showAndWait();
            return;
        }

        // Reduce the money in the user's account
        double newBalance = currentUser.getBalance() - amount;
        currentUser.setBalance(newBalance);

        // Write the updated user data to the file
        try {
            writeUsersToFile(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display a success message and redirect to the home page
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Money has been withdrawn from your account.");
        alert.showAndWait();

        // Redirect to the home page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLHome.fxml"));
            Parent root = loader.load();
            FXMLHomeController homeController = loader.getController();
            homeController.setUser(currentUser);
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnWithdrawMoneyAnchor.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // methods to read and write users to file
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

    private void writeUsersToFile(ArrayList<UserData> users) throws IOException {
        FileWriter writer = new FileWriter("users.txt");
        for (UserData user : users) {
            String line = user.getName() + "," + user.getUsername() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getBalance() + "\n";
            writer.write(line);
        }
        writer.close();
    }

}