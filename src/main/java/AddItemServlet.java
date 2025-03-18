import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/AddItemServlet")
@MultipartConfig
public class AddItemServlet extends HttpServlet {
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
        String itemName = request.getParameter("name");
        String itemDescription = request.getParameter("description");
        double itemPrice = Double.parseDouble(request.getParameter("price"));
        int itemQuantity = Integer.parseInt(request.getParameter("quantity"));
        Part imagePart = request.getPart("image");

        String rootPath = getServletContext().getRealPath("/");
        String imageDirectory = rootPath + "images";
        
        String imageFileName = generateUniqueImageFileName(imagePart);

        String imageDirectoryPath = imageDirectory + File.separator;

        imagePart.write(imageDirectoryPath + imageFileName);

        String imageUrl = "images/" + imageFileName;

        HttpSession session = request.getSession();
        String sellerUsername = (String) session.getAttribute("username");

        if (sellerUsername == null) {
            response.sendRedirect("index.jsp");
        } else {
            Properties props = getConnectionData();

            String url = props.getProperty("db.url");
            String dbUsername = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.passwd");

            try {
                Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

                String findSellerIdQuery = "SELECT seller_id FROM seller WHERE username = ?";
                PreparedStatement findSellerIdStatement = connection.prepareStatement(findSellerIdQuery);
                findSellerIdStatement.setString(1, sellerUsername);
                ResultSet sellerIdResult = findSellerIdStatement.executeQuery();

                int sellerId = -1;
                if (sellerIdResult.next()) {
                    sellerId = sellerIdResult.getInt("seller_id");
                }

                if (sellerId != -1) {
                    String insertItemQuery = "INSERT INTO item (name, description, price, image_url, quantity, seller_id) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertItemStatement = connection.prepareStatement(insertItemQuery);
                    insertItemStatement.setString(1, itemName);
                    insertItemStatement.setString(2, itemDescription);
                    insertItemStatement.setDouble(3, itemPrice);
                    insertItemStatement.setString(4, imageUrl);
                    insertItemStatement.setInt(5, itemQuantity);
                    insertItemStatement.setInt(6, sellerId);

                    insertItemStatement.executeUpdate();
                }

                sellerIdResult.close();
                findSellerIdStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.sendRedirect("SellerServlet");
        }

    }

    private String generateUniqueImageFileName(Part imagePart) {
        String submittedFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        String extension = submittedFileName.substring(submittedFileName.lastIndexOf("."));
        String baseName = UUID.randomUUID().toString();
        return baseName + extension;
    }
}
