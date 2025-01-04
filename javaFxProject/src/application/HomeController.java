package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private ScrollPane rootScrollPane;  // The root ScrollPane is injected here

    @FXML
    private VBox articlesBox;  // The VBox inside the ScrollPane

    @FXML
    private void initialize() {
        // You can load articles by default if needed
        loadArticles();
    }

    // Method to load articles dynamically when the button is clicked
    @FXML
    public void loadArticles() {
        Platform.runLater(() -> {
            // Clear existing articles
            articlesBox.getChildren().clear();

            String imagePath = "file:/C:/Users/hp/OneDrive/Desktop/1.jpg" ; // Use file URI format
            addArticle("News article 1", "Content for the first dynamic news article.", "Technology", imagePath);
            addArticle("News article 2", "Content for the second dynamic news article.", "Sports", imagePath);
            addArticle("News article 3", "Content for the third dynamic news article.", "Health", imagePath);
            addArticle("News article 4", "Content for the fourth dynamic news article.", "Business", imagePath);
        });
    }

    // Simplified method to add articles using the Article class with category and image
    private void addArticle(String title, String content, String category, String imageUrl) {
        Article article = new Article(title, content, category, imageUrl);
        articlesBox.getChildren().add(article.getArticleHBox());
    }
}
