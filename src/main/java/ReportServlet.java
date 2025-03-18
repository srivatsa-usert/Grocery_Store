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


import item.SalesItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
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
    	String username = (String) request.getSession().getAttribute("username");
        String reportType = request.getParameter("reportType");
        
        Connection connection = null;
        
        Properties props = getConnectionData();
        String dbUrl = props.getProperty("db.url");
        String dbUser = props.getProperty("db.user");
        String dbPassword = props.getProperty("db.passwd");

        try {
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if ("user".equals(reportType)) {
        	String selectedUser = request.getParameter("selectedUser");

            if (selectedUser != null && !selectedUser.isEmpty()) {
                List<SalesItem> salesItems = getSalesByUser(selectedUser,connection,username);

                request.setAttribute("salesItems", salesItems);

                request.getRequestDispatcher("sales_report.jsp").forward(request, response);
            }
        } else if ("date".equals(reportType)) {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");

            if (startDate != null && endDate != null) {
                List<SalesItem> salesItems = getSalesByDate(startDate, endDate, connection,username);

                request.setAttribute("salesItems", salesItems);

                request.getRequestDispatcher("sales_report.jsp").forward(request, response);
            }
        }

    }

    private List<SalesItem> getSalesByUser(String selectedName,Connection connection,String username) {
        List<SalesItem> salesItems = new ArrayList<>();

        try{
            String sql = "SELECT ti.item_id, i.name, ti.quantity, ti.total_price, t.transaction_date "+
            	"FROM transaction_item ti "+
            	"JOIN transaction t ON t.transaction_id = ti.transaction_id "+
            	"JOIN buyer b ON b.buyer_id = t.buyer_id "+
            	"JOIN item i ON ti.item_id = i.item_id "+
            	"JOIN seller s ON s.seller_id = i.seller_id "+
            	"WHERE s.username = ? AND b.username = ? "+
            	"ORDER BY t.transaction_date DESC";


            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username); // Set the username as a parameter
                preparedStatement.setString(2, selectedName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        SalesItem salesItem = new SalesItem();
                        salesItem.setItemId(resultSet.getInt("item_id"));
                        salesItem.setItemName(resultSet.getString("name"));
                        salesItem.setQuantity(resultSet.getInt("quantity"));
                        salesItem.setTotalPrice(resultSet.getDouble("total_price"));
                        salesItem.setTransactionDate(resultSet.getDate("transaction_date"));
                        salesItems.add(salesItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return salesItems;
    }
    
    private List<SalesItem> getSalesByDate(String startDate, String endDate, Connection connection, String username) {
        List<SalesItem> salesItems = new ArrayList<>();

        try {
            
            String sql = "SELECT ti.item_id, i.name, ti.quantity, ti.total_price, t.transaction_date "+
                	"FROM transaction_item ti "+
                	"JOIN transaction t ON t.transaction_id = ti.transaction_id "+
                	"JOIN buyer b ON b.buyer_id = t.buyer_id "+
                	"JOIN item i ON ti.item_id = i.item_id "+
                	"JOIN seller s ON s.seller_id = i.seller_id "+
                	"WHERE s.username = ? AND (t.transaction_date BETWEEN ? AND ?) "+
                	"ORDER BY t.transaction_date DESC";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	preparedStatement.setString(1, username);
                preparedStatement.setString(2, startDate);
                preparedStatement.setString(3, endDate);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        SalesItem salesItem = new SalesItem();
                        salesItem.setItemId(resultSet.getInt("item_id"));
                        salesItem.setItemName(resultSet.getString("name"));
                        salesItem.setQuantity(resultSet.getInt("quantity"));
                        salesItem.setTotalPrice(resultSet.getDouble("total_price"));
                        salesItem.setTransactionDate(resultSet.getDate("transaction_date"));
                        salesItems.add(salesItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return salesItems;
    }

}
