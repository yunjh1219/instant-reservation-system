package edu.du._waxing_home.event.controller;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.dto.EventCreateDto;
import edu.du._waxing_home.event.service.EventService;
import edu.du._waxing_home.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/event/activelist")
    public String getEvent(Model model,
                           @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = eventService.getActiveEvents(pageable);

        model.addAttribute("eventPage", eventPage);
        return "pages/community/event";
    }

    @GetMapping("/event/endedlist")
    public String getEndedEvents(Model model,
                                 @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = eventService.getEndedEvents(pageable);

        model.addAttribute("eventPage", eventPage);
        return "pages/community/endedEvents"; // 종료된 이벤트를 표시할 템플릿 파일 이름
    }

    @GetMapping("/event/write")
    public String eventwrtie(){return "pages/write/writeevent";}


    @PostMapping("/event/write")
    public String eventWritePost(EventCreateDto createDto,
                                 @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal User user) {
        // 이미지가 선택되지 않았으면 빈 리스트로 처리
        if (images == null) {
            images = new ArrayList<>();
        }

        try {
            eventService.createEvent(createDto, images, user); // 서비스 메서드를 호출하여 이벤트 생성
            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 등록되었습니다.");
            return "redirect:/event/list";  // 이벤트 목록 페이지로 리다이렉트
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/event/write";  // 이벤트 작성 페이지로 리다이렉트
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "이벤트 등록 중 오류가 발생했습니다.");
            return "redirect:/event/write";  // 이벤트 작성 페이지로 리다이렉트
        }
    }
}
