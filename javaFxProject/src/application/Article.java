package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

public class Article {

    private HBox articleHBox;
    private VBox textVBox;
    private Label titleLabel;
    private Label contentLabel;
    private Label categoryLabel;
    private ImageView articleImageView;

    public Article(String title, String content, String category, String imageUrl) {
        // Initialize HBox for the overall layout
        articleHBox = new HBox();
        articleHBox.setSpacing(10);
        articleHBox.setStyle("-fx-background-color: #fff; " +
                             "-fx-padding: 10px; " +
                             "-fx-border-radius: 10px; " +
                             "-fx-background-radius: 10px; " +
                             "-fx-border-width: 1px; " +
                             "-fx-border-color: #ddd; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 10, 0.2, 0, 4);");
        articleHBox.setAlignment(Pos.CENTER);

        // Text container (VBox for title, category, and content)
        textVBox = new VBox();
        textVBox.setSpacing(15);
        textVBox.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-padding: 0 10px 10px 10px");
        textVBox.setMaxWidth(Double.MAX_VALUE);
        textVBox.setAlignment(Pos.TOP_LEFT);

        // Title label
        titleLabel = new Label(title);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setStyle("-fx-font-size: 23px; " +
                            "-fx-underline: true; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #000; " +
                            "-fx-padding: 5px; " +
                            "-fx-border-radius: 8px; " +
                            "-fx-background-radius: 8px;");
        titleLabel.setAlignment(Pos.CENTER);

        // Category label
        categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: brown; -fx-font-style: italic;");

        // Content label
        contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(600);  // Set a maximum width for text wrapping
        contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-line-spacing: 5px;");
        VBox.setVgrow(contentLabel, Priority.ALWAYS);
        contentLabel.setMaxHeight(Double.MAX_VALUE);

        // Add text elements to VBox
        textVBox.getChildren().addAll(titleLabel, contentLabel, categoryLabel);

        // Add image only if URL is not null or empty
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Image view with error handling and placeholder
            articleImageView = new ImageView();
            try {
                articleImageView.setImage(new Image(imageUrl)); // Try to load the image from the provided URL
            } catch (Exception e) {
                // If the URL is invalid, no image will be displayed
                articleImageView = null;
            }

            if (articleImageView != null) {
                articleImageView.setPreserveRatio(true); // Preserve aspect ratio
                articleImageView.setFitWidth(300);
                articleImageView.setFitHeight(200);
                articleImageView.setStyle("-fx-border-radius: 12px 0 0 12px;");
                articleHBox.getChildren().add(articleImageView); // Add the image view to the layout
            }
        }

        // Allow text VBox to grow horizontally while image is fixed
        HBox.setHgrow(textVBox, Priority.ALWAYS);

        // Add text VBox to HBox
        articleHBox.getChildren().add(textVBox);
    }

    public HBox getArticleHBox() {
        return articleHBox;
    }
}
