package application;

import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class MyController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox sideMenuVBox;

    @FXML
    private Button homeButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button politicsButton;

    @FXML
    private Button searchPage;

    @FXML
    private Button signupButton;

    @FXML
    private Button sportsButton;

    @FXML
    private Button techButton;
    
    @FXML
    private void initialize() {
    	
        // Set initial page (Home)
    	showArticles(null);
        
        loginButton.setOnAction(event -> clearContent());
        signupButton.setOnAction(event -> clearContent());
        
        //SideMenu
        searchPage.setOnAction(event -> loadPage("Search.fxml"));
        homeButton.setOnAction(event -> showArticles(null));
        techButton.setOnAction(event -> showArticles("Tech"));
        politicsButton.setOnAction(event -> showArticles("Politics"));
        sportsButton.setOnAction(event -> showArticles("Sports"));
    }

    private void clearContent() {
        contentArea.getChildren().clear();
    }

    private void loadPage(String page) {
        clearContent();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
            VBox pageContent = loader.load();
            contentArea.getChildren().add(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showArticles(String category) {
        contentArea.getChildren().clear();  // Clear any existing content
        
        // Create a dynamic title based on the category
        Label titleLabel = new Label("Top " + (category == null ? "World" : category) + " News");
        titleLabel.setStyle("-fx-font-size: 24px; " +
                             "-fx-font-weight: bold; " +
                             "-fx-text-fill: white; " +  // Set text color to white
                             "-fx-background-color: #222; " +  // Set background color to #222
                             "-fx-padding: 10px; " +
                             "-fx-max-width: Infinity; " +  // Ensure label stretches to fill the width
                             "-fx-alignment: center;");  // Center the text horizontally

        
        // Fetch articles for the given category
        List<HBox> articles = Connector.fetchArticles(category);  // Assuming you have this method for fetching by category

        // Create a VBox to hold the title and articles
        VBox pageContainer = new VBox();
        pageContainer.setSpacing(20);  // Optional: adjust the spacing between title and articles
        pageContainer.getChildren().add(titleLabel);  // Add the title at the top
        
        // Create a VBox to hold the articles and wrap them in a ScrollPane
        VBox articlesContainer = new VBox();
        articlesContainer.setSpacing(10);  // Optional: add spacing between articles
        articlesContainer.getChildren().addAll(articles);  // Add the articles to the container

        // Create a ScrollPane and set the VBox with articles as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(articlesContainer);
        scrollPane.setFitToWidth(true);  // Ensure content fits to width of the ScrollPane
        scrollPane.setFitToHeight(true); // Ensure content fits to height of the ScrollPane
        scrollPane.setStyle("-fx-padding: 20px;");  // Set padding for all sides (top, right, bottom, left)

        // Check if any articles were returned
        if (articles.isEmpty()) {
            Label noArticlesLabel = new Label("No articles available for this category.");
            pageContainer.getChildren().add(noArticlesLabel);
        } else {
            pageContainer.getChildren().add(scrollPane);  // Add the ScrollPane containing articles to the container
        }

        // Add the page container (with title and articles) to the content area
        contentArea.getChildren().add(pageContainer);
    }


    
}
