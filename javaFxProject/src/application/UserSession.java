package application;

import java.time.LocalDateTime;

public class UserSession {

    // Static fields to hold the session data
    public static int id = -1;  // Default to -1 to indicate no active session
    public static String username;
    public static String email;
    public static String role;
    public static String passkey;
    public static String createdAt;  // This can be LocalDateTime, to store date and time
    public static boolean isConnected = false;

    // Getters and Setters
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        UserSession.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserSession.email = email;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        UserSession.role = role;
    }

    public static String getCreatedAt() {
        return createdAt;
    }
    

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setConnected(boolean isConnected) {
        UserSession.isConnected = isConnected;
    }

    // Method to set the createdAt timestamp
    public static void setCreatedAt() {
        UserSession.createdAt = LocalDateTime.now().toString();  // Store current timestamp (date + time)
    }

    // Method to logout (clear session data)
    public static void logout() {
        UserSession.id = -1;  // Use -1 to indicate no active session
        UserSession.username = null;
        UserSession.email = null;
        UserSession.role = null;
        UserSession.passkey = null;
        UserSession.createdAt = null;
        UserSession.isConnected = false;
    }
}
