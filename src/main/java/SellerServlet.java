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
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import item.Product;

@WebServlet("/SellerServlet")
public class SellerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static Properties getConnectionData() {

    	System.out.println("Entered getConnection");

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
    
    
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String sellerUsername = (String) session.getAttribute("username");

        ArrayList<Product> products = new ArrayList<>();

        Properties props = getConnectionData();

        String url = props.getProperty("db.url");
	    String dbUsername = props.getProperty("db.user");
	    String dbPassword = props.getProperty("db.passwd");

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String query = "SELECT item.item_id, item.name, item.description, item.price, item.image_url, item.quantity FROM item JOIN seller ON item.seller_id = seller.seller_id WHERE seller.username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sellerUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	int id = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String imageUrl = resultSet.getString("image_url");
                int quantity = resultSet.getInt("quantity");
                System.out.println(id+name+description+price+imageUrl);
                products.add(new Product(id, name, description, price, imageUrl,quantity));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("products", products);

        request.getRequestDispatcher("seller.jsp").forward(request, response);
    }
}
