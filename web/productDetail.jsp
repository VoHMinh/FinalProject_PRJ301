<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sản phẩm</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/product_detail.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="zone-content">
        <div class="product-detail-container">
            <!-- Hiển thị thông báo từ giỏ hàng nếu có -->
            <c:if test="${not empty CART_MSG}">
                <p style="color: green; font-weight: bold;">${CART_MSG}</p>
            </c:if>
            <%
                dto.ProductDTO product = (dto.ProductDTO) request.getAttribute("PRODUCT");
                if (product != null) {
                    String imageData = product.getImageBase64();
                    boolean hasImage = (imageData != null && !imageData.trim().isEmpty());
                    String price = product.getPrice();
                    if (price == null || price.trim().isEmpty()) {
                        price = "Liên hệ";
                    }
                    int quantity = product.getQuantity();
                    String availableSizes = product.getSize();
                    String category = product.getCategory();
            %>
                <div class="product-layout">
                    <div class="product-image">
                        <% if (hasImage) { %>
                            <img src="data:image/png;base64,<%= imageData %>" alt="Sản phẩm" class="img-responsive">
                        <% } else { %>
                            <img src="assets/images/default.png" alt="Ảnh mặc định" class="img-responsive">
                        <% } %>
                    </div>
                    <div class="product-info">
                        <h2 class="product-name"><%= product.getProductName() %></h2>
                        <p class="brand"><strong>Thương hiệu:</strong> <%= product.getBrand() %></p>
                        <p class="price"><strong>Giá:</strong> <%= price %></p>
                        <p class="description"><strong>Mô tả:</strong> <%= product.getDescription() %></p>
                        <% if (quantity < 10) { %>
                            <p class="warning" style="color: orange;">Sắp bán hết!</p>
                        <% } %>
                        <!-- Đưa các input cần thiết vào form -->
                        <div class="add-to-cart">
                            <form action="MainController" method="post">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                                <div class="quantity-selector">
                                    <label for="quantity">Số lượng:</label>
                                    <input type="number" id="quantity" name="quantity" min="1" value="1">
                                </div>
                                <div class="size-selector">
                                    <strong>Chọn kích thước:</strong>
                                    <div class="size-options">
                                        <% 
                                            if (category.equalsIgnoreCase("Shoes")) {
                                                for (int i = 39; i <= 44; i++) {
                                                    String size = String.valueOf(i);
                                                    boolean available = availableSizes.contains(size);
                                        %>
                                            <span class="size-option <%= available ? "available" : "unavailable" %>"><%= size %></span>
                                        <%
                                                }
                                            } else if (category.equalsIgnoreCase("Clothes")) {
                                                String[] clothingSizes = {"S", "M", "L", "XL"};
                                                for (String size : clothingSizes) {
                                                    boolean available = availableSizes.contains(size);
                                                    if (available) {
                                        %>
                                            <span class="size-option available"><%= size %></span>
                                        <%
                                                    } else {
                                        %>
                                            <span class="size-option unavailable"><%= size %></span>
                                        <%
                                                    }
                                                }
                                            }
                                        %>
                                    </div>
                                </div>
                                <button type="submit" class="btn-add-to-cart">Thêm vào giỏ hàng</button>
                            </form>
                        </div>
                    </div>
                </div>
            <%
                } else {
            %>
                <p style="color: red;">Sản phẩm không tồn tại.</p>
            <%
                }
            %>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>
