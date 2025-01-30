document.addEventListener('DOMContentLoaded', function () {
    const agreeAllButton = document.getElementById('agree-all');  // 전체 동의 버튼
    const checkboxes = document.querySelectorAll('.form-check input[type="checkbox"]');  // 모든 체크박스들
    const nextStepButton = document.getElementById('nextStepBtn');  // '다음단계로' 버튼
    const form = document.querySelector('form');

    // 전체 동의 버튼 클릭 시 모든 체크박스를 선택 또는 해제하는 함수
    agreeAllButton.addEventListener('click', function (e) {
        e.preventDefault();  // 기본 동작인 링크 이동을 방지

        // 모든 체크박스가 선택되었는지 확인
        const allChecked = Array.from(checkboxes).every(function (checkbox) {
            return checkbox.checked;
        });

        // 모든 체크박스를 선택하거나 해제
        checkboxes.forEach(function (checkbox) {
            checkbox.checked = !allChecked;  // 선택되지 않으면 모두 선택, 선택되면 모두 해제
        });

        // '전체 동의' 버튼에 active 클래스 추가 또는 제거
        if (allChecked) {
            agreeAllButton.classList.remove('active'); // 모든 체크박스가 체크되면 active 클래스 제거
        } else {
            agreeAllButton.classList.add('active'); // 하나라도 체크되지 않으면 active 클래스 추가
        }

        // 필수 항목 체크 여부를 기준으로 '다음단계로' 버튼 활성화 여부 설정
        toggleNextStepButton();
    });

    // 체크박스 상태 변경 시 '전체 동의' 버튼 상태 갱신
    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            // 모든 체크박스가 체크된 상태인지 확인
            const allChecked = Array.from(checkboxes).every(function (checkbox) {
                return checkbox.checked;
            });

            // '전체 동의' 버튼에 active 클래스 추가 또는 제거
            if (allChecked) {
                agreeAllButton.classList.add('active');  // 모든 체크박스가 체크되면 active 클래스 추가
            } else {
                agreeAllButton.classList.remove('active');  // 하나라도 체크되지 않으면 active 클래스 제거
            }

            // 필수 항목 체크 여부를 기준으로 '다음단계로' 버튼 활성화 여부 설정
            toggleNextStepButton();
        });
    });

    // 필수 항목이 모두 체크되었는지 확인 후 '다음단계로' 버튼 활성화
    function toggleNextStepButton() {
        const allRequiredChecked = Array.from(checkboxes).every(function (checkbox) {
            return checkbox.checked || !checkbox.required;  // 필수 항목만 체크되면 OK
        });

        // 필수 항목이 모두 체크되었으면 버튼 활성화
        if (allRequiredChecked) {
            nextStepButton.disabled = false;
        } else {
            nextStepButton.disabled = true;  // 필수 항목이 체크되지 않으면 버튼 비활성화
        }
    }

    // 초기 로딩 시에도 필수 체크박스 상태에 맞게 버튼 상태 설정
    toggleNextStepButton();

    // '다음단계로' 버튼 클릭 시 필수 항목이 모두 체크되었는지 확인
    nextStepButton.addEventListener('click', function (e) {
        // 체크박스 상태를 확인 후, 필수 항목이 체크되지 않으면 이동을 막음
        const allRequiredChecked = Array.from(checkboxes).every(function (checkbox) {
            return checkbox.checked || !checkbox.required;  // 필수 항목만 체크되면 OK
        });

        if (!allRequiredChecked) {
            alert('필수 항목을 모두 동의해주세요.');
            e.preventDefault();  // 링크 이동을 막음
        }
    });

    // 폼 제출 전 체크 상태 확인
    form.addEventListener('submit', function (e) {
        // 필수 항목만 체크되면 OK
        const allRequiredChecked = Array.from(checkboxes).every(function (checkbox) {
            return checkbox.checked || !checkbox.required;
        });

        // 필수 항목이 체크되지 않으면 제출을 막고 경고 메시지 표시
        if (!allRequiredChecked) {
            alert('필수 항목을 모두 동의해주세요.');
            e.preventDefault();  // 폼 제출 막기
        }
    });
});