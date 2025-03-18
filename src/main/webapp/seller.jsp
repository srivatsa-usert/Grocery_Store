<%@ page import="java.util.ArrayList, item.Product" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
	<title>Seller</title>
	<link rel="stylesheet" href="style.css">
	<link rel="stylesheet" href="modal.css">
	<link rel="stylesheet" href="footer.css">
	<style>
		.small-image {
		    max-width: 100px;
		    max-height: 100px;
		}
	</style>
</head>
<body>
    <jsp:include page="seller_header.jsp" />
    
    <a href="SelectItemServlet" class="btn btn-primary">Select Items for Amazon Market</a>
    
    
    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
            <div class="col-md-10">
            
            	<h1 style="text-align:center;"> Your Products </h1>
            	
                <table class="table">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Item ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Restock</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        ArrayList<Product> products = (ArrayList<Product>)request.getAttribute("products");
						if (products != null && !products.isEmpty()) {
							for (int i=0;i<products.size();i++) {
					        	Product product = products.get(i);
                        %>
                        
                            <tr>
                                <td width=100 height=100><img class="small-image" src="<%= product.getImageUrl() %>"></td>
                                <td><%= product.getId() %></td>
                                <td><%= product.getName() %></td>
                                <td><%= product.getPrice() %></td>
                                <td><%= product.getQuantity() %></td>
                                <td>
						            <form action="RestockItemsServlet" method="post">
						                <input type="hidden" name="item_id" value="<%= product.getId() %>">
						                <input type="number" name="restock_quantity" placeholder="Quantity" min="1" required>
						                <button type="submit">Restock</button>
						            </form>
						        </td>
                            </tr>
                         
                        <%
				            }
				        } else {
				        %>
				        <tr>
				            <td colspan="6"><h1>No products available.</h1></td>
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
