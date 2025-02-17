package edu.du._waxing_home.appointment;

import edu.du._waxing_home.dto.AppointmentForm;
import edu.du._waxing_home.customer.CustomerRepository;
import edu.du._waxing_home.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AppointmentService appointmentService;

    // 폼 데이터를 @ModelAttribute로 바인딩
    @PostMapping("/submit-appointment")
    public ResponseEntity<String> submitAppointment(@RequestBody AppointmentForm appointmentForm) {
        appointmentService.processAppointmentForm(appointmentForm);
        return ResponseEntity.ok("예약이 성공적으로 완료되었습니다.");
    }




}