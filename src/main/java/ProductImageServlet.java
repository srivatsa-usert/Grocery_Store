import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ProductImageServlet")
public class ProductImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));

        // Connect to the database and retrieve the image URL
        String imageUrl = getImageUrlFromDatabase(productId);

        if (imageUrl != null) {
            // Send the image URL as a response
            response.setContentType("text/plain");
            response.getWriter().write(imageUrl);
            System.out.println(imageUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // This method retrieves the image URL from the database
    private String getImageUrlFromDatabase(int productId) {
        String imageUrl = null;

        // Connect to the database and retrieve the image URL
        try {
            Properties props = getConnectionData();
            String dburl = props.getProperty("db.url");
            String dbuser = props.getProperty("db.user");
            String dbpassword = props.getProperty("db.passwd");

            Connection connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
            String sql = "SELECT image_url FROM item WHERE item_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                imageUrl = resultSet.getString("image_url");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(imageUrl);
        return imageUrl;
    }

    private static Properties getConnectionData() {
        // Load the database connection properties from a properties file
        Properties props = new Properties();
        String fileName = "/home/srivatsa/eclipse-workspace/Lab_Assignment-2/src/main/java/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(LoginServlet.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }
}
