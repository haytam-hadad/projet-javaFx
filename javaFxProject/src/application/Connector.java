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
    private static final String URL = "jdbc:mysql://localhost:3306/javadb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to establish a connection to the database
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to fetch articles by category
    public static List<HBox> fetchArticles(String category) {
        List<HBox> articleUIs = new ArrayList<>();
        String query = "SELECT a.*, u.username FROM article a " +
                       "JOIN users u ON a.userID = u.Id" +  // Join articles with users based on userID
                       (category != null && !category.isEmpty() ? " WHERE a.category = ?" : "");

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (category != null && !category.isEmpty()) {
                statement.setString(1, category); // Set category parameter if provided
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    String content = resultSet.getString("content");
                    String imageUrl = resultSet.getString("ImageUrl");
                    String categoryResult = resultSet.getString("category");
                    String createdAt = resultSet.getString("created_at");
                    String author = resultSet.getString("username");  // Fetching the author's username

                    // Pass the author's name to the Article constructor
                    Article article = new Article(title, content, categoryResult, imageUrl, createdAt, author);
                    articleUIs.add(article.getArticleHBox());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articleUIs;
    }
    // Fetch articles submitted by the logged-in user
    public static List<List<String>> fetchArticlesByUser(int userId) {
        List<List<String>> articles = new ArrayList<>();
        String query = "SELECT id, title, category FROM article WHERE userID = ?"; // Fetch only the necessary columns
        
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    List<String> article = new ArrayList<>();
                    article.add(String.valueOf(resultSet.getInt("id")));
                    article.add(resultSet.getString("title"));
                    article.add(resultSet.getString("category"));
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }


    
    public static List<HBox> searchForArticles(String keyword) {
        List<HBox> articleUIs = new ArrayList<>();

        // SQL query to search for articles by matching title, content, or category, and join with users to get the username
        String query = "SELECT a.*, u.username FROM article a " +
                       "JOIN users u ON a.userID = u.Id " +  // Join articles with users based on userID
                       "WHERE a.title LIKE ? OR a.content LIKE ? OR a.category LIKE ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Prepare the query with the keyword
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("Title");
                    String content = resultSet.getString("content");
                    String imageUrl = resultSet.getString("ImageUrl");
                    String category = resultSet.getString("category");
                    String createdAt = resultSet.getString("created_at");  // Fetch created_at field
                    String author = resultSet.getString("username");  // Fetching the author's username

                    // Create Article object (JavaFX component) for each result
                    Article article = new Article(title, content, category, imageUrl, createdAt, author); // Pass created_at and author
                    articleUIs.add(article.getArticleHBox());
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for articles with keyword: " + keyword);
            e.printStackTrace();
        }

        return articleUIs;
    }
    
    // Method to authenticate a user during login
    public static boolean authenticate(String username, String password) {
        String query = "SELECT id, passkey, email, role FROM users WHERE username = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("passkey");
                    String hashedInputPassword = hashPassword(password);

                    if (storedHashedPassword.equals(hashedInputPassword)) {
                        // Successful login, fetch user details
                        int userId = rs.getInt("id");
                        String email = rs.getString("email");
                        String role = rs.getString("role");

                        // Set session data
                        UserSession.id = userId;
                        UserSession.username = username;
                        UserSession.email = email;
                        UserSession.role = role;
                        UserSession.isConnected = true;
                        UserSession.createdAt = java.time.LocalDateTime.now().toString();  // Store current timestamp (date + time)

                        return true;  // Authentication successful
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Authentication failed
    }
    
    
    public static boolean deleteArticle(int articleId) {
        String query = "DELETE FROM article WHERE id = ?";
        
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, articleId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the article was deleted
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to handle user signup
    public static boolean signupUser(String username, String password, String email) {
        if (password.length() < 12 || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$") || isUsernameTaken(username)) {
            showError("Validation Failed", "Ensure password is 12+ characters, valid email, and username isn't taken.");
            return false;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return false;

        String query = "INSERT INTO users (username, passkey, email) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, email);
            
            if (stmt.executeUpdate() > 0) {
                UserSession.username = username;
                UserSession.email = email;
                UserSession.isConnected = true;
                saveUser(username, email);  // Save additional user data
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to check if the username already exists
    static boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to hash passwords securely
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Method to display error alerts
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to save user information in the database
    public static void saveUser(String username, String email) {
        String query = "INSERT INTO users (username, email, status, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, "Active");
            stmt.setString(4, "User");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a user is logged in
    public static boolean isUserLoggedIn() {
        return UserSession.username != null && UserSession.isConnected;
    }

    // Method to logout the user and clear session
    public static void logout() {
        UserSession.username = null;
        UserSession.email = null;
        UserSession.isConnected = false;
    }

    // Method to update username in the database
    public static boolean updateUsername(int userId, String newUsername) {
        String query = "UPDATE users SET username = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setInt(2, userId);
            
            if (stmt.executeUpdate() > 0) {
                return true;  // Update successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Update failed
    }

    // Method to update email in the database
    public static boolean updateEmail(int userId, String newEmail) {
        String query = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newEmail);
            stmt.setInt(2, userId);
            
            if (stmt.executeUpdate() > 0) {
                return true;  // Update successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Update failed
    }

    // Method to change password: first checks the old password, then updates with the new password
    public static boolean changePassword(int userId, String oldPassword, String newPassword) {
        String query = "SELECT passkey FROM users WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("passkey");
                    if (storedHashedPassword.equals(hashPassword(oldPassword))) {
                        // Update with new password
                        return updatePassword(userId, newPassword);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Old password is incorrect or update failed
    }

    // Method to update password in the database
    private static boolean updatePassword(int userId, String newPassword) {
        String hashedPassword = hashPassword(newPassword);
        if (hashedPassword == null) {
            return false;
        }
        
        String query = "UPDATE users SET passkey = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            if (stmt.executeUpdate() > 0) {
                return true;  // Update successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Update failed
    }
}
