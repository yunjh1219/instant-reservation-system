package edu.du._waxing_home.reservation.repository;

import edu.du._waxing_home.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
