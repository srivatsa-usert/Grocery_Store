import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/signUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private static Properties getConnectionData() {

    	System.out.println("Entered getConnection");

        Properties props = new Properties();
        String fileName = "/home/srivatsa/eclipse-workspace/Lab_Assignment-2/src/main/java/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(SignUpServlet.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return props;
    }


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Properties props = getConnectionData();

        String dburl = props.getProperty("db.url");
	    String dbuser = props.getProperty("db.user");
	    String dbpassword = props.getProperty("db.passwd");



        String username = request.getParameter("signup_username");
        String password = request.getParameter("signup_password");
        String email = request.getParameter("signup_email");
        String address = request.getParameter("signup_address");
        String userType = request.getParameter("signup_userType");

        System.out.println(userType);

        if( userType.equals("Seller") ) {

        	System.out.println("Entered Seller");

        	String shopName = request.getParameter("signup_shopname");

        	try {
    	        Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);
    	        String insertQuery = "INSERT INTO seller (username, password, email, address, shop_name) VALUES (?, ?, ?, ?, ?)";
    	        PreparedStatement preparedStatement = con.prepareStatement(insertQuery);

    	        preparedStatement.setString(1, username);
    	        preparedStatement.setString(2, password);
    	        preparedStatement.setString(3, email);
    	        preparedStatement.setString(4, address);
    	        preparedStatement.setString(5, shopName);

    	        int rowsAffected = preparedStatement.executeUpdate();

    	        if (rowsAffected > 0) {
    	            System.out.println("seller record inserted successfully.");
    	        } else {
    	            System.out.println("Failed to insert seller record.");
    	        }
    	    } catch (SQLException e) {
    	        Logger lgr = Logger.getLogger(SignUpServlet.class.getName());
    	        lgr.log(Level.SEVERE, e.getMessage(), e);
    	        out.println("An error occurred while processing the request.");
    	    }

        }
        else if(userType.equals("Buyer")) {

        	System.out.println("Entered Buyer");

        	try {
    	        Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);
    	        String insertQuery = "INSERT INTO buyer (username, password, email, address) VALUES (?, ?, ?, ?)";
    	        PreparedStatement preparedStatement = con.prepareStatement(insertQuery);

    	        preparedStatement.setString(1, username);
    	        preparedStatement.setString(2, password);
    	        preparedStatement.setString(3, email);
    	        preparedStatement.setString(4, address);


    	        int rowsAffected = preparedStatement.executeUpdate();

    	        if (rowsAffected > 0) {
    	            System.out.println("Buyer record inserted successfully.");
    	        } else {
    	            System.out.println("Failed to insert buyer record.");
    	        }
    	    } catch (SQLException e) {
    	        Logger lgr = Logger.getLogger(SignUpServlet.class.getName());
    	        lgr.log(Level.SEVERE, e.getMessage(), e);
    	        out.println("An error occurred while processing the request.");
    	    }

        }



        response.sendRedirect("index.jsp");
    }
}
