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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    
    private MyController myController;  // Reference to MyController

    // Setter method to receive the reference of MyController
    public void setMyController(MyController myController) {
        this.myController = myController;
    }	
    
    @FXML
    void OnSignup(ActionEvent event) {
        try {
            // Load the Sign Up page FXML (Signing.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signing.fxml"));
            Parent signinContent = loader.load(); // Load the FXML content

            // Get the controller from the loader (SigningController)
            SigningController signingController = loader.getController();

            // Add the loaded Sign Up content to the contentArea (if you have it in the same window)
            // If this is within the same stage, clear the current content and add the new one
            myController.getContentArea().getChildren().clear(); // Clear existing content
            myController.getContentArea().getChildren().add(signinContent); // Add the Sign Up form

            // Optionally, hide login and signup buttons (if that's desired behavior)
            myController.getLoginButton().setVisible(false);
            myController.getSignupButton().setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogin(ActionEvent event) {
        String username = userfield.getText();
        String password = passwordfield.getText();

        if (Connector.authenticate(username, password)) {
            try {
                // Close the current window
                // Get the current window (stage)
                Stage currentStage = (Stage) connecter.getScene().getWindow();
                currentStage.close(); // Close the login window

                // Load the main content window (MyController) through FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
                Parent root = loader.load();

                // Get the controller instance of the main window
                MyController myController = loader.getController();

                // Disable the login and signup buttons (visibility)
                Button loginButton = (Button) root.lookup("#loginButton");
                Button signupButton = (Button) root.lookup("#signupButton");
                loginButton.setVisible(false); // Hide the login button
                signupButton.setVisible(false); // Hide the signup button

                // Create a new scene and show the main window
                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.show(); // Show the main window

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            titrelogin.setText("Login Failed");
        }	
    }

}
