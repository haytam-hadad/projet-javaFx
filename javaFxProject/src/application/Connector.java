package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

public class Connector {
    private static final String URL = "jdbc:mysql://localhost:3306/projetjavafx";
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
    public static List<HBox> fetchArticles(String c) {
        List<HBox> articleUIs = new ArrayList<>();
        
        // Default query for fetching articles
        String query = "SELECT * FROM article";
        
        // Add category condition if category is passed (not null or empty)
        if (c != null && !c.isEmpty()) {
            query += " WHERE category = ?";
        }
        
        try (Connection connection = connect(); 
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the category parameter if it is provided
            if (c != null && !c.isEmpty()) {
                statement.setString(1, c); // Setting the category parameter correctly
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    String content = resultSet.getString("content");
                    String imageUrl = resultSet.getString("ImageUrl");
                    String category = resultSet.getString("category"); // Always fetch category from DB

                    // Create Article object (JavaFX component) for each result
                    Article article = new Article(title, content, category, imageUrl);
                    articleUIs.add(article.getArticleHBox());  // Add HBox to the list
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching articles for category: " + c);
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
                    Article article = new Article(title, content, category , imageUrl); // Pass category to the Article object
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

    public static boolean authenticate(String username, String password) {
        // Query to retrieve the stored password hash for the given username
        String query = "SELECT passkey FROM users WHERE username = ?";
        
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);  // Set the username parameter
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the stored hashed password from the database
                    String storedHashedPassword = rs.getString("passkey");
                    
                    // Hash the input password using the same hashing method as when storing
                    String hashedInputPassword = hashPassword(password);
                    
                    // Compare the stored hashed password with the hashed input password
                    return storedHashedPassword.equals(hashedInputPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;  // Return false if username does not exist or authentication fails
    }

    public static boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // Returns true if a user with the matching username is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean signupUser(String username, String password, String email) {
        // Validate the password length (e.g., minimum 12 characters)
        if (password.length() < 12) {
            showError("Password too short", "Password must be at least 12 characters.");
            return false;
        }
        
        // Check if the username already exists
        if (isUsernameTaken(username)) {
            showError("Username already exists", "The username you provided is already taken.");
            return false;
        }
        
        // Optional: Validate the email format (simple regex)
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Invalid email", "Please provide a valid email address.");
            return false;
        }

        // Hash the password before inserting into the database
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            showError("Error", "An error occurred while hashing the password.");
            return false;
        }

        // If validations pass, insert the new user into the database
        String query = "INSERT INTO users (username, passkey, email) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);  // Store the hashed password
            stmt.setString(3, email);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // User has been successfully registered
                System.out.println("User registered successfully.");
            } else {
                showError("Registration Failed", "Something went wrong during registration.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "An error occurred while connecting to the database.");
        }
		return true;
    }

    // Method to hash the password using SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    public static void showError(String title, String message) {
        // Create an alert to show the error message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header text
        alert.setContentText(message);  // The error message
        alert.showAndWait();  // Show the alert and wait for user to dismiss it
    }

}

