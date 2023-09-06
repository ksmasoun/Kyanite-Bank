/*
@author = Karanveer Singh
*/
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLHomeController {

    @FXML
    private Button btnCheckBalance;
    @FXML
    private Button btnDepositMoney;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnWithdrawMoney;
    @FXML
    private Label lblSessionId;
    @FXML
    private Label lblName;
    @FXML
    private Label lblNameHeader;
    @FXML
    private AnchorPane ReplaceSceneHome;
    @FXML
    private SplitPane SplitPane;

    public void setUser(UserData user) {
        lblName.setText(user.getName());
        lblNameHeader.setText(user.getName());
    }

    public void initialize() {
        generateSessionId();
    }
    
    private void generateSessionId() {
        Random random = new Random();
        String digits = "0123456789";
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String accountNum = "";
        
        for (int i = 0; i < 15; i++) {
            if (random.nextBoolean()) {
                accountNum += digits.charAt(random.nextInt(digits.length()));
            } else {
                accountNum += letters.charAt(random.nextInt(letters.length()));
            }
        }
        
        lblSessionId.setText(accountNum);
    }

    @FXML
    void DepositMoney(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDepositMoney.fxml"));
        AnchorPane pane = loader.load();
        ReplaceSceneHome.getChildren().setAll(pane);
    }

    @FXML
    void WithdrawMoney(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLWithdrawMoney.fxml"));
        AnchorPane pane = loader.load();
        ReplaceSceneHome.getChildren().setAll(pane);
    }

    @FXML
    void CheckBalance(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDisplayBalance.fxml"));
        AnchorPane pane = loader.load();
        ReplaceSceneHome.getChildren().setAll(pane);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Log Out");
        dialog.setContentText("Are you sure you want to log out?");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLThankYou.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}