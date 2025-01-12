package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;

public class Connector {
    private static final String URL = "jdbc:mysql://localhost:3306/javadb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Connect to the database
    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database!");
            e.printStackTrace();
        }
        return connection;
    }

    // Method to fetch articles by category
    public static List<HBox> fetchArticles(String category) {
        List<HBox> articleUIs = new ArrayList<>();
        
        // Default query for fetching articles based on category, if category is passed
        String query = "SELECT * FROM article";
        
        // Add category condition if category is passed (not null or empty)
        if (category != null && !category.isEmpty()) {
            query += " WHERE category = ?";
        }
        
        try (Connection connection = connect(); 
             PreparedStatement statement = connection.prepareStatement(query)) {

            // If category is passed, set the category in the query
            if (category != null && !category.isEmpty()) {
                statement.setString(1, category); // Setting the category parameter correctly
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    String content = resultSet.getString("content");
                    String imageUrl = resultSet.getString("ImageUrl");

                    // Create Article object (JavaFX component) for each result
                    Article article = new Article(title, content, category, imageUrl);
                    articleUIs.add(article.getArticleHBox());  // Add HBox to the list
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching articles for category: " + category);
            e.printStackTrace();
        }

        return articleUIs;
    }


    public static List<HBox> searchForArticles(String keyword) {
        List<HBox> articleUIs = new ArrayList<>();
        
        // SQL query to search for articles by matching title, content, or category
        String query = "SELECT * FROM article WHERE title LIKE ? OR content LIKE ? OR category LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Prepare the query with the keyword
            String searchPattern = "%" + keyword + "%"; // Add wildcard for search
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern); // Added search for category as well

            System.out.println("Executing query: " + query);  // Debug print for the query

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    String content = resultSet.getString("content");
                    String imageUrl = resultSet.getString("ImageUrl");
                    String category = resultSet.getString("category"); // Fetch category

                    // Print out each result for debugging
                    System.out.println("Found article: ");
                    System.out.println("Title: " + title);
                    System.out.println("Content: " + content);
                    System.out.println("Category: " + category);
                    System.out.println("Image URL: " + imageUrl);

                    // Create Article object (JavaFX component) for each result
                    Article article = new Article(title, content, imageUrl, category); // Pass category to the Article object
                    articleUIs.add(article.getArticleHBox());  // Add HBox to the list
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for articles with keyword: " + keyword);
            e.printStackTrace();
        }

        // Print the total number of results
        System.out.println("Total articles found: " + articleUIs.size());

        return articleUIs;
    }



}

