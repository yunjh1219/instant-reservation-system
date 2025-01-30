package edu.du._waxing_home.event.service;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // 진행중인 이벤트 목록 불러오기
    public Page<Event> getActiveEvents(Pageable pageable) {
        return eventRepository.findAllActiveEvents(LocalDate.now(), pageable);
    }
    // 진행 종료한 이벤트 목록 불러오기
    public Page<Event> getEndedEvents(Pageable pageable) {
        return eventRepository.findAllEndedEvents(LocalDate.now(), pageable);
    }
}
