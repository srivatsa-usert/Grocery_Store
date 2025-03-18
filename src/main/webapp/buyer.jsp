<%@ page import="java.util.*" %>
<%@ page import="item.Product" %>
<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Buyer</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="modal.css">
    <link rel="stylesheet" href="footer.css">
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    
    <style>
        .pagination-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>
    <jsp:include page="buyer_header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
            <div class="col-md-10">
                <div class="pagination-container">
                    <form id="myForm" method="get">
                        <label for="itemsPerPage">Items Per Page:</label>
                        <input type="number" id="itemsPerPage" name="filterNumber" min="1" value="<%= request.getParameter("filterNumber") %>" onchange="submitForm()">
                    </form>
                    <button id="prevPage" style="display: none;"><span class="glyphicon glyphicon-menu-left"></span> Previous Page</button>
                    <button id="nextPage" style="display: none;">Next Page <span class="glyphicon glyphicon-menu-right"></span></button>
                </div>
                <br><br>

                <!-- Loop through the products and generate content for each product -->
                <%
                    ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");
                    if (products != null && !products.isEmpty()) {
                        for (int i = 0; i < products.size(); i++) {
                            Product product = products.get(i);
                %>
                <div class="row p-2 bg-white border rounded">
                    <div class="col-md-3 mt-1 product-image-container" data-product-id="<%= product.getId() %>">
					    <img class="img-fluid img-responsive rounded product-image" src="placeholder-image.jpg" alt="Product Image">
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
                            <h4 class="mr-1">$<%= product.getPrice() %></h4>
                            <h5 class="mr-1">In Stock: <%=product.getQuantity() %></h5>
                        </div>
                        <form action="addCart" method="post" onsubmit="return validateQuantity()">
                            <label for="quantity">Quantity:</label>
                            <input type="number" class="quantity-input" id="quantity" name="quantity" min="1" max="<%= product.getQuantity() %>" style="width: 100%;" value="1">
                            <span id="quantityError" class="text-danger"></span>
                            <br><br>
                            <input type="hidden" class="item-id" id="item_id" name="item_id" value="<%= product.getId() %>">
                            <input type="submit" value="ADD TO CART">
                        </form>
                        <br>
                    </div>
                </div>
                <br>
                <%
                        }
                    } else {
                %>
                <h1 style="text-align: center;">No Products Available. Login to view products</h1>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            function submitForm() {
                $("#myForm").submit();
            }

            function navigateToPage(page) {
                var itemsPerPage = $("#itemsPerPage").val();
                var url = "ProductServlet?filterNumber=" + itemsPerPage + "&page=" + page;
                window.location.href = url;
            }

            var currentPageParam = new URLSearchParams(window.location.search).get("page");
            var currentPage = currentPageParam ? parseInt(currentPageParam) : 1;

            var totalPages = <%= request.getAttribute("totalPages") %>;

            var prevButton = $("#prevPage");
            var nextButton = $("#nextPage");
            if (currentPage > 1) {
                prevButton.show();
            }
            if (currentPage < totalPages) {
                nextButton.show();
            }

            prevButton.click(function () {
                navigateToPage(currentPage - 1);
            });

            nextButton.click(function () {
                navigateToPage(currentPage + 1);
            });
        });
    </script>
    
    <script>
        function validateQuantity() {
            var quantityInputs = $(".quantity-input");
            for (var i = 0; i < quantityInputs.length; i++) {
                var quantityInput = quantityInputs[i];
                var enteredValue = quantityInput.value.trim();

                if (enteredValue === "") {
                    alert("Enter a valid number");
                    return false;
                }
            }

            return true; 
        }
    </script>

    <script>
        function validateItemsPerPage() {
            var itemsPerPageInput = $("#itemsPerPage");
            if (isNaN(itemsPerPageInput.val()) || itemsPerPageInput.val() < 1) {
                alert("Enter a valid number");
                return false; 
            } else {
                return true; 
            }
        }
    </script>
    <script>
	    $(document).ready(function () {
	
	        function loadProductImage(productId) {
	            $.ajax({
	                url: "ProductImageServlet?productId=" + productId,
	                method: "GET",
	                success: function (data) {
	                    var imageContainer = $(".product-image-container[data-product-id='" + productId + "']");
	                    imageContainer.find(".product-image").attr("src", data);
	                },
	                error: function () {
	                }
	            });
	        }
	
	        $(".product-image-container").each(function () {
	            var productId = $(this).data("product-id");
	            loadProductImage(productId);
	        });
	    });
	</script>

    
    <jsp:include page="footer.jsp" />
</body>
</html>
