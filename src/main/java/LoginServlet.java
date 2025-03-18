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
import jakarta.servlet.http.HttpSession;


@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

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


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("login_username");
        String password = request.getParameter("login_password");
        String userType = request.getParameter("login_userType");


        if( userType.equals("Buyer") ) {

        	if (authenticateUser(username, password,"Buyer",response,request)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
            	response.sendRedirect("ProductServlet?filterNumber=10");
            } else {
            	System.out.println("wrong authentication buyer");
            }

        }



        else if(userType.equals("Seller")) {

        	if (authenticateUser(username, password,"Seller",response,request)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("SellerServlet");
            } else {
            	System.out.println("wrong authentication seller");
            }

        }
        
        else if(userType.equals("Amazon")) {
        	if( username.equals("amazon") && password.equals("amazon") ) {        		
        		response.sendRedirect("amazon.jsp");
        	}else {
        		System.out.println("wrong authentication Amazon");
        	}
        }

    }


	private static boolean authenticateUser(String username, String password, String userType, HttpServletResponse response,HttpServletRequest request) throws IOException {

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    Properties props = getConnectionData();

	    String dburl = props.getProperty("db.url");
	    String dbuser = props.getProperty("db.user");
	    String dbpassword = props.getProperty("db.passwd");

	    try {
	        Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);

	        if (userType.equals("Buyer")) {

	            String query = "SELECT * FROM buyer WHERE username = ?";
	            PreparedStatement preparedStatement = con.prepareStatement(query);
	            preparedStatement.setString(1, username);
	            preparedStatement.executeQuery();

	            if (preparedStatement.getResultSet().next()) {
	                return true;
	            }
	        } else if (userType.equals("Seller")) {
	            String query = "SELECT * FROM seller WHERE username = ? AND password = ?";
	            PreparedStatement preparedStatement = con.prepareStatement(query);
	            preparedStatement.setString(1, username);
	            preparedStatement.setString(2, password);
	            preparedStatement.executeQuery();

	            if (preparedStatement.getResultSet().next()) {
	                return true;
	            }
	        }

	    } catch (SQLException e) {
	        Logger lgr = Logger.getLogger(LoginServlet.class.getName());
	        lgr.log(Level.SEVERE, e.getMessage(), e);
	    }

	    HttpSession session = request.getSession();
        session.setAttribute("loginError", "Invalid username or password. Please try again.");
	    response.sendRedirect("index.jsp"); 
	    return false;
	}


}