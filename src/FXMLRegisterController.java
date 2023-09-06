/*
@author = Karanveer Singh
*/
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FXMLRegisterController {

    @FXML
    private GridPane RegisterGridPane;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnBack;

    @FXML
    void BackToLogin(ActionEvent event) throws IOException {
        // Load the FXML file for the login form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMain.fxml"));
        Parent root = loader.load();
    
        // Create a new scene using the login form FXML file
        Scene scene = new Scene(root);
    
        // Get the stage from the current button and set the new scene
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void createAccount(ActionEvent event) throws IOException {
        String name = txtName.getText();
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        double balance = 0.0; // set initial balance to 0
    
        // Check if any field is empty
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }
    
        // Check if the email is valid
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
            return;
        }
    
        // Check if the password is valid
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password must have 8+ characters, 1 lowercase & uppercase letter, 1 digit, and 1 spl. character required for a valid password.");
            alert.showAndWait();
            return;
        }
    
        // Check if the username is already taken
        ArrayList<UserData> users = readUsersFromFile();
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("The username is already taken. Please choose another one.");
                alert.showAndWait();
                return;
            }
        }
    
        // Create a new user and add it to the ArrayList
        UserData newUser = new UserData(name, username, email, password, balance);
        users.add(newUser);
        writeUsersToFile(users);
    
        // Display a success message and redirect to the login form
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Your account has been created. Please log in.");
        alert.showAndWait();
    
        // Load the FXML file for the login form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMain.fxml"));
        Parent root = loader.load();
    
        // Create a new scene using the login form FXML file
        Scene scene = new Scene(root);
    
        // Get the stage from the current button and set the new scene
        Stage stage = (Stage) btnCreateAccount.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        // Add event handler to password field to listen for Enter key press
        RegisterGridPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    createAccount(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private ArrayList<UserData> readUsersFromFile() throws IOException {
        ArrayList<UserData> users = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) {
            return users;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String name = parts[0];
                String username = parts[1];
                String email = parts[2];
                String password = parts[3];
                double balance = Double.parseDouble(parts[4]); // parse the balance value
                UserData user = new UserData(name, username, email, password, balance);
                users.add(user);
            }
        }
        reader.close();
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