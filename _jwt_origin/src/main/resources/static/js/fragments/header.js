
// 스크롤 내릴 때 padding 제거되게 -----------------------------------------------------------------------------
window.addEventListener('scroll', function() {
    const gnb = document.querySelector('.gnb'); // .gnb 요소 선택
    if (window.scrollY > 0) { // 스크롤이 0보다 클 경우
        gnb.classList.add('scrolled'); // 'scrolled' 클래스 추가
    } else {
        gnb.classList.remove('scrolled'); // 'scrolled' 클래스 제거
    }
});

// overlay -----------------------------------------------------------------------------
function toggleMenu(button) {
    const overlay = document.querySelector('.overlay');
    const isOpened = button.classList.toggle('opened');
    button.setAttribute('aria-expanded', isOpened);

    // overlay의 상태에 따라 클래스 추가 또는 제거
    if (isOpened) {
        overlay.classList.add('open'); // overlay 보이게 설정
    } else {
        overlay.classList.remove('open'); // overlay 숨김
    }
}