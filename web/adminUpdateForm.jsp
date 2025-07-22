<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.ProductDTO" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật sản phẩm</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 20px auto;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background: #f9f9f9;
        }
        .form-container label {
            display: block;
            margin-top: 10px;
        }
        .form-container input[type="text"],
        .form-container textarea {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            box-sizing: border-box;
        }
        .form-container button {
            margin-top: 15px;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .admin-error { color: red; }
        .admin-msg { color: green; }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    <div class="zone-content">
        <h2>Cập nhật sản phẩm</h2>
        <!-- Thông báo lỗi/ thành công -->
        <% if (request.getAttribute("ADMIN_ERROR") != null) { %>
            <p class="admin-error"><%= request.getAttribute("ADMIN_ERROR") %></p>
        <% } %>
        <% if (request.getAttribute("ADMIN_MSG") != null) { %>
            <p class="admin-msg"><%= request.getAttribute("ADMIN_MSG") %></p>
        <% } %>
        
        <div class="form-container">
            <%
                // Lấy đối tượng ProductDTO từ attribute "PRODUCT"
                ProductDTO product = (ProductDTO) request.getAttribute("PRODUCT");
                if (product != null) {
            %>
            <form action="MainController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="adminUpdateProduct">
                <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                
                <label>Tên sản phẩm:</label>
                <input type="text" name="productName" value="<%= product.getProductName() %>">
                
                <label>Brand:</label>
                <input type="text" name="brand" value="<%= product.getBrand() %>">
                
                <label>Category:</label>
                <input type="text" name="category" value="<%= product.getCategory() %>">
                
                <label>Price:</label>
                <input type="text" name="price" value="<%= product.getPrice() %>">
                
                <label>Size (NVARCHAR):</label>
                <input type="text" name="size" value="<%= product.getSize() %>">
                
                <label>Quantity:</label>
                <input type="text" name="quantity" value="<%= product.getQuantity() %>">
                
                <label>Description:</label>
                <textarea name="description"><%= product.getDescription() %></textarea>
                
                <label>Upload hình mới (nếu muốn thay đổi):</label>
                <input type="file" name="imageFile">
                
                <button type="submit">Cập nhật sản phẩm</button>
            </form>
            <%
                } else {
            %>
                <p>Sản phẩm không tồn tại.</p>
            <%
                }
            %>
        </div>
    </div>
    <jsp:include page="/footer.jsp" />
</body>
</html>
