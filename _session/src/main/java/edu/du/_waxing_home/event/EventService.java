package edu.du._waxing_home.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // 진행 종료한 이벤트 목록 불러오기
    public Page<Event> getEndedEvents(Pageable pageable) {
        return eventRepository.findAllEndedEvents(LocalDate.now(), pageable);
    }

    // 진행중인 이벤트 목록 불러오기
    public Page<Event> getActiveEvents(Pageable pageable) {
        return eventRepository.findAllActiveEvents(LocalDate.now(), pageable);
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

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // 이미지가 없는 경우
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    // 이미지가 있는 경우
    public void saveEventWithImages(Event event, List<MultipartFile> images) throws IOException {
        // 이미지 저장 후 이미지 URL 목록을 반환
        List<String> imageUrls = saveImages(images);
        event.setImageUrls(imageUrls); // 이미지 URL을 뉴스 객체에 설정
        eventRepository.save(event); // 뉴스 객체와 이미지를 함께 저장
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

    // 게시글 삭제 메서드
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        // 이미지 삭제
        for (String imageUrl : event.getImageUrls()) {
            String imagePath = "src/main/resources/static/" + imageUrl;  // 이미지 경로
            deleteImage(imagePath);  // 이미지 삭제 메서드 호출
        }

        eventRepository.delete(event);  // 리뷰 삭제
    }

    private void deleteImage(String imagePath) {
        Path path = Paths.get(imagePath);
        try {
            Files.deleteIfExists(path);  // 파일이 존재하면 삭제
        } catch (IOException e) {
            e.printStackTrace();  // 파일 삭제 오류 처리
        }
    }

    // 게시글 수정 메서드
    public void updateEvent(Long id, Event updatedEvent, List<MultipartFile> images, List<String> deletedImages) throws IOException {
        Event event = getEventById(id);
        event.setTitle(updatedEvent.getTitle());
        event.setContent(updatedEvent.getContent());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());

        // 삭제된 파일 처리
        if (deletedImages != null && !deletedImages.isEmpty()) {
            for (String imageUrl : deletedImages) {
                // 실제 파일 경로 계산 (이미지 URL을 통해)
                String imagePath = "src/main/resources/static" + imageUrl; // imageUrl은 /uploads/... 형식이어야 합니다.
                deleteImage(imagePath); // 파일 삭제 메서드 호출
                event.getImageUrls().remove(imageUrl); // 이미지 URL 목록에서 해당 파일 제거
            }
        }

        // 새로 업로드된 파일 처리
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = saveImages(images); // 이미지를 저장하는 메서드 호출
            event.getImageUrls().addAll(imageUrls); // 새로 업로드된 이미지 URL 추가
        }

        // 변경된 이벤트 정보 저장
        eventRepository.save(event);
    }
}
