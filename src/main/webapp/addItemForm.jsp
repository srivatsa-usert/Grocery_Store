<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Item</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="footer.css">
</head>
<body>
    <jsp:include page="seller_header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
            <div class="col-md-6">
                <h2>Add New Item</h2>
                <form action="AddItemServlet" method="post" enctype="multipart/form-data" id="add-item-form">
                    <div class="form-group">
                        <label for="name">Item Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea id="description" name="description" class="form-control" required></textarea>
                    </div>
                    <div class="form-group">
                        <label for="price">Price:</label>
                        <input id="price" name="price" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="quantity">Quantity:</label>
                        <input type="number" id="quantity" name="quantity" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="image">Image:</label>
                        <input type="file" id="image" name="image" class="form-control-file" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Item</button>
                </form>
            </div>
        </div>
    </div>

    <script>
    $(document).ready(function () {
        $("#add-item-form").submit(function () {
            var name = $("#name").val();
            var description = $("#description").val();
            var price = $("#price").val();
            var quantity = $("#quantity").val();
            var image = $("#image").val();

            if (name.trim() === "" || description.trim() === "" || price.trim() === "" || quantity.trim() === "" || image.trim() === "") {
                alert("All fields are required. Please fill out all fields.");
                return false;
            }

            if (!/^\d+(\.\d{1,2})?$/.test(price)) {
                alert("Price must be a numeric value with up to two decimal places.");
                return false;
            }
            return true;
        });
    });
    </script>

    <jsp:include page="footer.jsp" />
</body>
</html>
