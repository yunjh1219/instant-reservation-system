document.addEventListener('DOMContentLoaded', function() {
    var uploadedFiles = []; // 업로드된 파일을 추적하기 위한 배열
    var thumbnailFileId = null; // 썸네일 파일의 ID

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

                    var thumbnailButton = document.createElement('button');
                    thumbnailButton.textContent = '썸네일';
                    thumbnailButton.classList.add('thumbnail-btn');
                    fileItem.appendChild(thumbnailButton);

                    // 삭제 버튼 클릭 시 파일 삭제
                    deleteButton.addEventListener('click', function() {
                        uploadedFiles = uploadedFiles.filter(function(f) {
                            return f.id !== fileId;
                        }); // 배열에서 파일 삭제
                        fileItem.remove(); // 파일 항목 삭제
                        // 이미지가 삭제되면 imageInput에서 파일명 제거
                        updateImageInputText();
                    });

                    // 썸네일 버튼 클릭 시 0번째 객체로 설정
                    thumbnailButton.addEventListener('click', function() {
                        thumbnailFileId = fileId;
                        document.querySelectorAll('.thumbnail-btn').forEach(function(btn) {
                            btn.classList.remove('selected-thumbnail');
                        });
                        thumbnailButton.classList.add('selected-thumbnail');
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
    document.getElementById('eventwriteform').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 동작 막기

        var formData = new FormData(this);

        // 업로드된 파일들을 FormData에 추가
        uploadedFiles.forEach(function(fileObj) {
            formData.append('images', fileObj.file);
        });

        // 썸네일 파일이 선택되었으면, 첫 번째 파일로 설정
        if (thumbnailFileId) {
            var thumbnailFile = uploadedFiles.find(function(f) {
                return f.id === thumbnailFileId;
            });
            if (thumbnailFile) {
                formData.delete('images'); // 기존 파일들 제거
                formData.append('images', thumbnailFile.file); // 썸네일 파일을 첫 번째로 추가
                uploadedFiles.forEach(function(fileObj) {
                    if (fileObj.id !== thumbnailFileId) {
                        formData.append('images', fileObj.file); // 나머지 파일들 추가
                    }
                });
            }
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
                window.location.href = '/event/list'; // 페이지 리다이렉트
            })
            .catch(error => {
                // 오류 처리
                console.error('Error:', error);
            });
    });
});