package edu.du._waxing_home.appointment;

import edu.du._waxing_home.dto.AppointmentForm;
import edu.du._waxing_home.customer.Customer;
import edu.du._waxing_home.customer.CustomerRepository;
import edu.du._waxing_home.reservation.Reservation;
import edu.du._waxing_home.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    public void processAppointmentForm(AppointmentForm appointmentForm) {
        // 1. 환자 정보 확인
        Customer existingPatient = customerRepository.findByPhonenumber(appointmentForm.getPhonenumber());

        Customer customer;
        if (existingPatient != null) {
            // 2. 환자 정보가 있으면 기존 환자 사용
            customer = existingPatient;
        } else {
            // 3. 환자 정보가 없으면 새로운 환자 생성
            customer = new Customer();
            customer.setName(appointmentForm.getName());
            customer.setEmail(appointmentForm.getEmail());
            customer.setPhonenumber(appointmentForm.getPhonenumber());
            customer.setGender(appointmentForm.getGender());

            // 새로운 환자 저장
            customerRepository.save(customer);  // patient 저장
        }

        // 4. 예약 정보 생성
        Reservation reservation = new Reservation();
        reservation.setReservationDate(appointmentForm.getReservationDate());
        reservation.setHourselect(appointmentForm.getHourselect());
        reservation.setMinutesselect(appointmentForm.getMinutesselect());
        reservation.setTreatment1(appointmentForm.getTreatment1());
        reservation.setTreatment2(appointmentForm.getTreatment2());
        reservation.setAdditionalText(appointmentForm.getAdditionalText());
        reservation.setCustomer(customer);  // 환자와 예약 연결

        // 5. 예약 저장
        reservationRepository.save(reservation);  // 예약 저장
    }
}