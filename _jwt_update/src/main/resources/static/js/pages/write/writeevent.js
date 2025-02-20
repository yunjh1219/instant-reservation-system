document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.file-input').forEach(input => {
        input.addEventListener('change', function () {
            let fileName = this.files.length > 0 ? this.files[0].name : "선택된 파일 없음";
            let nameSpan = document.getElementById(this.id + "Name");

            if (nameSpan) {
                nameSpan.textContent = fileName;
            }
        });
    });
});

function clearFile(id) {
    let fileInput = document.getElementById(id);
    let nameSpan = document.getElementById(id + "Name");

    if (fileInput) {
        fileInput.value = ""; // 파일 입력 필드 초기화
    }

    if (nameSpan) {
        nameSpan.textContent = "선택된 파일 없음"; // 파일명 초기화
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");

    // 확인: 요소들이 실제로 존재하는지
    if (!startDateInput || !endDateInput) {
        console.error("시작 날짜나 종료 날짜 입력 필드가 없습니다.");
        return;
    }

    // 시작 날짜가 변경될 때 종료 날짜를 확인
    startDateInput.addEventListener("change", function() {
        validateDates();
    });

    // 종료 날짜가 변경될 때 시작 날짜와 비교
    endDateInput.addEventListener("change", function() {
        validateDates();
    });

    function validateDates() {
        const startDate = new Date(startDateInput.value);
        const endDate = new Date(endDateInput.value);

        if (startDate && endDate && startDate > endDate) {
            alert("시작 날짜가 종료 날짜보다 늦을 수 없습니다.");
            // 종료 날짜를 시작 날짜로 변경
            endDateInput.value = startDateInput.value;
        }
    }
});


