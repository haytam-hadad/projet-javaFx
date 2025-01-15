package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class UserDashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label joinDateLabel;
    
    @FXML
    private Button postArticleButton;
    
    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField newEmailField;

    @FXML
    private TextField oldPasswordField;

    @FXML
    private TextField newPasswordField;
    
    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Button updateUsernameButton;

    @FXML
    private Button updateEmailButton;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        String email = UserSession.getEmail();
        String createdAt = UserSession.getCreatedAt();

        usernameLabel.setText(username != null ? username : "No username available");
        emailLabel.setText(email != null ? email : "No email available");
        joinDateLabel.setText(createdAt != null ? createdAt : "Unknown");

        // Initialize button actions
        if (updateUsernameButton != null) updateUsernameButton.setOnAction(event -> updateUsername());
        if (updateEmailButton != null) updateEmailButton.setOnAction(event -> updateEmail());
        if (changePasswordButton != null) changePasswordButton.setOnAction(event -> changePassword());
        if (logoutButton != null) logoutButton.setOnAction(event -> logout());
    }

    private boolean showConfirmationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean isInputValid(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            showErrorDialog("Invalid Input", fieldName + " cannot be empty.");
            return false;
        }
        return true;
    }

    private void showErrorDialog(String title, String contentText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();
    }

    private void updateUsername() {
        String newUsername = newUsernameField.getText();
        if (!isInputValid(newUsername, "Username")) return;

        if (showConfirmationDialog("Username Update", "Are you sure you want to change your username?",
                "Your new username will be: " + newUsername)) {
            if (Connector.updateUsername(UserSession.getId(), newUsername)) {
                UserSession.setUsername(newUsername);
                usernameLabel.setText(newUsername);
                clearInputFields();
                showSuccessDialog("Username updated successfully.");
            } else {
                showErrorDialog("Update Failed", "Failed to update username. Please try again.");
            }
        }
    }

    private void updateEmail() {
        String newEmail = newEmailField.getText();
        if (!isInputValid(newEmail, "Email")) return;

        if (showConfirmationDialog("Email Update", "Are you sure you want to change your email?",
                "Your new email will be: " + newEmail)) {
            if (Connector.updateEmail(UserSession.getId(), newEmail)) {
                UserSession.setEmail(newEmail);
                emailLabel.setText(newEmail);
                clearInputFields();
                showSuccessDialog("Email updated successfully.");
            } else {
                showErrorDialog("Update Failed", "Failed to update email. Please try again.");
            }
        }
    }
    
    public void Onpost() {
        try {
            // Close the current window
            Stage currentStage = (Stage) postArticleButton.getScene().getWindow();
            currentStage.close();  // Closes the current window
            
            // Load the new FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("articleDash.fxml"));
            Parent root = loader.load();
            
            // Create a new stage and set the new scene
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root)); // Set the new scene
            newStage.setTitle("Article Dashboard"); // Set the window title
            newStage.show();  // Show the new stage
        } catch (IOException e) {
            e.printStackTrace();  // Handle exception
            showErrorDialog("Error", "Failed to load new page.");
        }
    }

    
    
    private void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        if (!isInputValid(oldPassword, "Old Password") || !isInputValid(newPassword, "New Password")) {
        	System.out.println("hello world");
        };
        if (showConfirmationDialog("Password Change", "Are you sure you want to change your password?",
                "Your password will be updated.")) {
            if (Connector.changePassword(UserSession.getId(), oldPassword, newPassword)) {
                System.out.println("Password updated successfully.");
            } else {
                showErrorDialog("Password Change Failed", "The old password you entered is incorrect. Please try again.");
            }
        }
    }

    private void logout() {
        if (showConfirmationDialog("Logout", "Are you sure you want to log out?",
                "You will be logged out of your account.")) {
            // Clear the user session
            UserSession.logout();
            System.out.println("Logging out...");

            // Close the current application window
            System.exit(0);  // This will terminate the app
        } else {
            System.out.println("Logout canceled.");
        }
    }

    private void clearInputFields() {
        if (newUsernameField != null) newUsernameField.clear();
        if (newEmailField != null) newEmailField.clear();
        if (oldPasswordField != null) oldPasswordField.clear();
        if (newPasswordField != null) newPasswordField.clear();
        if (confirmPasswordField != null) confirmPasswordField.clear();
    }


    private void showSuccessDialog(String message) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }

}
