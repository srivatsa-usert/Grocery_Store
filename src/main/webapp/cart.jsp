<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="item.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Cart</title>
	<link rel="stylesheet" href="modal.css">
	<link rel="stylesheet" href="style.css">	
	<link rel="stylesheet" href="footer.css">
	<style>
		@media (min-width: 1025px) {
		
			.h-custom {
				height: 100vh !important;
			}
		}
		
		.card{
			margin-left: 20%;
			margin-right: 20%;
		}
		.total {
		    background-color: #f5f5f5;
		    padding: 20px;
		    text-align: center;
		    border: 1px solid #ddd;
		}
		
		.total h3 {
		    font-size: 24px;
		    color: #333;
		    margin: 0;
		}
		
		.card {
		    margin: 20px 0;
		    padding: 10px;
		    text-align: center;
		    background-color: #007BFF;
		    border: none;
		}
		
		.card button {
		    background-color: #007BFF;
		    color: #fff;
		    font-size: 18px;
		    border: none;
		}
		
		.card button:hover {
		    background-color: #0056b3;
		}
		
	</style>
</head>
<body>
	<jsp:include page="buyer_header.jsp" />
	
	<div class="container mt-5 mb-5">
	    <div class="d-flex justify-content-center row">
	        <div class="col-md-10">
	        
	        
				<h1 style="text-align: center;"><b>CART</b></h1>

				<br><br>	        	
	        
	            
                <%
				    ArrayList<CartProduct> products = (ArrayList<CartProduct>)request.getAttribute("products");
						if (products != null && !products.isEmpty()) {
							double total_price = 0;
							for (int i=0;i<products.size();i++) {
					        	CartProduct product = products.get(i);
				%>
				<div class="row p-2 bg-white border rounded">
				    <div class="col-md-3 mt-1">
				        <img class="img-fluid img-responsive rounded product-image" src="<%= product.getImageUrl() %>">
				    </div>
				    <div class="col-md-6 mt-1">
				        <h5><%= product.getName() %></h5>
				        <hr>
				        <p class="text-justify text-truncate para mb-0">
				            <%= product.getDescription() %><br><br>
				        </p>
				    </div>
				    <div class="align-items-center align-content-center col-md-3 border-left mt-1">
				        <div class="d-flex flex-row align-items-center">
				            <h4 class="mr-1">Price: $<%= product.getPrice() %></h4>
				        </div>
				        <form>
				            <div class="d-flex flex-row align-items-center">
					            <h4 class="mr-1">Quantity:<%= product.getCartQuantity()%> </h4>
					        </div>
					        
					        <div class="d-flex flex-row align-items-center">
					        	<%
								    double totalPrice = product.getPrice() * product.getCartQuantity();
								    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
								    String formattedTotalPrice = df.format(totalPrice);
								    total_price += totalPrice;
								    
								%>
		                        <h4 class="mr-1">Total Price: $<%= formattedTotalPrice %> </h4>
		                        
		                        
		                    </div>
					        
				            &nbsp;&nbsp;<a href="DeleteCartItemServlet?id=<%=product.getId()%>">
						          <span class="glyphicon glyphicon-trash"></span>
						        </a>
				        </form>
				        <br>
				        
				        
				    </div>
				</div><br>
				
				
				<%
				        }
						%>
						<br><br><hr>
						<div class="total">
								<%
								    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
								    String formattedTotalPrice = df.format(total_price);
								    
								%>
						    <h3>Total Price: $<%= formattedTotalPrice %></h3>
						</div><hr>
						
						<form action="CartTransaction" method="post">
							<div class="card">
					          <div class="card-body">
					            <button type="submit" class="btn btn-warning btn-block btn-lg">Proceed to Pay</button>
					          </div>
						    </div>
							<hr>
						</form>
						
						<%
					}
					else {
				%>
						<h1 style="text-align: center;">No Products In The Cart</h1>
				<%
					}
				%>

	            
	        </div>
	    </div>
	</div>
	
	

	<jsp:include page="footer.jsp" />
	
</body>
</html>