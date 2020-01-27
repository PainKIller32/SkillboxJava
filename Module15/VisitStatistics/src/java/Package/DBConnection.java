package Package;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String dbName = "learn";
                String dbUser = "root";
                String dbPass = "mmm333";
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + dbName +
                                "?user=" + dbUser + "&password=" + dbPass + "&serverTimezone=UTC");
                connection.createStatement().execute("CREATE TABLE " +
                        "IF NOT EXISTS site_visit(id INT NOT NULL AUTO_INCREMENT," +
                        "browserName VARCHAR(50) UNIQUE, " +
                        "numberOfVisits INT(20), " +
                        " PRIMARY KEY(id))");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
