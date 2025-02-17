document.addEventListener('DOMContentLoaded', function () {
    const reservationDateInput = document.getElementById('reservationTime');
    const hourSelect = document.getElementById('hourselect');
    const minutesSelect = document.getElementById('minutesselect');
    const genderInputs = document.querySelectorAll('input[name="gender"]');
    const treatment1Select = document.getElementById('treatment1');
    const treatment2Select = document.getElementById('treatment2');
    const submitButton = document.getElementById("submit-button");
    const reservationForm = document.getElementById("reservation-form");
    const phoneNumberInput = document.getElementById("phone");  // 전화번호 입력 필드
    const emailInput = document.getElementById("email"); // 이메일 입력 필드
    const nameInput = document.getElementById("name"); // 이름 입력 필드

    // 시술부위 옵션 추가 (예시로 Treatment2를 '얼굴', '몸', '다리'로 동적으로 추가)
    treatment1Select.addEventListener('change', function () {
        const selectedTreatment = treatment1Select.value;
        updatetreatment2Options(selectedTreatment);
        updateDetails(); // 시술부위 선택 시 디테일 업데이트
    });

    // 예약일, 시간, 분, 성별, 시술부위를 선택하면 디테일에 그 값을 표시
    reservationDateInput.addEventListener('change', updateDetails);
    hourSelect.addEventListener('change', updateDetails);
    minutesSelect.addEventListener('change', updateDetails);
    genderInputs.forEach(input => {
        input.addEventListener('change', updateDetails);
    });
    treatment1Select.addEventListener('change', updateDetails);
    treatment2Select.addEventListener('change', updateDetails);

    // 전화번호 입력에 숫자만 허용하고 12글자까지만 입력되도록 하는 이벤트 추가
    phoneNumberInput.addEventListener('input', function () {
        // 숫자만 허용
        this.value = this.value.replace(/[^0-9]/g, '');

        // 최대 12글자까지 입력되도록 제한
        if (this.value.length > 12) {
            this.value = this.value.slice(0, 12);
        }
    });

    // "2차 분류" 옵션 업데이트 함수
    function updatetreatment2Options(treatment) {
        let options = [];
        if (treatment === "브라질리언왁싱") {
            options = ["눈", "코", "입"];
        } else if (treatment === "바디왁싱") {
            options = ["상체", "하체"];
        } else if (treatment === "페이스왁싱") {
            options = ["이마", "턱", "볼"];
        } else if (treatment === "임산부왁싱") {
            options = ["허벅지", "종아리"];
        }

        // Treatment2의 옵션을 동적으로 변경
        treatment2Select.innerHTML = ''; // 기존 옵션을 제거

        // 기본값으로 '2차 분류' 옵션을 넣음
        const defaultOption = document.createElement("option");
        defaultOption.value = "2차 분류";
        defaultOption.textContent = "2차 분류";
        treatment2Select.appendChild(defaultOption);

        // 해당 1차 분류에 맞는 2차 옵션을 추가
        options.forEach(option => {
            const optionElement = document.createElement("option");
            optionElement.value = option;
            optionElement.textContent = option;
            treatment2Select.appendChild(optionElement);
        });
    }

    // 예약 상세 정보 업데이트 함수
    function updateDetails() {
        const reservationDate = reservationDateInput.value;
        const selectedHour = hourSelect.value;
        const selectedMinutes = minutesSelect.value;
        const selectedGender = document.querySelector('input[name="gender"]:checked');
        const selectedTreatment1 = treatment1Select.value;
        const selectedTreatment2 = treatment2Select.value;

        // 성별 처리
        const genderValue = selectedGender ? (selectedGender.value === '여자' ? '여(F)' : '남(M)') : '-';

        // 1차와 2차 분류 처리
        const treatment1Value = selectedTreatment1 !== "1차 분류" ? selectedTreatment1 : ""; // 1차 분류가 기본값이면 제외
        const treatment2Value = selectedTreatment2 !== "2차 분류" ? selectedTreatment2 : ""; // 2차 분류가 기본값이면 제외

        // 예약일, 시간, 분을 "예약일: 시간: 분" 형식으로 표시
        const formattedTime = `${selectedHour}:${selectedMinutes}`;

        // 예약 상세에 표시
        document.getElementById('details-gender').textContent = genderValue;
        document.getElementById('details-reservation-date').textContent = reservationDate ? reservationDate :'-';
        document.getElementById('details-reservation-time').textContent = reservationDate ? formattedTime : '';

        // 1차와 2차 분류가 모두 선택되었을 때만 디테일에 표시
        if (treatment1Value && treatment2Value) {
            document.getElementById('details-treatment').textContent = `${treatment1Value} -> ${treatment2Value}`;
        } else {
            document.getElementById('details-treatment').textContent = '-';
        }
    }

    // 폼 제출 처리
    submitButton.addEventListener("click", function (event) {
        event.preventDefault();

        // 필수 입력값이 모두 채워졌는지 확인
        const name = nameInput.value;
        const phoneNumber = phoneNumberInput.value;
        const email = emailInput.value;
        const gender = document.querySelector('input[name="gender"]:checked');
        const reservationDate = reservationDateInput.value;
        const hour = hourSelect.value;
        const minute = minutesSelect.value;
        const treatment1 = treatment1Select.value;
        const treatment2 = treatment2Select.value;

        // 예약 날짜와 시간을 하나로 결합 (yyyy-MM-ddTHH:mm:ss 형식으로)
        const reservationDateTime = `${reservationDate}T${hour}:${minute}`;

//////////////////////////////////////////////////
//////////////////////////////////////////////////

// 이름 입력 확인
        if (!name) {
            alert("이름을 입력해주세요.");
            return; // 이름이 비어 있으면 제출되지 않음
        }

// 이름에 한글과 영어만 포함된 경우 체크
        const nameRegex = /^[a-zA-Z가-힣]+$/; // 영어와 한글만 허용하는 정규식
        if (!nameRegex.test(name)) {
            alert("이름의 형식이 올바르지 않습니다.");
            return; // 이름이 올바르지 않으면 제출되지 않음
        }

// 이름이 2글자 이상인지 확인
        if (name.length < 2) {
            alert("이름의 형식이 올바르지 않습니다.");
            return; // 이름이 2글자 미만이면 제출되지 않음
        }

        // 연락처 입력 확인
        if (!phoneNumber) {
            alert("연락처를 입력해주세요.");
            return; // 연락처가 비어 있으면 제출되지 않음
        }

        // 연락처 글자수가 11개에서 13개일 때만 허용
        if (phoneNumber.length < 11 || phoneNumber.length > 13) {
            alert("연락처 형식이 올바르지 않습니다.");
            return; // 연락처 형식이 맞지 않으면 제출되지 않음
        }

        // 이메일 입력 여부와 형식 검사
        if (!email) {
            alert("이메일을 입력하지 않았습니다.");
            return; // 이메일이 비어 있으면 제출되지 않음
        }


        // 이메일 형식 검사
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(email)) {
            alert("이메일 형식이 올바르지 않습니다.");
            return; // 이메일 형식이 올바르지 않으면 제출되지 않음
        }

        // 성별 입력 확인
        if (!gender) {
            alert("성별을 입력해주세요.");
            return; // 성별이 비어 있으면 제출되지 않음
        }

        // 예약일 입력 확인
        if (!reservationDate) {
            alert("예약일을 입력해주세요.");
            return; // 예약일이 비어 있으면 제출되지 않음
        }

        // 1차 분류 선택 확인
        if (treatment1 === "1차 분류") {
            alert("1차 분류를 선택해주세요.");
            return; // 1차 분류가 선택되지 않으면 제출되지 않음
        }

        // 2차 분류 선택 확인
        if (treatment2 === "2차 분류") {
            alert("2차 분류를 선택해주세요.");
            return; // 2차 분류가 선택되지 않으면 제출되지 않음
        }


//////////////////////////////////////////////////
//////////////////////////////////////////////////



        // 폼 데이터 수집
        const formData = new FormData(reservationForm);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        data["category"] = treatment2;
        data["reservationTime"] = reservationDateTime; // 예약 날짜와 시간을 추가

        console.log(data);  // 폼 데이터 로그 확인

        // 백엔드로 데이터 전송 (Spring Boot 서버 예시)
        fetch("/api/reservation/write", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                console.log("Success:", data);
                alert("예약에 실패했습니다. 다시 시도해주세요.");
            })
            .catch(error => {
                console.error("Error:", error);
                alert("예약이 완료되었습니다!");
            });
    });
});