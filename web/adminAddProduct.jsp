<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo sản phẩm mới</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/addproduct.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <div class="container-addproduct">
        <h2>Tạo sản phẩm mới</h2>
        
        <!-- Hiển thị thông báo lỗi nếu có -->
        <% if (request.getAttribute("ADMIN_ERROR") != null) { %>
            <p class="admin-error"><%= request.getAttribute("ADMIN_ERROR") %></p>
        <% } %>
        
        <!-- Hiển thị thông báo thành công nếu có -->
        <% if (request.getAttribute("ADMIN_MSG") != null) { %>
            <p class="admin-msg"><%= request.getAttribute("ADMIN_MSG") %></p>
        <% } %>
        
        <form action="MainController" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="adminAddProduct">
            
            <label for="productName">Tên sản phẩm:</label>
            <input type="text" id="productName" name="productName" required>
            
            <label for="brand">Hãng:</label>
            <select id="brand" name="brand" required>
                <option value="Asics">Asics</option>
                <option value="Mizuno">Mizuno</option>
                <option value="Beyono">Beyono</option>
            </select>
            
            <label for="category">Loại:</label>
            <select id="category" name="category" required>
                <option value="Shoes">Giày</option>
                <option value="Clothes">Quần áo</option>
            </select>
            
            <label for="price">Giá:</label>
            <input type="text" id="price" name="price" required>
            
            <label for="size">Size:</label>
            <input type="text" id="size" name="size">
            
            <label for="quantity">Số lượng:</label>
            <input type="text" id="quantity" name="quantity" required>
            
            <label for="description">Mô tả:</label>
            <textarea id="description" name="description"></textarea>
            
            <label for="imageFile">Chọn ảnh sản phẩm:</label>
            <input type="file" id="imageFile" name="imageFile">
            
            <button type="submit">Tạo sản phẩm</button>
        </form>
    </div>
    
    <jsp:include page="footer.jsp" />
</body>
</html>