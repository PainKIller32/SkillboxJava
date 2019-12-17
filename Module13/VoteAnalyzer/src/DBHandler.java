import java.sql.*;

public class DBHandler {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private static int queryCount = 0;

    public DBHandler(String dbName, String dbUser, String dbPass) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName +
                        "?rewriteBatchedStatements=true&user=" + dbUser + "&password=" + dbPass);
        connection.createStatement().execute("TRUNCATE TABLE voter_count");
        //connection.createStatement().execute("DROP INDEX voter ON voter_count");
//                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
//                connection.createStatement().execute("CREATE TABLE voter_count(" +
//                        "id INT NOT NULL AUTO_INCREMENT, " +
//                        "name TINYTEXT NOT NULL, " +
//                        "birthDate DATE NOT NULL, " +
//                        "PRIMARY KEY(id))");
        preparedStatement = connection.prepareStatement("INSERT INTO voter_count(name, birthDate) VALUES (?,?)");
    }

    public void countVoter(String name, String birthDay) throws SQLException {
        birthDay = birthDay.replace('.', '-');
        if (queryCount > 50000) {
            flushBatch();
            queryCount = 0;
        }
        queryCount++;
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, birthDay);
        preparedStatement.addBatch();
    }

    public void flushBatch() throws SQLException {
        preparedStatement.executeBatch();
    }

    public void printVoterCounts() throws SQLException {
        //connection.createStatement().execute("CREATE INDEX voter ON voter_count (name(50), birthDate)");
        preparedStatement.close();
        String sql = "SELECT name, birthDate, COUNT(*) AS count FROM voter_count GROUP BY name, birthDate HAVING count > 1";
        ResultSet rs = connection.createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
        connection.close();
    }
}
