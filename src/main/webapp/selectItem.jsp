<%@ page import="java.util.ArrayList, item.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Select Items for Amazon Market</title>
    <link rel="stylesheet" href="style.css">
    <!-- Add any necessary styles for the form -->
</head>
<body>
    <jsp:include page="seller_header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
            <div class="col-md-8">
                <h1 style="text-align:center;"> Select Items for Amazon Market </h1>
                <form action="SubmitSelectedItemsServlet" method="post">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Item ID</th>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Available Quantity</th>
                                <th>Select Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                            ArrayList<Product> products = (ArrayList<Product>)request.getAttribute("products");
                            if (products != null && !products.isEmpty()) {
                                for (int i=0; i<products.size(); i++) {
                                    Product product = products.get(i);
                            %>
                            <tr>
                                <td><%= product.getId() %></td>
                                <td><%= product.getName() %></td>
                                <td><%= product.getPrice() %></td>
                                <td><%= product.getQuantity() %></td>
                                <td>
                                    <input type="number" name="selectedItems[<%= product.getId() %>]" min="0" max="<%= product.getQuantity() %>">
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="4"><h1>No products available.</h1></td>
                            </tr>
                            <%
                            }
                            %>
                        </tbody>
                    </table>
                    <button type="submit">Submit Selection</button>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
