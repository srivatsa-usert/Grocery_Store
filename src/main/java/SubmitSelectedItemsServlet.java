import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import item.Product;

@WebServlet("/SubmitSelectedItemsServlet")
public class SubmitSelectedItemsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private static final String XML_FILE_PATH = "xml/amazon_items.xml";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the selected items from the request parameter
        String[] selectedItemIds = request.getParameterValues("selectedItems");

        // Get the list of products from the request attribute
        ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");

        // Create a list of selected products
        List<Product> selectedProducts = new ArrayList<>();
        for (String selectedItemId : selectedItemIds) {
            if (selectedItemId != null && !selectedItemId.isEmpty()) {
                int itemId = Integer.parseInt(selectedItemId);
                for (Product product : products) {
                    if (product.getId() == itemId) {
                        selectedProducts.add(product);
                        break;
                    }
                }
            }
        }

        // Generate the XML file
        generateXMLFile(selectedProducts);

        // Remove the selected items from the item table
        removeSelectedItems(selectedProducts);

        // Redirect to the success page
        response.sendRedirect("success.jsp");
    }

    private void generateXMLFile(List<Product> selectedProducts) throws IOException {
        File xmlFile = new File(XML_FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(xmlFile))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            writer.println("<Products>");

            for (Product product : selectedProducts) {
                writer.println("<Product>");
                writer.println("    <ID>" + product.getId() + "</ID>");
                writer.println("    <Name>" + product.getName() + "</Name>");
                writer.println("    <Price>" + product.getPrice() + "</Price>");
                writer.println("</Product>");
            }

            writer.println("</Products>");
        }
    }

    private void removeSelectedItems(List<Product> selectedProducts) {
    }
}
