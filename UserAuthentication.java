import java.util.HashMap;
import java.util.Map;

public class UserAuthentication {

    // Simulating a database of user credentials
    private static Map<String, String> userCredentials = new HashMap<>();

    static {
        // Adding some sample user credentials to the database
        userCredentials.put("john_doe", "password123");
        userCredentials.put("alice_smith", "securePass");
    }

    // Method to authenticate a user
    public static boolean authenticateUser(String username, String password) {
        // Check if the provided username exists in the database
        if (userCredentials.containsKey(username)) {
            // If the username exists, check if the provided password matches the stored password
            String storedPassword = userCredentials.get(username);
            return storedPassword.equals(password);
        }
        // If the username does not exist, authentication fails
        return false;
    }

    public static void main(String[] args) {
        // Example usage of the authenticateUser method
        String inputUsername = "john_doe";
        String inputPassword = "password123";

        if (authenticateUser(inputUsername, inputPassword)) {
            System.out.println("Authentication successful for user: " + inputUsername);
        } else {
            System.out.println("Authentication failed for user: " + inputUsername);
        }
    }
}
