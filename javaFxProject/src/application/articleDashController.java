package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private TableColumn<List<String>, String> idcol;
   
    @FXML
    private TableColumn<List<String>, String> categorycol;
    
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
    private TableView<List<String>> table;

    @FXML
    private Label title;

    @FXML
    private TableColumn<List<String>, String> titlecol;
    
    private ObservableList<List<String>> articleList = FXCollections.observableArrayList();
    
    private void loadArticles() {
        int userId = UserSession.id;
        List<List<String>> articles = Connector.fetchArticlesByUser(userId);
        
        // Debugging: Print fetched articles
        if (articles != null) {
            articles.forEach(article -> System.out.println("Loaded article: " + article));
        } else {
            System.out.println("No articles found.");
        }
        
        articleList.setAll(articles);
        table.setItems(articleList);
        count.setText(String.valueOf(articleList.size())); // Update the count label
    }

    @FXML
    void OnAddArticle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addArticle.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("editor window");
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void OnDelete(ActionEvent event) {
        String searchText = searcharea2.getText().trim();
        if (!searchText.isEmpty()) {
            try {
                int articleId = Integer.parseInt(searchText);
                boolean deleted = Connector.deleteArticle(articleId);
                if (deleted) {
                    List<String> articleToDelete = articleList.stream()
                            .filter(article -> Integer.parseInt(article.get(0)) == articleId)
                            .findFirst()
                            .orElse(null);
                    if (articleToDelete != null) {
                        articleList.remove(articleToDelete);
                        table.setItems(articleList);
                        count.setText(String.valueOf(articleList.size())); // Update count label
                        System.out.println("Article deleted successfully.");
                    }
                } else {
                    System.out.println("Error: Article could not be deleted.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid article ID.");
            }
        } else {
            System.out.println("Error: Please enter an article ID to delete.");
        }
    }


    @FXML
    void OnLogout(ActionEvent event) {
        UserSession.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
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
            ObservableList<List<String>> filteredList = articleList.filtered(article ->
                article.get(1).toLowerCase().contains(searchText) ||
                article.get(2).toLowerCase().contains(searchText)
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
        
        idcol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        titlecol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        categorycol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));

        // Load data from database on the JavaFX Application Thread
        Platform.runLater(() -> loadArticles());
    }
}
