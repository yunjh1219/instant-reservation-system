package edu.du._waxing_home.reservation;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCustomerId(Long id);

    // 예약일로 예약을 검색하는 메서드 (LocalDate로 변경)
    List<Reservation> findByReservationDate(LocalDate reservationDate);


}
