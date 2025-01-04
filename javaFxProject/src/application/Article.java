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
        articleHBox.setSpacing(0); // No space between image and text
        articleHBox.setStyle("-fx-background-color: #fff; " +
                             "-fx-padding: 10px; " +
                             "-fx-border-radius: 10px; " +
                             "-fx-background-radius: 10px; " +
                             "-fx-border-width: 1px; " +
                             "-fx-border-color: #bbb; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 10, 0.2, 0, 4);");
        articleHBox.setAlignment(Pos.CENTER);

        // Image view to cover the left side
        articleImageView = new ImageView();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            articleImageView.setImage(new Image(imageUrl)); // Load image from URL
        } else {
            // Placeholder image
            articleImageView.setImage(new Image("https://example.com/placeholder.jpg"));
        }
        articleImageView.setPreserveRatio(false); // Allow stretching
        articleImageView.setFitWidth(300); // Fixed width for left side
        articleImageView.setFitHeight(200); // Dynamic height for proportional content
        articleImageView.setStyle("-fx-border-radius: 12px 0 0 12px;");

        // Text container (VBox for title, category, and content)
        textVBox = new VBox();
        textVBox.setSpacing(15);
        textVBox.setStyle("-fx-font-family: 'Arial', sans-serif; -fx-padding: 0 10px 10px 10px");
        textVBox.setMaxWidth(Double.MAX_VALUE); // Allow text to occupy remaining space
        textVBox.setAlignment(Pos.TOP_LEFT); // Ensure all elements start from the top-left

        // Title label with modern font, full width, and clean typography
        titleLabel = new Label(title);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setStyle("-fx-font-size: 23px; " +
        					"-fx-underline: true;"+
        					"-fx-background-color: brown;"+
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: #fff; " +
                            "-fx-padding: 5px; " +
                            "-fx-border-radius: 8px; " +
                            "-fx-background-radius: 8px;");
        titleLabel.setAlignment(Pos.CENTER);

        // Category label with subtle color and italic style
        categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: brown; -fx-font-style: italic;");

        // Content label with wrap text and subtle styling
        contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-line-spacing: 5px;");
        VBox.setVgrow(contentLabel, Priority.ALWAYS); // Make the contentLabel grow in the VBox
        contentLabel.setMaxHeight(Double.MAX_VALUE); // Allow maximum height

        // Add text elements to the VBox
        textVBox.getChildren().addAll(titleLabel, contentLabel, categoryLabel);

        // Ensure the image takes the left side and the text VBox takes the rest
        HBox.setHgrow(textVBox, Priority.ALWAYS);

        // Add the image and the text VBox to the HBox
        articleHBox.getChildren().addAll(articleImageView, textVBox);
    }

    public HBox getArticleHBox() {
        return articleHBox;
    }
}
