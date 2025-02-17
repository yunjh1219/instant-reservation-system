package edu.du._waxing_home.reservation;

import edu.du._waxing_home.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    // 예약 목록을 보여주고 예약일로 검색할 수 있는 메서드
    @GetMapping("/list")
    public String viewReservations(@RequestParam(name = "reservationDate", required = false) String reservationDate, Model model) {
        List<Reservation> reservations;

        // 예약일 필터링
        if (reservationDate != null && !reservationDate.isEmpty()) {
            LocalDate parsedDate = LocalDate.parse(reservationDate);
            reservations = reservationService.findByReservationDate(parsedDate);
        } else {
            reservations = reservationService.findAllReservations();  // 모든 예약 조회
        }

        // 모델에 예약 목록과 검색 날짜를 추가
        model.addAttribute("reservations", reservations);
        model.addAttribute("searchDate", reservationDate);
        return "pages/admin/reservation/reservationlist";  // Thymeleaf 템플릿 반환
    }


    // 예약 폼을 보여주는 GET 요청 핸들러
    @GetMapping("/form")
    public String showReservationForm() {
        return "reservation-form";  // Thymeleaf 템플릿 이름 (예: reservation-form.html)
    }



    // 예약 확정 처리
    @PostMapping("/confirm/{id}")
    public String confirmReservation(@PathVariable("id") Long id) {
        // 예약 정보 가져오기
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            // 예약 상태를 'y'로 업데이트 (확정)
            reservation.setStatus("y");
            reservationService.saveReservation(reservation);  // 상태 저장
        }
        return "redirect:/reservation/list";  // 예약 현황 페이지로 리다이렉트
    }


    // 예약 취소 처리
    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable("id") Long id) {
        // 예약 정보 가져오기
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            // 예약 상태를 'n'으로 업데이트 (취소)
            reservation.setStatus("n");
            reservationService.saveReservation(reservation);  // 상태 저장
        }
        return "redirect:/reservation/list";  // 예약 현황 페이지로 리다이렉트
    }


}