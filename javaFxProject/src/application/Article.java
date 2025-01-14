package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Article {

    private HBox articleHBox;
    private VBox textVBox;
    private Label titleLabel;
    private Label contentLabel;
    private Label categoryLabel;
    private Label createdAtLabel;
    private ImageView articleImageView;

    public Article(String title, String content, String category, String imageUrl, String createdAt) {
        // Initialize HBox for the overall layout
        articleHBox = new HBox();
        articleHBox.setSpacing(10);
        articleHBox.setStyle("-fx-background-color: #fff; " +
                             "-fx-padding: 10px; " +
                             "-fx-border-radius: 5px; " +
                             "-fx-background-radius: 5px; " +
                             "-fx-border-width: 1px; " +
                             "-fx-border-color: #ddd; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 10, 0.2, 0, 4);");
        articleHBox.setAlignment(Pos.CENTER);

        // Text container (VBox for title, created_at, content, and category)
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

        // Parse created_at and calculate time difference
        String timeAgo = calculateTimeAgo(createdAt);

        // Created_at label
        createdAtLabel = new Label(timeAgo +"  ⌚");
        createdAtLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold -fx-text-fill: #aaa ; -fx-font-style: italic;");
        createdAtLabel.setMaxWidth(Double.MAX_VALUE);
        createdAtLabel.setAlignment(Pos.TOP_LEFT);

        // Content label
        contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(600); // Set a maximum width for text wrapping
        contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-line-spacing: 5px;");
        VBox.setVgrow(contentLabel, Priority.ALWAYS);
        contentLabel.setMaxHeight(Double.MAX_VALUE);

        // Category label
        categoryLabel = new Label(category);
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: brown; -fx-font-style: italic;");
        categoryLabel.setAlignment(Pos.BOTTOM_LEFT);

        // Add title, created_at, content, and category elements to VBox
        textVBox.getChildren().addAll(titleLabel, createdAtLabel, contentLabel, categoryLabel);

        // Add image only if URL is not null or empty
        if (imageUrl != null && !imageUrl.isEmpty()) {
            articleImageView = new ImageView();
            try {
                articleImageView.setImage(new Image(imageUrl));
            } catch (Exception e) {
                articleImageView = null;
            }

            if (articleImageView != null) {
                articleImageView.setPreserveRatio(true);
                articleImageView.setFitWidth(300);
                articleImageView.setFitHeight(200);
                articleImageView.setStyle("-fx-border-radius: 12px 0 0 12px;");
                articleHBox.getChildren().add(articleImageView);
            }
        }

        // Allow text VBox to grow horizontally while image is fixed
        HBox.setHgrow(textVBox, Priority.ALWAYS);

        // Add text VBox to HBox
        articleHBox.getChildren().add(textVBox);
    }

    private String calculateTimeAgo(String createdAt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Adjust based on your format
            LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, formatter);
            LocalDateTime now = LocalDateTime.now();

            long days = ChronoUnit.DAYS.between(createdDateTime, now);
            if (days > 0) return days + " days ago";

            long hours = ChronoUnit.HOURS.between(createdDateTime, now);
            if (hours > 0) return hours + " hours ago";

            long minutes = ChronoUnit.MINUTES.between(createdDateTime, now);
            if (minutes > 0) return minutes + " minutes ago";

            return "Just now";
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public HBox getArticleHBox() {
        return articleHBox;
    }
}
