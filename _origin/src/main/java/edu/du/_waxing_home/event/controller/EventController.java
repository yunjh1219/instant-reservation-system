package edu.du._waxing_home.event.controller;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
