import java.sql.*;

public class SQLInjectionDemo {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/demo_db";
    static final String USER = "demo_user";
    static final String PASS = "demo_pass";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            setupDatabase(conn);

            String maliciousInput = "'; DROP TABLE users; --";
            System.out.println("Unsafe Query using Statement:");
            unsafeQuery(conn, maliciousInput);

            System.out.println("\nSafe Query using PreparedStatement:");
            safeQuery(conn, maliciousInput);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setupDatabase(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS users");
        stmt.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, name TEXT)");
        stmt.execute("INSERT INTO users (name) VALUES ('Alice'), ('Bob')");
    }

    private static void unsafeQuery(Connection conn, String name) {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE name = '" + name + "'";
            System.out.println("Executing: " + query);
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }

    private static void safeQuery(Connection conn, String name) {
        try {
            String query = "SELECT * FROM users WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}