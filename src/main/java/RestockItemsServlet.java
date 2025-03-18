import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RestockItemsServlet")
public class RestockItemsServlet extends HttpServlet {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemID = request.getParameter("item_id");
        String restockQuantity = request.getParameter("restock_quantity");

        if (itemID != null && restockQuantity != null) {
            try {
                Properties props = getConnectionData();
                String dbUrl = props.getProperty("db.url");
                String dbUser = props.getProperty("db.user");
                String dbPassword = props.getProperty("db.passwd");

                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                String updateQuery = "UPDATE item SET quantity = quantity + ? WHERE item_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setInt(1, Integer.parseInt(restockQuantity));
                preparedStatement.setInt(2, Integer.parseInt(itemID));

                int updatedRows = preparedStatement.executeUpdate();

                preparedStatement.close();
                connection.close();

                if (updatedRows > 0) {
                    response.sendRedirect("SellerServlet"); 
                } else {
                    response.sendRedirect("SellerServlet?restockError=true"); 
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("SellerServlet?restockError=true"); 
        }
    }
}
