<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, item.Product" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generate Report</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="footer.css">
</head>
<body>
    <jsp:include page="seller_header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center">
            <form action="ReportServlet" method="post">
                <h1>Select Report Type</h1>
                <label for="reportType">Report Type:</label>
                <select name="reportType" id="reportType" required>
                    <option value="user">Generate by User</option>
                    <option value="date">Generate by Date</option>
                </select>

                <button type="button" class="btn btn-primary" id="showFormButton">Show Form</button><br><hr>

                <!-- Additional form elements go here (hidden by default) -->
                <div id="generateUserForm" style="display: none;">
                    <!-- Form elements for generating reports by user -->
                    <label for="username">User:</label>
                    <select name="selectedUser" id="selectedUser">
                        <option value="">Select a buyer</option>
                        <c:forEach var="buyerName" items="${buyerNames}">
                            <option value="${buyerName}"><c:out value="${buyerName}" /></option>
                        </c:forEach>
                    </select><br><br>
                </div>

                <div id="generateDateForm" style="display: none;">
                    <!-- Form elements for generating reports by date -->
                    <label for="startDate">Start Date:</label>
                    <input type="date" name="startDate" id="startDate">
                    <br>
                    <label for="endDate">End Date:</label>
                    <input type="date" name="endDate" id="endDate"><br><br>
                </div>

                <button type="submit" id="submitButton" class="btn btn-primary" style="display: none;">Generate Report</button>
            </form>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            const reportTypeSelect = $("#reportType");
            const showFormButton = $("#showFormButton");
            const generateUserForm = $("#generateUserForm");
            const generateDateForm = $("#generateDateForm");
            const submitButton = $("#submitButton");

            showFormButton.click(function () {
                if (reportTypeSelect.val() === "user") {
                    generateUserForm.show();
                    generateDateForm.hide();
                    submitButton.show();
                } else if (reportTypeSelect.val() === "date") {
                    generateDateForm.show();
                    generateUserForm.hide();
                    submitButton.show();
                }
            });

            submitButton.click(function (e) {
                if (!handleSubmit()) {
                    e.preventDefault();
                }
            });
        });
    </script>

    <script>
        function validateUserForm() {
            var selectedUser = $("#selectedUser");
            if (selectedUser.val() === "") {
                alert("Please select a buyer.");
                return false;
            }
            return true;
        }

        function validateDateForm() {
            var startDate = $("#startDate");
            var endDate = $("#endDate");
            var currentDate = new Date();

            if (startDate.val() === "") {
                alert("Please select a start date.");
                return false;
            }
            if (endDate.val() === "") {
                alert("Please select an end date.");
                return false;
            }

            var selectedStartDate = new Date(startDate.val());
            var selectedEndDate = new Date(endDate.val());

            if (selectedStartDate > currentDate) {
                alert("Start date cannot be in the future.");
                return false;
            }
            if (selectedEndDate > currentDate) {
                alert("End date cannot be in the future.");
                return false;
            }
            if (selectedStartDate > selectedEndDate) {
                alert("Start date should be before or equal to the end date.");
                return false;
            }

            return true;
        }

        function handleSubmit() {
            var reportType = $("#reportType").val();
            if (reportType === "user") {
                return validateUserForm();
            } else if (reportType === "date") {
                return validateDateForm();
            }
            return false;
        }
    </script>

    <jsp:include page="footer.jsp" />
</body>
</html>
