import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import item.Product;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterNumber = request.getParameter("filterNumber");
        String search = request.getParameter("search");
        String pageStr = request.getParameter("page");

        int currentPage = (pageStr != null) ? Integer.parseInt(pageStr) : 1;

        ArrayList<Product> products = new ArrayList<>();

        Properties props = getConnectionData();

        String dbUrl = props.getProperty("db.url");
        String dbUser = props.getProperty("db.user");
        String dbPassword = props.getProperty("db.passwd");
        
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            int itemsPerPage = Integer.parseInt(filterNumber);
            int offset = (currentPage - 1) * itemsPerPage;

            String query;
	         if (search != null && !search.isEmpty()) {
	             query = "SELECT * FROM item WHERE lower(name) LIKE ? LIMIT ? OFFSET ?";
	         } else {
	             query = "SELECT * FROM item LIMIT ? OFFSET ?";
	         }
	
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         if (search != null && !search.isEmpty()) {
	             preparedStatement.setString(1, "%" + search + "%"); 
	             preparedStatement.setInt(2, itemsPerPage); 
	             preparedStatement.setInt(3, offset); 
	         } else {
	             preparedStatement.setInt(1, itemsPerPage);
	             preparedStatement.setInt(2, offset); 
	         }


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String imageUrl = resultSet.getString("image_url");
                int quantity = resultSet.getInt("quantity");
                if( quantity != 0 ) {                	
                	products.add(new Product(id, name, description, price, imageUrl, quantity));
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("products", products);
        request.setAttribute("filterNumber", filterNumber); 
        
        int totalItems = getTotalItems(connection);

        int itemsPerPage = Integer.parseInt(filterNumber);
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        request.setAttribute("totalPages", totalPages);

        
        try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        request.getRequestDispatcher("buyer.jsp").forward(request, response);
    }
    
    private int getTotalItems(Connection connection) {
        int totalItems = 0;

        try {
            String query = "SELECT COUNT(*) FROM item WHERE quantity != 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalItems = resultSet.getInt(1);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalItems;
    }

    
}
