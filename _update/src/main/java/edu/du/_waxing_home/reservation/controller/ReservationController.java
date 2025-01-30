package edu.du._waxing_home.reservation.controller;

import edu.du._waxing_home.reservation.dto.ReservationFormDto;
import edu.du._waxing_home.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservation")
    public String reservation(){
        return "pages/reservation/reservation";
    }

    @PostMapping("/reservation/write")
    public ResponseEntity<String> submitReservation(@RequestBody ReservationFormDto reservationFormDto) {
        reservationService.processReservationForm(reservationFormDto);
        return ResponseEntity.ok("예약이 성공적으로 완료되었습니다.");
    }


}
