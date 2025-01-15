package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthMyController {

    @FXML
    private Button connecter;

    @FXML
    private PasswordField passwordfield;

    @FXML
    private Button signup;

    @FXML
    private Label titrelogin;

    @FXML
    private TextField userfield;

    @FXML
    void OnSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signing.fxml"));
            Parent signinContent = loader.load();

            Scene scene = new Scene(signinContent);
            Stage signupStage = new Stage();
            signupStage.setScene(scene);
            signupStage.show();

            Stage currentStage = (Stage) signup.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogin(ActionEvent event) {
        String inputUsername = userfield.getText();
        String inputPassword = passwordfield.getText();

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            Connector.showError("All fields are required!", "Please make sure all fields are filled out.");
            return;
        }

        if (Connector.authenticate(inputUsername, inputPassword)) {
            try {
                Stage currentStage = (Stage) connecter.getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage mainStage = new Stage();
                mainStage.setScene(scene);
                mainStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Connector.showError("Username or Password is Incorrect", "Please Try again.");

        }

        passwordfield.clear();
    }
}
