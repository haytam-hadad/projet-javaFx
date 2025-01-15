package application;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class addarticleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button SubArticlebtn;

    @FXML
    private TextField cateid;

    @FXML
    private TextArea contid;

    @FXML
    private Label pagetitle;

    @FXML
    private TextField titid;
    
    private int Cuserid = UserSession.getId();
    
    @FXML
    void OnSubArticle(ActionEvent event) {
        // Retrieve user inputs from text fields
        String title = titid.getText();
        String category = cateid.getText();
        String content = contid.getText();

        // SQL INSERT query
        String insertQuery = "INSERT INTO article (title, category, content, userID) VALUES (?, ?, ?, ?)";

        try (Connection connection = Connector.connect();
             java.sql.PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set parameters in the query
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, category);
            preparedStatement.setString(3, content);
            preparedStatement.setInt(4, Cuserid);

            // Execute the query
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Article successfully added to the database.");
            } else {
                System.out.println("Failed to add the article.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void initialize() {
        assert SubArticlebtn != null : "fx:id=\"SubArticlebtn\" was not injected: check your FXML file 'addArticle.fxml'.";
        assert cateid != null : "fx:id=\"cateid\" was not injected: check your FXML file 'addArticle.fxml'.";
        assert contid != null : "fx:id=\"contid\" was not injected: check your FXML file 'addArticle.fxml'.";
        assert pagetitle != null : "fx:id=\"pagetitle\" was not injected: check your FXML file 'addArticle.fxml'.";
        assert titid != null : "fx:id=\"titid\" was not injected: check your FXML file 'addArticle.fxml'.";

    }

}
