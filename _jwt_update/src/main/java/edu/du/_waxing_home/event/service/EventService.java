package edu.du._waxing_home.event.service;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.dto.EventCreateDto;
import edu.du._waxing_home.event.repository.EventRepository;
import edu.du._waxing_home.global.error.exception.UserNotFoundException;
import edu.du._waxing_home.user.domain.Role;
import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.dto.LoginUser;
import edu.du._waxing_home.user.repository.UserRepository;
import edu.du._waxing_home.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // 이벤트 등록
    @Transactional
    public void createEvent(EventCreateDto createDto, LoginUser loginUser) throws IOException {
        User user = userRepository.findByUsernameAndRole(loginUser.getUserName(), Role.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("관리자 권한이 없습니다."));

        // 썸네일 이미지 저장
        String thumbnailPath = saveImage(createDto.getThumbnail());

        // 본문 이미지 저장
        String bodyImagePath = saveImage(createDto.getBodyImage());

        // 이벤트 엔티티 생성
        Event event = createDto.toEntity(user);
        event.setThumbnailPath(thumbnailPath);
        event.setBodyImagePath(bodyImagePath);

        // 이벤트 저장
        eventRepository.save(event);
    }

    // 이미지 저장
    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) return null;

        // 파일명 생성
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.createDirectories(filePath.getParent()); // 디렉토리 생성
        Files.copy(image.getInputStream(), filePath);  // 파일 저장

        return "/uploads/" + fileName;  // 상대 경로 리턴
    }

    // 게시글 삭제 메서드
    public void deleteEvent(Long id, LoginUser loginUser) throws IOException {
        // 로그인된 사용자 정보를 기반으로 사용자 조회
        userRepository.findByUsernameAndRole(loginUser.getUserName(), Role.ADMIN)
                .orElseThrow(() -> new UserNotFoundException("관리자 권한이 없습니다."));

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        // 썸네일 이미지 삭제
        if (event.getThumbnailPath() != null) {
            deleteImage(event.getThumbnailPath());
        }

        // 본문 이미지 삭제
        if (event.getBodyImagePath() != null) {
            deleteImage(event.getBodyImagePath());
        }

        // 이벤트 삭제
        eventRepository.delete(event);  // 이벤트 삭제
    }


    // 이미지 삭제
    private void deleteImage(String imagePath) throws IOException {
        if (imagePath != null) {
            // 이미지 경로에서 "/uploads/"를 제외하고 실제 경로로 변환
            Path pathToDelete = Paths.get(UPLOAD_DIR, imagePath.replace("/uploads/", ""));

            // 파일이 존재하면 삭제
            if (Files.exists(pathToDelete)) {
                Files.delete(pathToDelete);  // 파일 삭제
            }
        }
    }

    //이벤트 수정
    @Transactional
    public void updateEvent(Long id, EventCreateDto updateDto, LoginUser loginUser) throws IOException {
        // 로그인된 사용자 정보를 기반으로 사용자 조회
       userRepository.findByUsernameAndRole(loginUser.getUserName(), Role.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("관리자 권한이 없습니다."));

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        // 기존 이미지 경로가 있다면 삭제 (이미지를 수정하는 경우에만)
        if (updateDto.getThumbnail() != null && !updateDto.getThumbnail().isEmpty()) {
            deleteImage(event.getThumbnailPath());  // 기존 썸네일 이미지 삭제
            event.setThumbnailPath(saveImage(updateDto.getThumbnail()));  // 새 썸네일 이미지 저장
        }

        if (updateDto.getBodyImage() != null && !updateDto.getBodyImage().isEmpty()) {
            deleteImage(event.getBodyImagePath());  // 기존 본문 이미지 삭제
            event.setBodyImagePath(saveImage(updateDto.getBodyImage()));  // 새 본문 이미지 저장
        }

        // 제목 및 내용 수정 (이미지를 수정하지 않아도 항상 수행)
        event.setTitle(updateDto.getTitle());
        event.setContent(updateDto.getContent());
        event.setStartDate(updateDto.getStartDate());
        event.setEndDate(updateDto.getEndDate());

        // 이벤트 업데이트 저장
        eventRepository.save(event);
    }



    //===[ GET 요청 서비스 ] ============================================

    // 진행중인 이벤트 목록 불러오기
    public Page<Event> getActiveEvents(Pageable pageable) {
        return eventRepository.findAllActiveEvents(LocalDate.now(), pageable);
    }
    // 진행 종료한 이벤트 목록 불러오기
    public Page<Event> getEndedEvents(Pageable pageable) {
        return eventRepository.findAllEndedEvents(LocalDate.now(), pageable);
    }

    //Id로 찾기
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
    }

    //이전글
    public Event getPreviousEvent(Long id) {
        return eventRepository.findTopByIdLessThanOrderByIdDesc(id).orElse(null);
    }

    //다음글
    public Event getNextEvent(Long id) {
        return eventRepository.findTopByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    // 조회수 증가 메서드
    @Transactional
    public void incrementViews(Event event) {
        // 조회수 증가
        event.setViews(event.getViews() + 1);

        // 업데이트된 뉴스 저장
        eventRepository.save(event);
    }


}



