package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SearchController {

    @FXML
    private TextField searchField;

    @FXML
    private Button submitsearch;
    
    @FXML
    private Label inpError;

    @FXML
    private void initialize() {
        submitsearch.setOnAction(event -> handleSearch());
    }
    
    
    private void handleSearch() {
        String keyword = searchField.getText();
        
        // Check if the input is empty or invalid
        if (keyword == null || keyword.trim().isEmpty()) {
            // Update the label text on the JavaFX Application thread
            Platform.runLater(() -> inpError.setText("Please enter a keyword to search."));
            return;
        }
        
        // Clear the error message if search is valid
        Platform.runLater(() -> inpError.setText(""));
        
        System.out.println("Searching for: " + keyword);
        // Add your search logic here (e.g., query a database or API)
    }
    
    
    
}
