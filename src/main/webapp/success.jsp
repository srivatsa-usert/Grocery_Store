<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <link href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="footer.css">
  </head>
      <meta charset="UTF-8">
	  <title>Success</title>
    <style>
    
      body {
        text-align: center;
        padding: 40px 0;
        background: #EBF0F5;
      }
      .checkmark {
        color: #9ABC66;
        font-size: 100px;
        line-height: 200px;
        margin-left:-15px;
      }
      .card {
        background: white;
        padding: 60px;
        border-radius: 4px;
        box-shadow: 0 2px 3px #C8D0D8;
        display: inline-block;
        margin: 0 auto;
      }
    </style>
    <body>
      <jsp:include page="buyer_header.jsp" />
      
      <div class="card">
      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
        <i class="checkmark">✓</i>
      </div>
        <h1 class="green">Success</h1> 
        <p class="pFont">We received your purchase request;<br/> Delivery on the way!</p>
        <a href="ProductServlet?filterNumber=10" class="green">Redirect to Home Page</a>
      </div>
      
      <jsp:include page="footer.jsp" />
    </body>
</html>