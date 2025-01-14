package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SigningController {

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
        String username = userfield.getText();
        String email = emailfield.getText();
        String password = passfield.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Connector.showError("All fields are required!", "Please make sure all fields are filled out.");
        } else if (!isValidEmail(email)) {
            Connector.showError("Invalid email format", "Please enter a valid email address.");
        } else if (password.length() < 8) {
            Connector.showError("Password too short", "Password must be at least 8 characters long.");
        } else if (Connector.isUsernameTaken(username)) {
            Connector.showError("Username already exists", "The username you entered is already taken. Please choose another one.");
        } else {
            if (Connector.signupUser(username, password, email)) {
                signlabel.setText("Signup successful!");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
