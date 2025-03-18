<!DOCTYPE html>
<html lang="en">
<head>
    <title>Seller</title>
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
                <li class="active"><a href="SellerServlet">Home</a></li>
            </ul>
            <button class="btn btn-danger navbar-btn" onclick="location.href='addItemForm.jsp'">Add Items</button>
            <button class="btn btn-danger navbar-btn" onclick="location.href='GenerateReportServlet'">Generate Reports</button>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form id="logoutForm" action="logout" method="post" style="display: none;"></form>
                </li>
                <li><a href="#" id="logoutLink"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
            </ul>
        </div>
    </nav>

    <script>
        $(document).ready(function () {
            $(document).click(function (event) {
                if (event.target == $("#id01")[0]) {
                    $("#id01").css("display", "none");
                }
            });

            $("#logoutLink").click(function () {
                $("#logoutForm").submit();
            });
        });
    </script>
</body>
</html>
