document.addEventListener('DOMContentLoaded', function() {
    var uploadedFiles = []; // 업로드된 파일을 추적하기 위한 배열

    document.getElementById('image').addEventListener('change', function(event) {
        var files = event.target.files;
        var fileListContainer = document.getElementById('fileList'); // 파일 목록을 표시할 컨테이너
        var imageInput = document.getElementById('imageInput'); // 이미지 input 텍스트 필드

        // 파일 개수가 3개 이상일 경우 경고 메시지
        if (uploadedFiles.length + files.length > 3) {
            document.getElementById('file-warning').textContent = '최대 3개까지 업로드 가능합니다.';
            document.getElementById('file-warning').style.display = 'block';
            return;
        } else {
            document.getElementById('file-warning').style.display = 'none';
        }

        // 새로 선택된 파일들을 배열에 추가하고, 이미지 추가
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var fileId = Date.now() + '-' + i; // 고유 ID 생성 (현재 시간 + 인덱스)

            uploadedFiles.push({
                file: file,
                id: fileId
            }); // 업로드된 파일을 배열에 추가

            var reader = new FileReader();
            reader.onload = (function(f, fileId) {
                return function(e) {
                    // 파일명 및 삭제 버튼을 표시할 div 생성
                    var fileItem = document.createElement('div');
                    fileItem.classList.add('file-item');
                    fileItem.setAttribute('data-id', fileId); // 파일 아이디 설정

                    var fileName = document.createElement('span');
                    fileName.textContent = f.name;
                    fileItem.appendChild(fileName);

                    var deleteButton = document.createElement('button');
                    deleteButton.textContent = '삭제';
                    deleteButton.classList.add('delete-btn');
                    fileItem.appendChild(deleteButton);

                    // 삭제 버튼 클릭 시 파일 삭제
                    deleteButton.addEventListener('click', function() {
                        uploadedFiles = uploadedFiles.filter(function(f) {
                            return f.id !== fileId;
                        }); // 배열에서 파일 삭제
                        fileItem.remove(); // 파일 항목 삭제
                        // 이미지가 삭제되면 imageInput에서 파일명 제거
                        updateImageInputText();
                    });

                    fileListContainer.appendChild(fileItem); // 파일 목록에 추가
                    // 파일명 업데이트
                    updateImageInputText();
                };
            })(file, fileId);

            reader.readAsDataURL(file); // 파일 읽기
        }

        // imageInput 텍스트 필드 업데이트
        function updateImageInputText() {
            var fileNames = uploadedFiles.map(function(f) {
                return f.file.name;
            });
            imageInput.value = fileNames.join(', ');
        }
    });

    // 폼 제출 시 첨부된 파일 정보 전송
    document.getElementById('reivewpost-form').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 동작 막기

        var formData = new FormData(this);

        var name = document.getElementById('name').value; // 이름 입력값 가져오기
        var phoneNumber = document.getElementById('phoneNumber').value;
        var email = document.getElementById('email').value;
        var content = document.getElementById('content').value;

// 이름에 한글과 영어만 포함된 경우 체크
        const nameRegex = /^[a-zA-Z가-힣]+$/; // 영어와 한글만 허용하는 정규식
        if (!nameRegex.test(name)) {
            alert("이름의 형식이 올바르지 않습니다.");
            return; // 이름이 올바르지 않으면 제출되지 않음
        }

        // 연락처 글자수가 11개에서 13개일 때만 허용
        if (phoneNumber.length < 11 || phoneNumber.length > 13) {
            alert("연락처 형식이 올바르지 않습니다.");
            return; // 연락처 형식이 맞지 않으면 제출되지 않음
        }

        // 이메일 형식 검사
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(email)) {
            alert("이메일 형식이 올바르지 않습니다.");
            return; // 이메일 형식이 올바르지 않으면 제출되지 않음
        }

        // content가 10글자 미만인 경우
        if (content.length < 10) {
            alert("내용은 10글자 이상이어야 합니다.");
            return; // 제출을 중지
        }

        // 업로드된 파일들을 FormData에 추가
        uploadedFiles.forEach(function(fileObj) {
            formData.append('images', fileObj.file);
        });

        // 이미지가 첨부되지 않았다면, images 파라미터를 추가하지 않음
        if (uploadedFiles.length === 0) {
            // 빈 배열을 보내지 않고 'images' 파라미터를 아예 생략
        }

        // FormData를 서버로 전송
        fetch(this.action, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // 서버 응답을 텍스트로 받기
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(responseText => {
                // 성공 처리
                console.log('Success:', responseText);
                window.location.href = '/review/list'; // 페이지 리다이렉트
            })
            .catch(error => {
                // 오류 처리
                console.error('Error:', error);
            });
    });
});