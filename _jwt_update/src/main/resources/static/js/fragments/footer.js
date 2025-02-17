document.addEventListener("DOMContentLoaded", function () {
    const observerOptions = {
        root: null, // viewport
        rootMargin: "0px",
        threshold: 0.5 // 요소가 50% 보일 때 나타나도록 설정
    };

    // .intro-box, .info-box-item, .map 요소를 관찰
    const introBox = document.querySelector('.intro-box');
    const infoBoxItems = document.querySelectorAll('.info-box-item');
    const fmap = document.querySelector('#fmap'); // fmap 지도 요소

    // 콜백 함수 정의
    const revealOnScroll = (entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = 1; // 요소가 보이면 opacity를 1로 설정
                entry.target.style.transform = 'translateY(0)'; // 아래에서 위로 이동
                observer.unobserve(entry.target); // 한 번만 애니메이션 실행
            }
        });
    };

    // IntersectionObserver 생성
    const observer = new IntersectionObserver(revealOnScroll, observerOptions);

    // 각 요소에 대해 observer 적용
    observer.observe(introBox);
    infoBoxItems.forEach(item => observer.observe(item)); // .info-box-item이 여러 개일 경우
    observer.observe(fmap); // fmap 지도 관찰 추가
});
