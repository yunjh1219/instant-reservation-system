window.onload = function() {
    const modal = document.getElementById("customeraddModal"); // 모달 DOM 요소
    const openModalButton = document.getElementById("openaddModal"); // 모달 열기 버튼
    const closeModalButton = document.getElementById("closeaddModal"); // 모달 닫기 버튼

    // 모달을 열 때
    openModalButton.addEventListener("click", function() {
        modal.style.display = "flex"; // 모달 보이기
    });

    // 모달을 닫을 때
    closeModalButton.addEventListener("click", function() {
        modal.style.display = "none"; // 모달 숨기기
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener("click", function(event) {
        if (event.target === modal) {
            modal.style.display = "none"; // 모달 숨기기
        }
    });
};

// 폼 제출 처리 (AJAX 요청을 통해 서버로 데이터 전송)
customerForm.addEventListener('submit', function(event) {
    event.preventDefault();  // 기본 제출 방지

    // 폼 데이터 가져오기
    const formData = new FormData(customerForm);

    // AJAX 요청 (Fetch API 사용)
    fetch('/customer/add', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json()) // 서버로부터의 응답 처리
        .then(data => {
            if (data.success) {
                alert('환자가 추가되었습니다.');
                window.location.reload(); // 페이지 새로고침 (환자 목록 갱신)
            } else {
                alert('환자 추가에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버 오류로 인해 환자 추가에 실패했습니다.');
        });
});