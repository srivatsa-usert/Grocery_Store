<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, item.SalesItem" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Sales Report</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="footer.css">
</head>
<body>
    <jsp:include page="seller_header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
            <div class="col-md-10">
                <h1 style="text-align:center;">Sales Report</h1>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Item ID</th>
                            <th>Name</th>
                            <th>Quantity</th>
                            <th>Total Price</th>
                            <th>Transaction Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        List<SalesItem> salesItems = (List<SalesItem>) request.getAttribute("salesItems");
                        if (salesItems != null && !salesItems.isEmpty()) {
                            for (SalesItem salesItem : salesItems) {
                        %>
                        <tr>
                            <td><%= salesItem.getItemId() %></td>
                            <td><%= salesItem.getItemName() %></td>
                            <td><%= salesItem.getQuantity() %></td>
                            <td><%= salesItem.getTotalPrice() %></td>
                            <td><%= salesItem.getTransactionDate() %></td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="5"><h1>No sales items available.</h1></td>
                        </tr>
                        <%
                        }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <jsp:include page="footer.jsp" />
</body>
</html>
