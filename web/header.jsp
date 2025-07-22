<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
    <meta charset="UTF-8">
    <title>Trang bất kỳ</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/roll.css">
</head>
<!-- Topbar -->
<div class="zone-topbar">
    <div class="zone-topbar-container">
        <p>ZONESPORT - SINCE 2016 - CAM KẾT HÀNG CHÍNH HÃNG</p>
    </div>
</div>

<!-- Header chính -->
<div class="zone-header">
    <div class="zone-header-container">
        <div class="zone-logo">
            <a href="index.jsp">
                <img src="assets/images/logo.png" alt="ZoneSport">
            </a>
        </div>
        <div class="zone-search">
            <form action="MainController" method="get">
                <input type="hidden" name="action" value="viewProducts">
                <input type="text" name="search" placeholder="Tìm kiếm sản phẩm...">
                <button type="submit">
                    <img src="assets/images/search-icon.png" alt="Search">
                </button>
            </form>
        </div>
        <!-- Container nhóm User và Cart -->
        <div class="zone-user-cart" style="display: flex; align-items: center; gap: 15px;">
            <!-- Phần User -->
            <div class="zone-user" onclick="toggleUserDropdown(event)">
                <div class="user-account">
                    <%
                        dto.UserDTO user = (dto.UserDTO) session.getAttribute("USER");
                        if (user != null) {
                            out.print(user.getUsername());
                        } else {
                    %>
                    <img src="assets/images/user-icon.png" alt="User" class="user-icon">
                    <%
                        }
                    %>
                </div>
                
                <div id="userDropdown" class="user-dropdown" onclick="event.stopPropagation()" style="display: none;">
                    <%
                        if (session.getAttribute("USER") == null) {
                            String loginError = (String) request.getAttribute("LOGIN_ERROR");
                            if (loginError != null) {
                    %>
                    <p class="login-error"><%= loginError %></p>
                    <%
                            }
                    %>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="login">
                        <label for="login-username">Username</label>
                        <input type="text" id="login-username" name="username" placeholder="Nhập username" required>
                        <label for="login-password">Mật khẩu</label>
                        <input type="password" id="login-password" name="password" placeholder="Nhập mật khẩu" required>
                        <button type="submit" class="login-submit-btn">Đăng nhập</button>
                    </form>
                    <a class="register-link" href="register.jsp">Đăng ký</a>
                    <%
                        } else {
                            dto.UserDTO currentUser = (dto.UserDTO) session.getAttribute("USER");
                            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
                    %>
                    <div class="account-info">
                        <p>Chào, <strong><%= currentUser.getUsername() %></strong></p>
                        <%
                            if (isAdmin) {
                        %>
                        <a class="admin-link" href="adminAddProduct.jsp">Tạo sản phẩm mới</a><br>
                        <a class="admin-link" href="MainController?action=adminListProductsForUpdate">Cập nhật sản phẩm</a><br>
                        <%
                            }
                        %>
                        <a href="MainController?action=logout" class="logout-link">Đăng xuất</a>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
            <!-- Icon Giỏ hàng -->
            <div class="zone-cart">
                <a href="MainController?action=viewCart">
                    <img src="assets/images/cart-icon.png" alt="Giỏ hàng" class="cart-icon" style="width:110px;height:65px; margin-left: 20px">
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Thanh menu -->
<nav class="zone-nav">
    <ul class="zone-main-menu">
        <li class="zone-dropdown">
            <a href="#">THƯƠNG HIỆU <span class="caret"></span></a>
            <ul class="zone-dropdown-menu">
                <li><a href="MainController?action=viewProducts&brand=Asics">Asics</a></li>
                <li><a href="MainController?action=viewProducts&brand=Mizuno">Mizuno</a></li>
                <li><a href="MainController?action=viewProducts&brand=Beyono">Beyono</a></li>
            </ul>
        </li>
        <li class="zone-dropdown">
            <a href="MainController?action=viewProducts&category=Shoes">GIÀY BÓNG CHUYỀN <span class="caret"></span></a>
            <ul class="zone-dropdown-menu">
                <li><a href="MainController?action=viewProducts&brand=Asics&category=Shoes">Giày Asics</a></li>
                <li><a href="MainController?action=viewProducts&brand=Mizuno&category=Shoes">Giày Mizuno</a></li>
                <li><a href="MainController?action=viewProducts&brand=Beyono&category=Shoes">Giày Beyono</a></li>
            </ul>
        </li>
        <li class="zone-dropdown">
            <a href="MainController?action=viewProducts&category=Clothes">QUẦN ÁO BÓNG CHUYỀN <span class="caret"></span></a>
            <ul class="zone-dropdown-menu">
                <li><a href="MainController?action=viewProducts&brand=Asics&category=Clothes">Quần Áo Asics</a></li>
                <li><a href="MainController?action=viewProducts&brand=Mizuno&category=Clothes">Quần Áo Mizuno</a></li>
                <li><a href="MainController?action=viewProducts&brand=Beyono&category=Clothes">Quần Áo Beyono</a></li>
            </ul>
        </li>
        <li><a href="MainController?action=viewFlashSale">FLASHSALE</a></li>
        <li><a href="MainController?action=viewBlog">BLOG</a></li>
    </ul>
</nav>

<script src="assets/js/roll.js"></script> <!-- Thêm file roll.js -->
<script>
    function toggleUserDropdown(e) {
        e.stopPropagation();
        const dropdown = document.getElementById('userDropdown');
        if (dropdown.style.display === 'block') {
            dropdown.style.display = 'none';
        } else {
            dropdown.style.display = 'block';
        }
    }
    window.addEventListener('click', function (e) {
        const dropdown = document.getElementById('userDropdown');
        const zoneUser = document.querySelector('.zone-user');
        if (!zoneUser.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });
</script>
