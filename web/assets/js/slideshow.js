document.addEventListener("DOMContentLoaded", function () {
    let slideIndex = 0;
    let slides = document.getElementsByClassName("slide");
    let dots = document.getElementsByClassName("dot");
    let slideTimer;

    function showSlides() {
        for (let i = 0; i < slides.length; i++) {
            slides[i].classList.remove("active");
        }
        slideIndex++;
        if (slideIndex > slides.length) { slideIndex = 1; }
        slides[slideIndex - 1].classList.add("active");
        updateDots(slideIndex - 1);
        slideTimer = setTimeout(showSlides, 5000);
    }

    function updateDots(activeIndex) {
        for (let i = 0; i < dots.length; i++) {
            dots[i].classList.remove("active");
        }
        if (dots[activeIndex]) {
            dots[activeIndex].classList.add("active");
        }
    }

    window.plusSlides = function(n) {
        clearTimeout(slideTimer);
        slideIndex += n;
        if (slideIndex > slides.length) { slideIndex = 1; }
        if (slideIndex < 1) { slideIndex = slides.length; }
        for (let i = 0; i < slides.length; i++) {
            slides[i].classList.remove("active");
        }
        slides[slideIndex - 1].classList.add("active");
        updateDots(slideIndex - 1);
        slideTimer = setTimeout(showSlides, 5000);
    };

    window.currentSlide = function(n) {
        clearTimeout(slideTimer);
        slideIndex = n;
        for (let i = 0; i < slides.length; i++) {
            slides[i].classList.remove("active");
        }
        slides[slideIndex - 1].classList.add("active");
        updateDots(slideIndex - 1);
        slideTimer = setTimeout(showSlides, 5000);
    };

    showSlides();
});
