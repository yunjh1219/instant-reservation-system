package edu.du._waxing_home.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;



    // 예약일로 예약을 검색하는 메서드 (LocalDate로 변경)
    public List<Reservation> findByReservationDate(LocalDate reservationDate) {
        return reservationRepository.findByReservationDate(reservationDate);
    }

    // 모든 예약을 반환하는 메서드
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    // 예약 저장 또는 업데이트 (Reservation을 저장하는 메서드)
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }



    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }



    // 예약 상태를 'y' (확정)로 변경
    public Reservation confirmReservation(Long id) {
        Reservation reservation = getReservationById(id);
        if (reservation != null) {
            reservation.setStatus("y");
            return saveReservation(reservation);
        }
        return null;
    }

    // 예약 상태를 'n' (취소)로 변경
    public Reservation cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);
        if (reservation != null) {
            reservation.setStatus("n");
            return saveReservation(reservation);
        }
        return null;
    }

    public List<Reservation> getReservationsByCustomerId(Long id) {
        return reservationRepository.findByCustomerId(id);
    }


}