<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.ProductDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chọn sản phẩm để cập nhật</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        table.product-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table.product-table th, table.product-table td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        table.product-table th {
            background-color: #f2f2f2;
        }
        a.product-link {
            color: #4CAF50;
            text-decoration: none;
            font-weight: bold;
        }
        a.product-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    <div class="zone-content">
        <h2>Chọn sản phẩm cần cập nhật</h2>
        <%
            List<ProductDTO> productList = (List<ProductDTO>) request.getAttribute("PRODUCT_LIST");
            if (productList != null && !productList.isEmpty()) {
        %>
        <table class="product-table" >
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sản phẩm</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (ProductDTO p : productList) {
                %>
                <tr>
                    <td><%= p.getProductId() %></td>
                    <td><%= p.getProductName() %></td>
                    <td>
                        <a class="product-link" href="MainController?action=loadProductForUpdate&productId=<%= p.getProductId() %>">
                            Cập nhật
                        </a>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
            } else {
        %>
            <p>Không có sản phẩm nào để cập nhật.</p>
        <%
            }
        %>
    </div>
    <jsp:include page="/footer.jsp" />
</body>
</html>
