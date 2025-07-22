<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="assets/css/register.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <script>
        // Kiểm tra khớp mật khẩu trước khi submit form
        function validateForm() {
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirmPassword").value;
            if (password !== confirmPassword) {
                alert("Mật khẩu và xác nhận mật khẩu không khớp! Vui lòng nhập lại.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="register-container">
        <h2>Register</h2>
        <%
            String msg = (String) request.getAttribute("REGISTER_MSG");
            if(msg != null){
        %>
            <p class="error"><%= msg %></p>
        <%
            }
        %>
        <form action="MainController" method="post" onsubmit="return validateForm();">
            <input type="hidden" name="action" value="register">
            <p>Username: <input type="text" name="username" required></p>
            <p>Password: <input type="password" id="password" name="password" required></p>
            <p>Confirm Password: <input type="password" id="confirmPassword" name="confirmPassword" required></p>
            <p>Full Name: <input type="text" name="fullName" required></p>
            <p>Email: <input type="email" name="email" required></p>
            <p>Phone: <input type="text" name="phone"></p>
            <p>Address: <input type="text" name="address"></p>
            <p><input type="submit" value="Register"></p>
        </form>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
