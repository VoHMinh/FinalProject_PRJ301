<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify Account</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
    <%
        String code = request.getParameter("code");
        if(code != null){
            // Đã xử lý xác thực trong MainController?action=verify
            String msg = (String) request.getAttribute("VERIFY_MSG");
            if(msg != null){
    %>
                <h2><%= msg %></h2>
    <%
            }
        } else {
    %>
            <h2>Invalid verification request.</h2>
    <%
        }
    %>
    <a href="login.jsp">Go to Login</a>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
