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

@WebServlet("/addCart")
public class AddCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    private static Properties getConnectionData() {

    	System.out.println("Entered getConnection");

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
    
    
    
    

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        System.out.println("User: "+username);

        if (username == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int buyer_id = -1;
        
        
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


        Properties props = getConnectionData();

        String url = props.getProperty("db.url");
	    String dbUsername = props.getProperty("db.user");
	    String dbPassword = props.getProperty("db.passwd");

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String query = "SELECT buyer_id FROM buyer WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                buyer_id = resultSet.getInt("buyer_id");
            }

            if (buyer_id != -1) {
            	
            	System.out.println("item: "+request.getParameter("item_id"));
            	System.out.println("quantity: "+request.getParameter("quantity"));
            	
                int item_id = Integer.parseInt(request.getParameter("item_id"));
                int cart_quantity = Integer.parseInt(request.getParameter("quantity"));
                int currentCartQuantity = 0;
                
                
                String cartQuery = "SELECT cart_quantity FROM cart WHERE item_id = ? AND buyer_id = ?";
                PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
                cartStatement.setInt(1, item_id);
                cartStatement.setInt(2, buyer_id);

                ResultSet cartResultSet = cartStatement.executeQuery();
                if (cartResultSet.next()) {
                    currentCartQuantity = cartResultSet.getInt("cart_quantity");
                }
                
                
                String itemQuery = "SELECT quantity FROM item WHERE item_id = ?";
                PreparedStatement itemStatement = connection.prepareStatement(itemQuery);
                itemStatement.setInt(1, item_id);

                ResultSet itemResultSet = itemStatement.executeQuery();
                if (itemResultSet.next()) {
                    int availableQuantity = itemResultSet.getInt("quantity");
                    
                    
                    if( currentCartQuantity != 0 ) {
                    	
                    	if (cart_quantity + currentCartQuantity <= availableQuantity) {
                    		String updateCartQuery = "UPDATE cart SET cart_quantity = ? WHERE item_id = ? AND buyer_id = ?";
                            PreparedStatement updateCartStatement = connection.prepareStatement(updateCartQuery);
                            updateCartStatement.setInt(1, cart_quantity+currentCartQuantity);
                            updateCartStatement.setInt(2, item_id);
                            updateCartStatement.setInt(3, buyer_id);

                            updateCartStatement.executeUpdate();

                            updateCartStatement.close();
                        }
                    	
                    }
                    

                    else{
                        String updateCartQuery = "INSERT INTO cart (item_id, buyer_id, cart_quantity) VALUES (?, ?, ?)";
                        PreparedStatement updateCartStatement = connection.prepareStatement(updateCartQuery);
                        updateCartStatement.setInt(1, item_id);
                        updateCartStatement.setInt(2, buyer_id);
                        updateCartStatement.setInt(3, cart_quantity);

                        updateCartStatement.executeUpdate();

                        updateCartStatement.close();
                    }
                }
                
                cartStatement.close();
                itemStatement.close();
                preparedStatement.close();
                connection.close();
                

            }

            


            response.sendRedirect("ProductServlet?filterNumber=10");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
