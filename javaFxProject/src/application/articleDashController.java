package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class articleDashController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addarticle;

    @FXML
    private Button backhomebtn;
    
    @FXML
    private TableColumn<Article, Integer> idcol;
   
    @FXML
    private TableColumn<Article, String> categorycol;
    
    @FXML
    private Label count;

    @FXML
    private Button deletebtn;

    @FXML
    private Button logout;

    @FXML
    private Button search;

    @FXML
    private TextField searcharea;

    @FXML
    private TextField searcharea2;

    @FXML
    private TableView<Article> table;

    @FXML
    private Label title;

    @FXML
    private TableColumn<Article, String> titlecol;
    
    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    
    private void loadArticles() {
        int userId = UserSession.id; // Get the logged-in user's ID from the UserSession
        List<Article> articles = Connector.fetchArticlesByUser(userId); // Fetch articles for this user
        articleList.setAll(articles);
        table.setItems(articleList);
        count.setText(String.valueOf(articleList.size())); // Update the count label
    }

    @FXML
    void OnAddArticle(ActionEvent event) {
    	try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("addArticle.fxml"));
	        Parent root = loader.load();
	
	        // Create a new stage
	        Stage newStage = new Stage();
	        newStage.setTitle("editor window");
	
	        // Set the scene
	        Scene scene = new Scene(root);
	        newStage.setScene(scene);
	
	        // Show the new window
	        newStage.show();

    	} catch (IOException e) {
        e.printStackTrace();
    	}
    }

    @FXML
    void OnDelete(ActionEvent event) {
        String searchText = searcharea2.getText().trim();  // Get text from the search area
        
        if (!searchText.isEmpty()) {
            // Try to parse it as an integer (ID)
            try {
                int articleId = Integer.parseInt(searchText);
                
                // Delete the article from the database
                boolean deleted = Connector.deleteArticle(articleId);
                if (deleted) {
                    // If deletion was successful, remove the article from the ObservableList
                    Article articleToDelete = articleList.stream()
                            .filter(article -> article.getId() == articleId)
                            .findFirst()
                            .orElse(null);  // If no article is found, it returns null

                    if (articleToDelete != null) {
                        articleList.remove(articleToDelete);
                        table.setItems(articleList);
                        count.setText(String.valueOf(articleList.size()));  // Update count label
                    }
                } else {
                    // Handle failure (optional: show error message)
                    System.out.println("Error: Article could not be deleted.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Error: Please enter a valid article ID.");
            }
        } else {
            // Handle case where the search area is empty (optional: show error message)
            System.out.println("Error: Please enter an article ID to delete.");
        }
    }
    

    @FXML
    void OnLogout(ActionEvent event) {
    	UserSession.logout();
    	try {
            // Load the main page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();

            // Set the new scene to the current stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnReturn(ActionEvent event) {
    	try {
            // Load the main page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();

            // Set the new scene to the current stage
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnSearch(ActionEvent event) {
    	String searchText = searcharea.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<Article> filteredList = articleList.filtered(article -> 
                article.getTitle().toLowerCase().contains(searchText) ||
                article.getCategory().toLowerCase().contains(searchText)
            );
            table.setItems(filteredList);
        } else {
            table.setItems(articleList); // Reset to full list
        }
    }

    @FXML
    void initialize() {
        assert addarticle != null : "fx:id=\"addarticle\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert backhomebtn != null : "fx:id=\"backhomebtn\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert categorycol != null : "fx:id=\"categorycol\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert count != null : "fx:id=\"count\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert deletebtn != null : "fx:id=\"deletebtn\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert idcol != null : "fx:id=\"idcol\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert search != null : "fx:id=\"search\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert searcharea != null : "fx:id=\"searcharea\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert searcharea2 != null : "fx:id=\"searcharea2\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'articleDash.fxml'.";
        assert titlecol != null : "fx:id=\"titlecol\" was not injected: check your FXML file 'articleDash.fxml'.";
        
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titlecol.setCellValueFactory(new PropertyValueFactory<>("title"));
        categorycol.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Load data from database
        loadArticles();
    }

}
