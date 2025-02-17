package edu.du._waxing_home.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService EventService;

    //게시글 목록 보여줌
    @GetMapping("/list")
    public String getEvent(Model model,
                          @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = EventService.getActiveEvents(pageable);

        model.addAttribute("eventPage", eventPage);
        return "pages/community/event";
    }

    @GetMapping("/list/ended")
    public String getEndedEvents(Model model,
                                 @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = EventService.getEndedEvents(pageable);

        model.addAttribute("eventPage", eventPage);
        return "pages/community/endedEvents"; // 종료된 이벤트를 표시할 템플릿 파일 이름
    }

    //전송 게시글 업로드
    @PostMapping("/submit")
    public String submitEvent(Event event,
                               @RequestParam(value = "images", required = false) List<MultipartFile> images, // 파일 선택이 선택적
                               RedirectAttributes redirectAttributes) {
        try {
            // 이미지가 첨부되었다면 처리
            if (images != null && !images.isEmpty()) {
                EventService.saveEventWithImages(event, images);  // 여러 이미지를 서비스에서 처리
            } else {
                EventService.saveEvent(event);  // 이미지가 없으면 이미지 없이 저장
            }

            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 등록되었습니다.");
            return "redirect:/event/list"; // 소식 목록 페이지로 리다이렉트
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "소식 등록에 실패했습니다.");
            return "redirect:/event/write"; // 작성 페이지로 리다이렉트
        }
    }


    // 게시글 상세 보기
    @GetMapping("/detail/{id}")
    public String getEventDetail(@PathVariable Long id, Model model) {

        // 이벤트 조회
        Event event = EventService.getEventById(id);

        // 조회수 증가
        EventService.incrementViews(event);

        // 이전글과 다음글 조회
        Event previousEvent = EventService.getPreviousEvent(id);
        Event nextEvent = EventService.getNextEvent(id);

        // 모델에 이벤트 정보, 이전글, 다음글 추가
        model.addAttribute("event", event);
        model.addAttribute("previousEvent", previousEvent); // 이전글
        model.addAttribute("nextEvent", nextEvent); // 다음글

        // 상세 페이지로 이동
        return "pages/detail/detailevent";
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            EventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("message", "이벤트가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "이벤트 삭제에 실패했습니다.");
        }
        return "redirect:/event/list";  // 이벤트 목록 페이지로 리다이렉트
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        Event event = EventService.getEventById(id);
        model.addAttribute("event", event);
        return "pages/write/writeevent";
    }

    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, Event updatedEvent,
                              @RequestParam(value = "images", required = false) List<MultipartFile> images,
                              @RequestParam(value = "deleteFilesId", required = false) List<String> deletedFiles,
                              RedirectAttributes redirectAttributes) {
        try {
            // 삭제된 파일 목록 처리
            if (deletedFiles != null && !deletedFiles.isEmpty()) {
                System.out.println("Received deleted files: " + deletedFiles);
            }

            // 업로드된 이미지 처리
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    System.out.println("Received image: " + image.getOriginalFilename());
                }
            }

            // EventService.updateEvent 호출하여 이벤트 업데이트
            EventService.updateEvent(id, updatedEvent, images, deletedFiles);

            // 성공 시 리다이렉트
            redirectAttributes.addFlashAttribute("message", "이벤트가 성공적으로 수정되었습니다.");
            return "redirect:/event/detail/" + id;
        } catch (Exception e) {
            // 기타 예외 처리
            redirectAttributes.addFlashAttribute("error", "이벤트 수정에 실패했습니다.");
            return "redirect:/event/edit/" + id;
        }
    }
    // 이미지 URL만 반환하는 API 요청
    @GetMapping("/api/event/{id}/images")
    @ResponseBody
    public ResponseEntity<List<String>> getEventImages(@PathVariable Long id) {
        Event event = EventService.getEventById(id);
        return ResponseEntity.ok(event.getImageUrls());
    }


}
