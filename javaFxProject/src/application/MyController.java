package application;

import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;


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
    private Button profileButton;

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
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private HBox topSection;

    @FXML
    private void initialize() {
        // Set initial page (Home)
        showArticles(null);

        // Set login and signup button actions
        loginButton.setOnAction(event -> loadLogContent());
        signupButton.setOnAction(event -> loadSignContent());

        // Side menu actions
        profileButton.setOnAction(event -> loadProfilePage());
        searchPage.setOnAction(event -> loadPage("Search.fxml"));
        homeButton.setOnAction(event -> showArticles(null));
        techButton.setOnAction(event -> showArticles("Tech"));
        politicsButton.setOnAction(event -> showArticles("Politics"));
        sportsButton.setOnAction(event -> showArticles("Sports"));

        // Update UI based on user login status
        updateUIBasedOnLoginStatus();
    }

    private void loadPageContent(String fxmlPage) {
        clearContent();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPage));
            Node pageContent = loader.load();
            contentArea.getChildren().add(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while loading the page.");
        }
    }

    private void loadLogContent() {
        loadPageContent("authentification.fxml");
    }

    private void loadSignContent() {
        loadPageContent("Signing.fxml");
    }

    private void updateUIBasedOnLoginStatus() {
        if (UserSession.isConnected()) {
            // Hide login and signup buttons when the user is logged in
            loginButton.setVisible(false);
            signupButton.setVisible(false);

            // Display the logged-in user's username and email
            userNameLabel.setText("👤 " + UserSession.getUsername());
            userEmailLabel.setText("📧 " + UserSession.getEmail());

            // Make labels visible when the user is logged in
            userNameLabel.setVisible(true);
            userEmailLabel.setVisible(true);
        } else {
            // Show login and signup buttons when the user is not logged in
            loginButton.setVisible(true);
            signupButton.setVisible(true);

            // Clear the labels and hide them when the user is logged out
            userNameLabel.setText("");
            userEmailLabel.setText("");
            userNameLabel.setVisible(false);
            userEmailLabel.setVisible(false);
        }
    }




    private void loadProfilePage() {
        if (!UserSession.isConnected()) {  // Correct method here
            showNotConnectedAlert();
        } else {
            loadPage("userPage.fxml");
        }
    }

    private void showNotConnectedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not Connected");
        alert.setHeaderText("You need to be logged in to access this page.");
        alert.setContentText("Please log in first.");
        alert.showAndWait();
    }

    public void clearContent() {
        contentArea.getChildren().clear();
    }

    private void loadPage(String page) {
        clearContent();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
            Node pageContent = loader.load();
            contentArea.getChildren().add(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while loading the page.");
        }
    }

    public void showArticles(String category) {
        contentArea.getChildren().clear();

        // Create a dynamic title based on the category
        Label titleLabel = new Label("Top " + (category == null ? "World" : category) + " News");
        titleLabel.setStyle("-fx-font-size: 24px; " +
                             "-fx-font-weight: bold; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-color: #222; " +
                             "-fx-padding: 10px; " +
                             "-fx-max-width: Infinity; " +
                             "-fx-alignment: center;");

        // Fetch articles for the given category
        List<HBox> articles = Connector.fetchArticles(category);

        VBox pageContainer = new VBox();
        pageContainer.setSpacing(20);
        pageContainer.getChildren().add(titleLabel);

        VBox articlesContainer = new VBox();
        articlesContainer.setSpacing(10);
        articlesContainer.getChildren().addAll(articles);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(articlesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-padding: 20px;");

        // If no articles are returned, display a message
        if (articles.isEmpty()) {
            Label noArticlesLabel = new Label("No articles available for this category.");
            pageContainer.getChildren().add(noArticlesLabel);
        } else {
            pageContainer.getChildren().add(scrollPane);
        }

        contentArea.getChildren().add(pageContainer);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Button getSignupButton() {
        return signupButton;
    }

    public void setSignupButton(Button signupButton) {
        this.signupButton = signupButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public StackPane getContentArea() {
        return contentArea;
    }
}
