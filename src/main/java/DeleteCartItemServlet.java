import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteCartItemServlet")
public class DeleteCartItemServlet extends HttpServlet {
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
    	HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		Properties props = getConnectionData();

        String url = props.getProperty("db.url");
        String dbUsername = props.getProperty("db.user");
        String dbPassword = props.getProperty("db.passwd");
        
        Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, dbUsername, dbPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if (username != null) {
            int item_id = Integer.parseInt(request.getParameter("id"));

            try {

                int buyer_id = getBuyerId(connection, username);

                if (buyer_id != -1) {
                    deleteCartItem(connection, item_id, buyer_id);
                }

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("cart");
    }
    
    private int getBuyerId(Connection connection, String username) throws SQLException {
	    String query = "SELECT buyer_id FROM buyer WHERE username = ?";
	    PreparedStatement statement = connection.prepareStatement(query);
	    statement.setString(1, username);
	    ResultSet resultSet = statement.executeQuery();

	    int buyerId = -1; 
	    if (resultSet.next()) {
	        buyerId = resultSet.getInt("buyer_id");
	    }

	    resultSet.close();
	    statement.close();

	    return buyerId;
	}
    
    private void deleteCartItem(Connection connection, int item_id, int buyer_id) throws SQLException {
        String query = "DELETE FROM cart WHERE item_id = ? AND buyer_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, item_id);
            preparedStatement.setInt(2, buyer_id);
            preparedStatement.executeUpdate();
        }
    }


}
