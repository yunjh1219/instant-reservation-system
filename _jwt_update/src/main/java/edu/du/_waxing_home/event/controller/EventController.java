package edu.du._waxing_home.event.controller;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.dto.EventCreateDto;
import edu.du._waxing_home.event.repository.EventRepository;
import edu.du._waxing_home.event.service.EventService;
import edu.du._waxing_home.global.security.Login;
import edu.du._waxing_home.user.dto.LoginUser;
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
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;


    //POST ========================================================

    // 이벤트 등록 처리
    @PostMapping("/write")
    public String createEvent(@ModelAttribute EventCreateDto createDto, @Login LoginUser loginUser, RedirectAttributes redirectAttributes) throws IOException {
        try {
            eventService.createEvent(createDto, loginUser);
            redirectAttributes.addFlashAttribute("message", "이벤트가 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/event/list";  // 이벤트 목록 페이지로 리다이렉트
    }

    //게시글 삭제
    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes, @Login LoginUser loginUser) {
        try {
            eventService.deleteEvent(id, loginUser);
            redirectAttributes.addFlashAttribute("message", "이벤트가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "이벤트 삭제에 실패했습니다.");
        }
        return "redirect:/event/list";  // 이벤트 목록 페이지로 리다이렉트
    }

    // 이벤트 수정 처리
    @PostMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id,
                              @ModelAttribute EventCreateDto updateDto,
                              @Login LoginUser loginUser,
                              RedirectAttributes redirectAttributes) throws IOException {
        try {
            eventService.updateEvent(id, updateDto, loginUser);
            redirectAttributes.addFlashAttribute("message", "이벤트가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/event/list";
    }



    //GET =========================================================
    //진행중인 이벤트 확인(default)
    @GetMapping("/list")
    public String getEvent(Model model,
                           @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = eventService.getActiveEvents(pageable);

        model.addAttribute("eventPage", eventPage);

        return "pages/community/event";
    }

    //종료된 이벤트 확인
    @GetMapping("/endedlist")
    public String getEndedEvents(Model model,
                                 @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 n개 항목
        Page<Event> eventPage = eventService.getEndedEvents(pageable);

        model.addAttribute("eventPage", eventPage);
        return "pages/community/endedEvents"; // 종료된 이벤트를 표시할 템플릿 파일 이름
    }

    //게시글 상세 보기
    @GetMapping("/{id}")
    public String getEventDetail(@PathVariable Long id, Model model) {

        // 이벤트 조회
        Event event = eventService.getEventById(id);

        // 조회수 증가
        eventService.incrementViews(event);

        // 이전글과 다음글 조회
        Event previousEvent = eventService.getPreviousEvent(id);
        Event nextEvent = eventService.getNextEvent(id);

        // 모델에 이벤트 정보, 이전글, 다음글 추가
        model.addAttribute("event", event);
        model.addAttribute("previousEvent", previousEvent); // 이전글
        model.addAttribute("nextEvent", nextEvent); // 다음글

        // 상세 페이지로 이동
        return "/pages/detail/detailevent.html";
    }

    //게시글 작성으로 넘어가기
    @GetMapping("/write")
    public String eventwrtie(){return "pages/write/writeevent";}

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "pages/write/writeevent";
    }


}
