import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHandler {
    private Connection connection;
    private StringBuilder builder = new StringBuilder();
    private static final int BUFFER_SIZE = 256_000;

    public DBHandler(String dbName, String dbUser, String dbPass) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName +
                            "?user=" + dbUser + "&password=" + dbPass);
            connection.createStatement().execute("TRUNCATE TABLE voter_count");
//                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
//                connection.createStatement().execute("CREATE TABLE voter_count(" +
//                        "id INT NOT NULL AUTO_INCREMENT, " +
//                        "name TINYTEXT NOT NULL, " +
//                        "birthDate DATE NOT NULL, " +
//                        "`count` INT NOT NULL, " +
//                        "PRIMARY KEY(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void countVoter(String name, String birthDay) throws SQLException {
        birthDay = birthDay.replace('.', '-');
        if (builder.length() > BUFFER_SIZE) {
            flushCounts();
        }
        if (builder.length() == 0) {
            builder.append("INSERT INTO voter_count(name, birthDate, `count`) VALUES");
        } else {
            builder.append(',');
        }
        builder.append("('")
                .append(name)
                .append("', '")
                .append(birthDay)
                .append("', 1)");
    }

    public void flushCounts() throws SQLException {
        builder.append(" ON DUPLICATE KEY UPDATE `count`=`count` + 1");
        connection.createStatement().execute(builder.toString());
        builder = new StringBuilder();
    }

    public void printVoterCounts() throws SQLException {
        String sql = "SELECT name, birthDate, `count` FROM voter_count WHERE `count` > 1";
        ResultSet rs = connection.createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
    }
}
