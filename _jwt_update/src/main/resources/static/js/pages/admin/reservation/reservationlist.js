// 모달 관련 변수
var modal = document.getElementById("myModal");
var span = document.getElementsByClassName("close")[0];

// 내용 보기 버튼 클릭 시 모달 열기
var buttons = document.querySelectorAll('.openModalBtn');
buttons.forEach(function(button) {
    button.addEventListener('click', function() {
        var content = button.getAttribute('data-text');
        document.getElementById("modalText").textContent = content;  // 모달에 내용 설정
        modal.style.display = "block";  // 모달 보이기
    });
});

// 모달 닫기
span.onclick = function() {
    modal.style.display = "none";  // 모달 닫기
}

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}