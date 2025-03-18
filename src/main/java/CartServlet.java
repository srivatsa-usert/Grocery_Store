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

import item.CartProduct;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private static Properties getConnectionData() {

    	System.out.println("Entered getConnection");

        Properties props = new Properties();
        String fileName = "/home/srivatsa/eclipse-workspace/Lab_Assignment-2/src/main/java/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(CartServlet.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username != null) {
            ArrayList<CartProduct> products = new ArrayList<>();
            Properties props = getConnectionData();
            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.passwd");

            try {
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                String buyerIdQuery = "SELECT buyer_id FROM buyer WHERE username = ?";
                PreparedStatement buyerIdStatement = connection.prepareStatement(buyerIdQuery);
                buyerIdStatement.setString(1, username);
                ResultSet buyerIdResult = buyerIdStatement.executeQuery();

                int buyerId = 0;
                if (buyerIdResult.next()) {
                    buyerId = buyerIdResult.getInt("buyer_id");
                }

                String cartQuery = "SELECT item.*,cart.cart_quantity FROM cart " +
                        "INNER JOIN item ON cart.item_id = item.item_id " +
                        "WHERE cart.buyer_id = ?";
                PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
                cartStatement.setInt(1, buyerId);
                ResultSet resultSet = cartStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("item_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    String imageUrl = resultSet.getString("image_url");
                    int quantity = resultSet.getInt("quantity");
                    int cartQuantity = resultSet.getInt("cart_quantity");
                    System.out.println(id + name + description + price + imageUrl);
                    products.add(new CartProduct(id, name, description, price, imageUrl, quantity,cartQuantity));
                }

                resultSet.close();
                cartStatement.close();
                buyerIdResult.close();
                buyerIdStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.setAttribute("products", products);
        }

        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}
