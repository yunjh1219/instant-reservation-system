

    function openReviewDetailModal(button) {
        // 버튼에서 환자 정보를 가져옴
        var reviewId = button.getAttribute('data-review-id');
        var reviewName = button.getAttribute('data-review-name');
        var reviewTreatment = button.getAttribute('data-review-treatment');
        var reviewPhone = button.getAttribute('data-review-phone');
        var reviewEmail = button.getAttribute('data-review-email');
        var reviewContent = button.getAttribute('data-review-content');

        // 모달에 값을 설정
        document.getElementById('editId').value = reviewId;
        document.getElementById('editName').value = reviewName;
        document.getElementById('editPhone').value = reviewPhone;
        document.getElementById('editEmail').value = reviewEmail;
        document.getElementById('editTreatment').value = reviewTreatment;
        document.getElementById('editContent').value = reviewContent;

        // 모달 열기
        document.getElementById('reviewEditModal').style.display = 'block';
    }


    // 모달 닫기
    document.getElementById('closeEditModal').onclick = function() {
        document.getElementById('reviewEditModal').style.display = 'none';
    }

    // 수정 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        var modal = document.getElementById('reviewEditModal');
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    }

