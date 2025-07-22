// roll.js
(function() {
    var nav = document.querySelector(".zone-nav");
    // Lấy vị trí ban đầu của thanh menu so với đầu trang
    var navOffsetTop = nav.offsetTop;
    var ticking = false;

    function onScroll() {
        // Nếu vị trí cuộn lớn hơn hoặc bằng vị trí ban đầu của nav,
        // thêm class sticky-nav để cố định nó
        if (window.pageYOffset >= navOffsetTop) {
            nav.classList.add("sticky-nav");
        } else {
            nav.classList.remove("sticky-nav");
        }
        ticking = false;
    }

    window.addEventListener("scroll", function() {
        if (!ticking) {
            window.requestAnimationFrame(onScroll);
            ticking = true;
        }
    });
})();
