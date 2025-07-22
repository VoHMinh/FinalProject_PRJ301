<%@ page import="dto.ProductDTO" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Product Management</title>
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body>
<jsp:include page="../header.jsp" />
<div class="container">
    <h2>Manage Products</h2>
    <%
        String msg = (String) request.getAttribute("ADMIN_MSG");
        if(msg != null){
    %>
        <p class="error"><%= msg %></p>
    <%
        }
    %>
    <!-- Form thêm s?n ph?m -->
    <form action="../MainController" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="adminAddProduct">
        <p>Product Name: <input type="text" name="productName" required></p>
        <p>Brand:
            <select name="brand">
                <option value="Asics">Asics</option>
                <option value="Mizuno">Mizuno</option>
                <option value="Beyono">Beyono</option>
            </select>
        </p>
        <p>Category:
            <select name="category">
                <option value="Shoes">Shoes</option>
                <option value="Clothes">Clothes</option>
            </select>
        </p>
        <p>Price: <input type="number" name="price" step="0.01" required></p>
        <p>Size: <input type="text" name="size"></p>
        <p>Quantity: <input type="number" name="quantity" required></p>
        <p>Description: <textarea name="description"></textarea></p>
        <p>Image: <input type="file" name="imageFile"></p>
        <p><input type="submit" value="Add Product"></p>
    </form>
    <!-- Danh sách s?n ph?m c?a admin (có th? hi?n th? và cho nút Edit/Delete) -->
</div>
<jsp:include page="../footer.jsp" />
</body>
</html>
