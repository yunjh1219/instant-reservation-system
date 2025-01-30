// 수정 모달 열기
function openEditModal(button) {
    // 버튼에서 환자 정보를 가져옴
    var customerId = button.getAttribute('data-customer-id');
    var customerName = button.getAttribute('data-customer-name');
    var customerPhone = button.getAttribute('data-customer-phone');
    var customerEmail = button.getAttribute('data-customer-email');
    var customerGender = button.getAttribute('data-customer-gender');

    // 모달에 값을 설정
    document.getElementById('editId').value = customerId;
    document.getElementById('editName').value = customerName;
    document.getElementById('editPhone').value = customerPhone;
    document.getElementById('editEmail').value = customerEmail;
    document.getElementById('editGender').value = customerGender;

    // 모달 열기
    document.getElementById('customerEditModal').style.display = 'block';
}

// 수정 모달 닫기
document.getElementById('closeEditModal').onclick = function() {
    document.getElementById('customerEditModal').style.display = 'none';
}

// 수정 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    var modal = document.getElementById('customerEditModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}