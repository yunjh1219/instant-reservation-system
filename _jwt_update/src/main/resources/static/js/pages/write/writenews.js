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
    document.getElementById('newswriteform').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 동작 막기

        var formData = new FormData(this);

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
                window.location.href = '/news/list'; // 페이지 리다이렉트
            })
            .catch(error => {
                // 오류 처리
                console.error('Error:', error);
            });
    });
});