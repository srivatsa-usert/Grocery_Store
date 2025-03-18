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
                <li class="active"><a href="ProductServlet?filterNumber=10">Home</a></li>
            </ul>
            <form class="navbar-form navbar-left" action="ProductServlet?filterNumber=10" method="get">
                <div class="input-group">
                    <input type="hidden" name="filterNumber" value="10">
                    <input type="text" class="form-control" placeholder="Search" name="search">
                    <div class="input-group-btn">
                        <button class="btn btn-default" type="submit">
                            <i class="glyphicon glyphicon-search"></i>
                        </button>
                    </div>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <!-- <li><form id="cartForm" action="cart" method="post" style="display: none;"></form></li> -->
                <li><a href="cart" id="cartLink"><span class="glyphicon glyphicon-shopping-cart"></span> Cart</a></li>
                <li><form id="logoutForm" action="logout" method="post" style="display: none;"></form></li>
                <li><a href="#" id="logoutLink"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
            </ul>
        </div>
    </nav>

    <script>
        $(document).ready(function () {
            var modal = $('#id01');

            $(document).on("click", function(event) {
                if (event.target === modal[0]) {
                    modal.hide();
                }
            });

            function logout() {
                $('#logoutForm').submit();
            }

            function cart() {
                $('#cartForm').submit();
            }

            $('#logoutLink').click(logout);
            $('#cartLink').click(cart);
        });
    </script>
</body>
</html>
