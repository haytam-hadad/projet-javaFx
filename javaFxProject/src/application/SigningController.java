package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SigningController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailfield;

    @FXML
    private PasswordField passfield;

    @FXML
    private Label signlabel;

    @FXML
    private Button submitbtn;

    @FXML
    private TextField userfield;

    @FXML
    void OnSubmit(ActionEvent event) {
        // Retrieve the values from the text fields
        String username = userfield.getText();
        String email = emailfield.getText();
        String password = passfield.getText();

        // Perform the validation checks
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Connector.showError("All fields are required!", "Please make sure all fields are filled out.");
        } else if (password.length() < 12) {
        	Connector.showError("Password too short", "Password must be at least 12 characters long.");
        } else if (Connector.isUsernameTaken(username)) {  // Assuming you have this method to check the username
        	Connector.showError("Username already exists", "The username you entered is already taken. Please choose another one.");
        } else {
            // If validation passes, proceed with signup logic (e.g., saving to the database)
            boolean signupSuccess = Connector.signupUser(username, password, email);
            if (signupSuccess) {
                signlabel.setText("Sign Up Successful!");
                try {
                    // Close the current window
                    // Get the current window (stage)
                    Stage currentStage = (Stage) submitbtn.getScene().getWindow();
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
            	Connector.showError("Signup failed", "An error occurred while signing up. Please try again.");
            }
        }
    }



    @FXML
    void initialize() {
        assert emailfield != null : "fx:id=\"emailfield\" was not injected: check your FXML file 'Signing.fxml'.";
        assert passfield != null : "fx:id=\"passfield\" was not injected: check your FXML file 'Signing.fxml'.";
        assert signlabel != null : "fx:id=\"signlabel\" was not injected: check your FXML file 'Signing.fxml'.";
        assert submitbtn != null : "fx:id=\"submitbtn\" was not injected: check your FXML file 'Signing.fxml'.";
        assert userfield != null : "fx:id=\"userfield\" was not injected: check your FXML file 'Signing.fxml'.";

    }

}
