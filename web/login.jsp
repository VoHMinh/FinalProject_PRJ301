<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập tài khoản</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="login-container zonesport-login">
    <h2>ĐĂNG NHẬP TÀI KHOẢN</h2>
    <p>Nhập email và mật khẩu của bạn:</p>
    <%
        String error = (String) request.getAttribute("LOGIN_ERROR");
        if(error != null){
    %>
        <p class="error"><%= error %></p>
    <%
        }
    %>
    <form action="MainController" method="post">
        <input type="hidden" name="action" value="login">
        <label>Email</label>
        <input type="text" name="username" placeholder="Email" required />
        
        <label>Mật khẩu</label>
        <input type="password" name="password" placeholder="******" required />
        
        <!-- Bỏ Google, Facebook login -->
        
        <button type="submit" class="btn-login">ĐĂNG NHẬP</button>
        
        <div class="other-links">
            <a href="register.jsp">Tạo tài khoản</a> | 
            <a href="#">Khôi phục mật khẩu</a>
        </div>
    </form>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
