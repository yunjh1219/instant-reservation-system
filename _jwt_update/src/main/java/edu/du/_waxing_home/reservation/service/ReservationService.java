package edu.du._waxing_home.reservation.service;

import edu.du._waxing_home.customer.domain.Customer;
import edu.du._waxing_home.customer.repository.CustomerRepository;
import edu.du._waxing_home.reservation.domain.Reservation;
import edu.du._waxing_home.reservation.dto.ReservationFormDto;
import edu.du._waxing_home.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public void processReservationForm(ReservationFormDto reservationFormDto) {
        // 1. 환자 정보 확인
        Customer existingCustomer = customerRepository.findByPhoneAndName(reservationFormDto.getPhone(), reservationFormDto.getName());

        Customer customer;
        if (existingCustomer != null) {
            // 2. 환자 정보가 있으면 기존 환자 사용
            customer = existingCustomer;
        } else {
            // 3. 환자 정보가 없으면 새로운 환자 생성
            customer = new Customer();
            customer.setName(reservationFormDto.getName());
            customer.setEmail(reservationFormDto.getEmail());
            customer.setPhone(reservationFormDto.getPhone());
            customer.setGender(reservationFormDto.getGender());

            // 새로운 환자 저장
            customerRepository.save(customer);  // patient 저장
        }

        // 4. 예약 정보 생성
        Reservation reservation = new Reservation();
        reservation.setReservationTime(reservationFormDto.getReservationTime());
        reservation.setCategory(reservationFormDto.getCategory());
        reservation.setContent(reservationFormDto.getContent());
        reservation.setCustomer(customer);  // 환자와 예약 연결

        // 5. 예약 저장
        reservationRepository.save(reservation);  // 예약 저장
    }
}