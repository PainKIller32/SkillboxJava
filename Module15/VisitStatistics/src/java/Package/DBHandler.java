package Package;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHandler {
    private Connection connection = DBConnection.getConnection();

    public void countVisits(String browserName) {
        try {
            String sql = "INSERT INTO site_visit(browserName, numberOfVisits) VALUE('" + browserName + "', 1) ON DUPLICATE KEY UPDATE numberOfVisits = numberOfVisits + 1";
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder getBrowserStatistics() {
        StringBuilder result = new StringBuilder();
        try {
            String sql = "SELECT * FROM site_visit";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                result.append("<tr><td>");
                result.append(rs.getString("browserName"));
                result.append("</td><td>");
                result.append(rs.getString("numberOfVisits"));
                result.append("</td></tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
