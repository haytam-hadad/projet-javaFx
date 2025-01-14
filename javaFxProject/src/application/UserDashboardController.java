package application;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UserDashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label joinDateLabel;

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
    private Button updatePasswordButton;

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
        if (updatePasswordButton != null) updatePasswordButton.setOnAction(event -> changePassword());
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

    private void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        if (!isInputValid(oldPassword, "Old Password") || !isInputValid(newPassword, "New Password")) return;

        if (showConfirmationDialog("Password Change", "Are you sure you want to change your password?",
                "Your password will be updated.")) {
            if (Connector.changePassword(UserSession.getId(), oldPassword, newPassword)) {
                System.out.println("Password updated successfully.");
                UserSession.logout();
                clearInputFields();
            } else {
                showErrorDialog("Password Change Failed", "The old password you entered is incorrect. Please try again.");
            }
        }
    }

    private void logout() {
        if (showConfirmationDialog("Logout", "Are you sure you want to log out?", "You will be logged out of your account.")) {
            UserSession.logout();
            System.out.println("Logging out...");
            System.exit(0);  // You can replace this with primaryStage.close() in a more complex app
        } else {
            System.out.println("Logout canceled.");
        }
    }

    private void clearInputFields() {
        newUsernameField.clear();
        newEmailField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showSuccessDialog(String message) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }
}
