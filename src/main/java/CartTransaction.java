

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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import item.TransactionItem;

/**
 * Servlet implementation class CartTransaction
 */
@WebServlet("/CartTransaction")
public class CartTransaction extends HttpServlet {
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
       
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		
		if(username != null) {
			
			try {
				
				
	            connection.setAutoCommit(false);
	            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	            
	            String cartQuery = "SELECT cart.item_id, cart.cart_quantity FROM cart " +
                        "JOIN buyer ON buyer.buyer_id = cart.buyer_id " +
                        "WHERE buyer.username = ?";
                PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
                cartStatement.setString(1, username);
                ResultSet cartResultSet = cartStatement.executeQuery();
                
                ArrayList<TransactionItem> transactionItems = new ArrayList<>();
                
                
                while (cartResultSet.next()) {
                    int itemId = cartResultSet.getInt("item_id");
                    int cartQuantity = cartResultSet.getInt("cart_quantity");
                    
                    String checkQuery = "SELECT quantity FROM item WHERE item_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, itemId);
                    ResultSet checkResult = checkStatement.executeQuery();

                    int currentQuantity = 0;

                    if (checkResult.next()) {
                        currentQuantity = checkResult.getInt("quantity");
                    }

                    if (currentQuantity - cartQuantity >= 0) {
                        String updateItemQuery = "UPDATE item SET quantity = quantity - ? WHERE item_id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateItemQuery);
                        updateStatement.setInt(1, cartQuantity);
                        updateStatement.setInt(2, itemId);

                        updateStatement.executeUpdate();

                        TransactionItem transactionItem = new TransactionItem(itemId, cartQuantity);
                        transactionItems.add(transactionItem);
                    } else {
                        connection.rollback();
                        response.sendRedirect("DeleteCartItemServlet?id=" + itemId);
                        return;
                    }
                    
                    checkResult.close();
                    checkStatement.close();
                }

                
                String deleteCartItemsQuery = "DELETE FROM cart " + "WHERE buyer_id = (SELECT buyer_id FROM buyer WHERE username = ?)";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteCartItemsQuery);
                
                deleteStatement.setString(1, username);
                deleteStatement.executeUpdate();
                
                int buyerId = getBuyerId(connection, username);
                java.util.Date utilDate = new java.util.Date(); 
                java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(utilDate.getTime());
                int transactionId = insertTransaction(connection, buyerId, sqlTimestamp);
                
                insertTransactionItems(connection, transactionItems, transactionId);
	            
	            
                connection.commit();

                cartResultSet.close();
                cartStatement.close();
                response.sendRedirect("success.jsp");
                
	            
	            
			}catch (SQLException e) {
				
				if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
            }finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
		}			
		else {
			response.sendRedirect("login.jsp");
		}
		
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
	
	private int insertTransaction(Connection connection, int buyerId, Timestamp transactionDate) throws SQLException {
	    String insertQuery = "INSERT INTO transaction (buyer_id, transaction_date) VALUES (?, ?)";
	    PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
	    insertStatement.setInt(1, buyerId);
	    insertStatement.setTimestamp(2, transactionDate);

	    insertStatement.executeUpdate();

	    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
	    int transactionId = -1; 
	    if (generatedKeys.next()) {
	        transactionId = generatedKeys.getInt(1);
	    }

	    insertStatement.close();

	    return transactionId;
	}
	
	private void insertTransactionItems(Connection connection, ArrayList<TransactionItem> transactionItems, int transactionId) throws SQLException {
	    String insertQuery = "INSERT INTO transaction_item (item_id, transaction_id, quantity, total_price) VALUES (?, ?, ?, ?)";
	    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

	    for (TransactionItem transactionItem : transactionItems) {
	        insertStatement.setInt(1, transactionItem.getItemId());
	        insertStatement.setInt(2, transactionId);
	        insertStatement.setInt(3, transactionItem.getQuantity());
	        insertStatement.setDouble(4, calculateTotalPrice(connection,transactionItem.getItemId(),transactionItem.getQuantity()));
	        insertStatement.addBatch(); 
	    }

	    int[] updateCounts = insertStatement.executeBatch();

	    for (int updateCount : updateCounts) {
	        if (updateCount != 1) {
	            
	        }
	    }

	    insertStatement.close();
	}
	
	private double calculateTotalPrice(Connection connection, int itemId, int quantity) throws SQLException {
	    String selectQuery = "SELECT price FROM item WHERE item_id = ?";
	    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	    selectStatement.setInt(1, itemId);

	    ResultSet resultSet = selectStatement.executeQuery();
	    
	    double price = 0.0;
	    if (resultSet.next()) {
	        price = resultSet.getDouble("price");
	    }

	    selectStatement.close();
	    
	    return price * quantity;
	}


}
