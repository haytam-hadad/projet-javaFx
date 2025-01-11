package application;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchController {

    // FXML elements
    @FXML
    private TextField searchField;
    @FXML
    private Button submitSearch;
    @FXML
    private Label noResultsLabel;
    @FXML
    private VBox searchResultsVBox;  // This will hold the search results dynamically

    // Initialize method called after the FXML is loaded
    @FXML
    private void initialize() {
        // Set the action for the search button
        submitSearch.setOnAction(event -> performSearch());
    }

    // Method to perform search action
    private void performSearch() {
        String keyword = searchField.getText().trim();

        // Input validation
        if (keyword.isEmpty()) {
            noResultsLabel.setText("Please enter a search term.");
            noResultsLabel.setVisible(true);
            return;
        }

        // Clear previous results and hide the error label
        searchResultsVBox.getChildren().clear();
        noResultsLabel.setVisible(false);

        // Fetch articles from the database based on the search keyword
        List<HBox> articles = DataBaseConnector.searchForArticles(keyword);

        // Check if any articles were returned
        if (articles.isEmpty()) {
            noResultsLabel.setText("No results found for '" + keyword + "'.");
            noResultsLabel.setVisible(true);
        } else {
            // Add the articles to a new VBox
            VBox resultsVBox = new VBox(10);
            resultsVBox.getChildren().addAll(articles);

            // Create a ScrollPane for the VBox
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(resultsVBox);
            scrollPane.setFitToWidth(true); 
            scrollPane.setStyle("-fx-padding: 10px;"); 

            // Add the ScrollPane to the searchResultsVBox
            searchResultsVBox.getChildren().add(scrollPane);
        }
    }
}
