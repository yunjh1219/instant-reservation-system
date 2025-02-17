package edu.du._waxing_home.event.service;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.dto.EventCreateDto;
import edu.du._waxing_home.event.repository.EventRepository;
import edu.du._waxing_home.user.domain.User;
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

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // 진행중인 이벤트 목록 불러오기
    public Page<Event> getActiveEvents(Pageable pageable) {
        return eventRepository.findAllActiveEvents(LocalDate.now(), pageable);
    }
    // 진행 종료한 이벤트 목록 불러오기
    public Page<Event> getEndedEvents(Pageable pageable) {
        return eventRepository.findAllEndedEvents(LocalDate.now(), pageable);
    }

    @Transactional
    public void createEvent(EventCreateDto createDto, List<MultipartFile> images, User user) throws IOException {
        // 이미지가 없을 경우 빈 리스트 처리
        if (images == null) {
            images = new ArrayList<>();
        }

        // 이미지를 처리하여 URL 리스트를 가져옴
        List<String> imageUrls = saveImages(images);

        // Event 엔티티 생성
        Event event = Event.builder()
                .user(user)
                .title(createDto.getTitle())
                .content(createDto.getContent())
                .startDate(createDto.getStartDate())
                .endDate(createDto.getEndDate())
                .status("ACTIVE") // 상태 예시
                .views(0L) // 초기 조회수
                .imageUrls(imageUrls) // 변환된 이미지 URL 목록
                .build();

        eventRepository.save(event);  // 이벤트 저장
    }



    // 이미지 저장 메서드
    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            // 이미지 파일 이름을 UUID로 변경하여 중복을 방지
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

            // 이미지 파일을 서버 내 지정된 디렉터리에 저장
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());  // 디렉터리가 없으면 생성
            image.transferTo(path);  // 이미지 파일을 지정된 경로로 저장

            // 웹에서 접근 가능한 이미지 경로를 URL 형식으로 생성하여 리스트에 추가
            imageUrls.add("/uploads/" + fileName);
        }

        return imageUrls;
    }
}
