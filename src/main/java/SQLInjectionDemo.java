import java.sql.*;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;


//TEST with this statement = admin' OR '1'='1

public class SQLInjectionDemo {
    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String DB_HOST = dotenv.get("DB_HOST");
    private static final String DB_PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    // Constructing the JDBC URL dynamically using environment variables
    private static final String URL = String.format("jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);

    // Database credentials (from environment variables)
    private static final String USER = DB_USER;
    private static final String PASSWORD = DB_PASSWORD;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.println("\nUsing Statement:");
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM users WHERE username = '" + username + "'";
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    System.out.println("User found: " + rs.getString("username"));
                }
            }

            System.out.println("\nUsing PreparedStatement:");
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.println("User found: " + rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}