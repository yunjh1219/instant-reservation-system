package edu.du._waxing_home.reservation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ReservationFormDto {
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String content;
    private String category;
    private LocalDateTime reservationTime;

}
