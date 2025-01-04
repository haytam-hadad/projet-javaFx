package application;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
        loadPage("Home.fxml");
        
        loginButton.setOnAction(event -> clearContent());
        signupButton.setOnAction(event -> clearContent());
        
        //SideMenu
        homeButton.setOnAction(event -> handleButtonClick(homeButton, "Home.fxml"));
        searchPage.setOnAction(event -> handleButtonClick(searchPage, "Search.fxml"));
        techButton.setOnAction(event -> clearContent());
        politicsButton.setOnAction(event -> clearContent());
        sportsButton.setOnAction(event -> clearContent());
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

    private void handleButtonClick(Button clickedButton, String page) {
        // Remove the 'visited' class from all buttons in the side menu
        for (Node button : sideMenuVBox.getChildren().filtered(node -> node instanceof Button)) {
            button.getStyleClass().remove("visited-button");
        }
        // Add the 'visited' class to the clicked button
        clickedButton.getStyleClass().add("visited-button");
        // Load the corresponding page
        loadPage(page);
    }
    
}
