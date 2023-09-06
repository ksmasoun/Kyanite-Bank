/*
@author = Karanveer Singh
*/
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXMLMainController {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private GridPane MainGridPane;

    @FXML
    private TextField txtPassword;

    @FXML
    private ImageView imgBankLogo;

    @FXML
    private TextField txtUsername;

    private ArrayList<UserData> users = new ArrayList<>();

    public FXMLMainController() {
        try {
            File file = new File("users.txt");

            // Check if the file exists
            if (!file.exists()) {
                // Create the file if it doesn't exist
                file.createNewFile();
            } else {
                // Read user data from file and populate the ArrayList
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");
                    String name = fields[0];
                    String username = fields[1];
                    String email = fields[2];
                    String password = fields[3];
                    double balance = Double.parseDouble(fields[4]); // parse the balance value
                    UserData user = new UserData(name, username, email, password, balance); 
                    users.add(user);
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        MainGridPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login(new ActionEvent());
            }
        });

        // Set the bank logo image
        File imageFile = new File("src/images/imgBankLogo.jpg");
        Image image = new Image(imageFile.toURI().toString());
        imgBankLogo.setImage(image);
    }
    
    @FXML
    void login(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Check if the entered credentials match any registered user
        boolean foundUser = false;
        for (UserData user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                foundUser = true;

                // Pass the logged-in user data to the home controller
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLHome.fxml"));
                    Parent root = loader.load();
                    FXMLHomeController homeController = loader.getController();
                    homeController.setUser(user); // Pass the user data
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        if (!foundUser) {
            // Display an error message if the entered credentials don't match any registered user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
            //Got an error when login button clicked and both boxes got out of focus
            txtUsername.requestFocus();
        }
    }

    @FXML
    void redirectRegistration(ActionEvent event) throws IOException {
        // Load the FXML file for the registration form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLRegister.fxml"));
        Parent root = loader.load();

        // Create a new scene using the registration form FXML file
        Scene scene = new Scene(root);

        // Get the stage from the current button and set the new scene
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void addUser(UserData user) {
        users.add(user);
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }
}