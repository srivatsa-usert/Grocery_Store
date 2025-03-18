import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GenerateReportServlet")
public class GenerateReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static Properties getConnectionData() {
        Properties props = new Properties();
        String fileName = "/home/srivatsa/eclipse-workspace/Lab_Assignment-2/src/main/java/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(ProductServlet.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> buyerNames = getBuyerNamesFromDatabase();

        request.setAttribute("buyerNames", buyerNames);

        request.getRequestDispatcher("generateReport.jsp").forward(request, response);
    }

    private List<String> getBuyerNamesFromDatabase() {
        List<String> names = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
        	Properties props = getConnectionData();
            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.passwd");

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String query = "SELECT username FROM buyer";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                names.add(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return names;
    }
}
