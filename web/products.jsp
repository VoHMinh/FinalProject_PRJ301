<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dto.ProductDTO" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách sản phẩm</title>
    <!-- File CSS chung -->
    <link rel="stylesheet" href="assets/css/style.css">
    <!-- File CSS riêng cho trang products -->
    <link rel="stylesheet" href="assets/css/products.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <%
        
        String brandParam = request.getParameter("brand");
        String categoryParam = request.getParameter("category");
        String title = "";
        String bannerImage = "assets/images/default-banner.png"; 

       
        if (categoryParam != null) {
            if (categoryParam.equalsIgnoreCase("Shoes")) {
                title = "GIÀY BÓNG CHUYỀN";
                bannerImage = "assets/images/banner-giay.png"; 
            } else if (categoryParam.equalsIgnoreCase("Clothes")) {
                title = "QUẦN ÁO THỂ THAO";
                bannerImage = "assets/images/banner-quanao.png"; 
            }
        }

        
        if (brandParam != null) {
            title = (title.isEmpty() ? "" : title + " ") + brandParam.toUpperCase();
        }
    %>
    <div class="banner-container">
        <img src="<%= bannerImage %>" alt="<%= title %> Banner" class="banner-image">
        <h2 class="brand-title"><%= title %></h2>
    </div>

    <div class="zone-content">
        <div class="product-list">
            <%
                List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("PRODUCT_LIST");
                if (products != null && !products.isEmpty()) {
                    for (ProductDTO p : products) {
                        String imageData = p.getImageBase64();
            %>
            <!-- Thẻ a bọc toàn bộ khung sản phẩm -->
            <a class="product-link" href="MainController?action=productDetail&productId=<%= p.getProductId() %>">
                <div class="product-item">
                    <%
                        // Nếu không có ảnh, dùng default.png
                        if (imageData == null || imageData.trim().isEmpty()) {
                    %>
                    <img src="assets/images/default.webp" alt="Default Image" class="product-img">
                    <%
                        } else {
                    %>
                    <img src="data:image/webp;base64,<%= imageData %>" alt="Product Image" class="product-img">
                    <%
                        }
                    %>
                    <h3><%= p.getProductName() %></h3>
                    <p>Price: <%= p.getPrice() %></p>
                </div>
            </a>
            <%
                    }
                } else {
            %>
            <p>Không có sản phẩm nào.</p>
            <%
                }
            %>
        </div>

        <!-- Phân trang -->
        <div class="pagination">
            <%
                int currentPage = 1;
                int totalPages = 1;
                if (request.getAttribute("pageIndex") != null) {
                    currentPage = (Integer) request.getAttribute("pageIndex");
                }
                if (request.getAttribute("totalPages") != null) {
                    totalPages = (Integer) request.getAttribute("totalPages");
                }

                // Tạo URL kèm brandParam và category nếu có
                String baseUrl = "MainController?action=viewProducts";
                if (brandParam != null && !brandParam.isEmpty()) {
                    baseUrl += "&brand=" + brandParam;
                }
                if (categoryParam != null && !categoryParam.isEmpty()) {
                    baseUrl += "&category=" + categoryParam;
                }

                if (currentPage > 1) {
            %>
            <a href="<%= baseUrl %>&page=<%= currentPage - 1 %>">&            <a href="<%= baseUrl %>&page=<%= currentPage - 1 %>">&laquo; Prev</a>
            <%
                }
                for (int i = 1; i <= totalPages; i++) {
                    if (i == currentPage) {
            %>
                <span class="current-page"><%= i %></span>
            <%
                    } else {
            %>
                <a href="<%= baseUrl %>&page=<%= i %>"><%= i %></a>
            <%
                    }
                }
                if (currentPage < totalPages) {
            %>
                <a href="<%= baseUrl %>&page=<%= currentPage + 1 %>">Next &raquo;</a>
            <%
                }
                // Nút "Last" để đi tới trang cuối
                if (currentPage < totalPages) {
            %>
                <a href="<%= baseUrl %>&page=<%= totalPages %>">Last &raquo;</a>
            <%
                }
            %>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>