<!DOCTYPE html>
<html lang="en">
<head>
    <title>Index</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</head>
<body>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">WebSiteName</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="index.jsp">Home</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#" data-toggle="modal" data-target="#signupModal"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                <li><a href="#" data-toggle="modal" data-target="#loginModal"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            </ul>
        </div>
    </nav>

    <!-- Login Modal -->
    <div id="loginModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Login</h4>
                </div>
                <form action="loginServlet" method="post">
                    <div class="modal-body text-center">
                        <input type="text" placeholder="Username" name="login_username"><br><br>
                        <input type="password" placeholder="Password" id="login_password" name="login_password"><br><br>
                        <input type="checkbox" id="showPassword">Show Password
                    </div>
                    <div class="form-group text-center">
                        <label>I am a:</label><br>
                        <input type="radio" name="login_userType" value="Seller" id="login_sellerRadio" required>
                        <label for="login_sellerRadio">Seller</label>&nbsp;&nbsp;
                        <input type="radio" name="login_userType" value="Buyer" id="login_buyerRadio" required>
                        <label for="login_buyerRadio">Buyer</label>
                        <input type="radio" name="login_userType" value="Amazon" id="login_amazonRadio" required>
                        <label for="login_amazonRadio">Amazon</label>
                    </div>
                    <div class="modal-footer text-center">
                        <button type="submit" class="btn btn-primary">Login</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Signup Modal -->
    <div id="signupModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Sign Up</h4>
                </div>
                <form action="signUpServlet" method="post" onsubmit="return validateSignUpForm()">
                    <div class="modal-body text-center">
                        <input type="text" placeholder="Username" name="signup_username"><br><br>
                        <input type="text" placeholder="Password" name="signup_password"><br><br>
                        <input type="email" placeholder="user@email.com" name="signup_email"><br><br>
                        <textarea placeholder="address" name="signup_address"></textarea>
                    </div>
                    <div class="form-group text-center">
                        <label>I am a:</label><br>
                        <input type="radio" name="signup_userType" value="Seller" id="signup_sellerRadio" required>
                        <label for="signup_sellerRadio">Seller</label>&nbsp;&nbsp;
                        <input type="radio" name="signup_userType" value="Buyer" id="signup_buyerRadio" required>
                        <label for="signup_buyerRadio">Buyer</label>
                    </div>
                    <div class="modal-body text-center">
                        <input type="text" placeholder="Shop Name" name="signup_shopname" id="shopNameField" style="display: none;">
                    </div>
                    <div class="modal-footer text-center">
                        <button type="submit" class="btn btn-primary">Sign Up</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            // Show/hide password functionality
            $("#showPassword").on("change", function () {
                var passwordInput = $("#login_password");
                if (this.checked) {
                    passwordInput.attr("type", "text");
                } else {
                    passwordInput.attr("type", "password");
                }
            });

            // Login form validation
            $("form[action='loginServlet']").on("submit", function (e) {
                var username = $("input[name='login_username']").val();
                var password = $("input[name='login_password']").val();

                if (username === "" || password === "") {
                    alert("Please fill in both Username and Password fields.");
                    e.preventDefault(); // Prevent form submission
                }
            });

            // Seller/Buyer radio button behavior
            var sellerRadio = $("#signup_sellerRadio");
            var shopNameField = $("#shopNameField");
            var buyerRadio = $("#signup_buyerRadio");

            sellerRadio.on("change", function () {
                if (sellerRadio.is(":checked") && sellerRadio.val() === "Seller") {
                    shopNameField.show();
                } else {
                    shopNameField.hide();
                }
            });

            buyerRadio.on("change", function () {
                shopNameField.hide();
            });

            // Validate Sign Up Form
            $("form[action='signUpServlet']").on("submit", function (e) {
                var username = $("input[name='signup_username']").val();
                var password = $("input[name='signup_password']").val();
                var email = $("input[name='signup_email']").val();
                var userType = $("input[name='signup_userType']:checked");
                var shopNameField = $("#shopNameField");

                if (username === "" || password === "" || email === "") {
                    alert("Please fill in all the required fields.");
                    e.preventDefault();
                }

                if (userType.length === 0) {
                    alert("Please select a user type (Seller or Buyer).");
                    e.preventDefault(); 
                }

                if (userType.val() === "Seller" && shopNameField.is(":visible")) {
                    var shopName = $("input[name='signup_shopname']").val();
                    if (shopName === "") {
                        alert("Please enter a Shop Name.");
                        e.preventDefault(); 
                    }
                }
            });
        });
    </script>
</body>
</html>
