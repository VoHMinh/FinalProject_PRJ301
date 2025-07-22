<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>ZoneSport</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <!-- Header -->
    <jsp:include page="header.jsp" />

    <!-- Banner Slideshow -->
    <div class="banner-slideshow">
        <div class="slides">
            <img src="assets/images/banner1.png" class="slide" alt="Banner 1">
            <img src="assets/images/banner2.jpg" class="slide" alt="Banner 2">
            <img src="assets/images/banner3.png" class="slide" alt="Banner 3">
        </div>
        <!-- Nút chuyển slide -->
        <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
        <a class="next" onclick="plusSlides(1)">&#10095;</a>
        <!-- Chấm đánh dấu slide -->
        <div class="dot-container">
            <span class="dot" onclick="currentSlide(1)"></span>
            <span class="dot" onclick="currentSlide(2)"></span>
            <span class="dot" onclick="currentSlide(3)"></span>
        </div>
    </div>

    <!-- Container: Sản Phẩm Hot -->
    <div class="container">
        <h2>Sản Phẩm Hot</h2>
        <div class="featured-categories">
            <!-- Sản phẩm 1 -->
            <div class="cat-item">
                <img src="assets/images/metarise.png" alt="ASICS METARISE">
                <div class="cat-info">
                    <h3>ASICS METARISE</h3>
                    <a href="MainController?action=productDetail&productId=13" class="btn-view">Xem sản phẩm</a>
                </div>
            </div>
            <!-- Sản phẩm 2 -->
            <div class="cat-item">
                <img src="assets/images/Sky-ff3.png" alt="ASICS SKY ELITE FF MT 3">
                <div class="cat-info">
                    <h3>ASICS SKY ELITE FF MT 3</h3>
                    <a href="MainController?action=productDetail&productId=52" class="btn-view">Xem sản phẩm</a>
                </div>
            </div>
            <!-- Sản phẩm 3 -->
            <div class="cat-item">
                <img src="assets/images/mizuno-mid.png" alt="MIZUNO WAVE MOMENTUM 3 MID">
                <div class="cat-info">
                    <h3>MIZUNO WAVE MOMENTUM 3 MID</h3>
                    <a href="MainController?action=productDetail&productId=51" class="btn-view">Xem sản phẩm</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="footer.jsp" />

    <!-- JS cho slideshow (nếu có) -->
    <script src="assets/js/slideshow.js"></script>
</body>
</html>
