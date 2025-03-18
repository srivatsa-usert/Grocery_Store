<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Index</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="modal.css">
    <link rel="stylesheet" href="footer.css">
    
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    
    <style>
        /* CSS for the dialog box */
        .dialog-box {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #fff;
            padding: 30px; /* Increased padding to make it larger */
            width: 300px; /* Increased width */
            height: 150px; /* Increased height */
            border: 1px solid #ccc;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        /* CSS for the container with background image */
        body {
            background-image: url('images/bakeryproducts.jpg'); /* Adjust the image path */
            background-size: cover;
            background-repeat: no-repeat;
            background-position: center;
        }
        
        .footer {
            background-color: #f5f5f5;
            padding: 20px 0;
            position: absolute;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>
    <jsp:include page="index_header.jsp" />
    
    <% 
    String errorMessage = (String) session.getAttribute("loginError");
    if (errorMessage != null) {
    %>
    <div class="alert alert-danger">
        <%= errorMessage %>
    </div>
    <% } %>
    
    <jsp:include page="footer.jsp" />
    
    <script>
        $(document).ready(function() {
            function showDialog() {
                $('#custom-dialog').show();
            }

            function closeDialog() {
                $('#custom-dialog').hide();
            }

            $('#show-dialog-button').on('click', showDialog);
            $('#close-dialog-button').on('click', closeDialog);
        });
    </script>
</body>
</html>
