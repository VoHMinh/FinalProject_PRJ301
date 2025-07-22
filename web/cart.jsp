<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.CartItemDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng</title>
    <!-- style.css của bạn (header, footer) -->
    <link rel="stylesheet" href="assets/css/style.css">
    <!-- cart.css mới, chứa class .cart-container -->
    <link rel="stylesheet" href="assets/css/cart.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <!-- Bọc toàn bộ giỏ hàng trong .cart-container -->
    <div class="cart-container">
        <h2>Giỏ hàng</h2>

        <%
            String cartMsg = (String) request.getAttribute("CART_MSG");
            String checkoutMsg = (String) request.getAttribute("CHECKOUT_MSG");
            Boolean checkoutError = (Boolean) request.getAttribute("CHECKOUT_ERROR");
            if (checkoutError == null) {
                checkoutError = false;
            }
            List<CartItemDTO> cartItems = (List<CartItemDTO>) request.getAttribute("CART_ITEMS");
        %>

        <% if (cartMsg != null && !cartMsg.trim().isEmpty()) { %>
            <p style="color: green; font-weight: bold;"><%= cartMsg %></p>
        <% } %>

        <% if (checkoutMsg != null && !checkoutMsg.trim().isEmpty()) {
            if (checkoutError) { %>
                <p style="color: red; font-weight: bold;"><%= checkoutMsg %></p>
        <%    } else { %>
                <p style="color: green; font-weight: bold;"><%= checkoutMsg %></p>
        <%    }
        } %>

        <% if (cartItems != null && !cartItems.isEmpty()) { %>
            <table>
                <tr>
                    <th>STT</th>
                    <th>Sản phẩm</th>
                    <th>Hình ảnh</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Hành động</th>
                </tr>
                <%
                    int index = 1;
                    for (CartItemDTO item : cartItems) {
                %>
                <tr>
                    <td><%= index++ %></td>
                    <td><%= item.getProductName() %> - <%= item.getBrand() %></td>
                    <td>
                        <%
                            String base64Image = item.getImageBase64();
                            if (base64Image != null && !base64Image.trim().isEmpty()) {
                        %>
                            <img src="data:image/png;base64,<%= base64Image %>" alt="<%= item.getProductName() %>">
                        <%
                            }
                        %>
                    </td>
                    <td><%= item.getPrice() %></td>
                    <td><%= item.getQuantity() %></td>
                    <td>
                        <form action="MainController" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="deleteFromCart">
                            <input type="hidden" name="productId" value="<%= item.getProductId() %>">
                            <button type="submit">Xóa</button>
                        </form>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
            <br>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="checkout">
                <button type="submit">Thanh toán</button>
            </form>
        <% } else { %>
            <p>Giỏ hàng của bạn đang trống.</p>
        <% } %>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
