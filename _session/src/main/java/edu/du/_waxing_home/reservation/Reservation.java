package edu.du._waxing_home.reservation;

import edu.du._waxing_home.customer.Customer;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_sequence", initialValue = 1000, allocationSize = 1)
    @NotNull  // null 값 허용하지 않음
    private Long reservationId;  // 예약번호는 자동 생성

    @ManyToOne
    @JoinColumn(name = "id")
    private Customer customer;

    @NotNull  // null 값 허용하지 않음
    private LocalDate reservationDate;  // 예약 날짜
    @NotNull  // null 값 허용하지 않음
    private int hourselect;  // 예약 시간 (시)
    @NotNull  // null 값 허용하지 않음
    private int minutesselect;  // 예약 분 (분)

    private String treatment1;  // 첫 번째 치료 항목
    private String treatment2;  // 두 번째 치료 항목
    private String additionalText;  // 추가 설명

    private String status;  // "y" (확정), "n" (취소)

    @NotNull  // null 값 허용하지 않음
    private LocalDateTime createdat;  // 예약 생성일

    @NotNull  // null 값 허용하지 않음
    private LocalDateTime updatedat;  // 예약 수정일



    // 예약 생성 시 자동으로 날짜 설정
    @PrePersist
    public void onCreate() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

        if (reservationDate == null) {
            reservationDate = LocalDate.now(koreaZoneId);  // 예약 날짜를 현재 날짜로 설정
        }

        if (createdat == null) {
            createdat = LocalDateTime.now(koreaZoneId);  // 생성일 설정
        }
        updatedat = LocalDateTime.now(koreaZoneId);  // 수정일은 생성 시 설정
    }

    // 예약 수정 시 자동으로 날짜 설정
    @PreUpdate
    public void onUpdate() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        updatedat = LocalDateTime.now(koreaZoneId);  // 수정일 업데이트
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationDate=" + reservationDate +
                ", hourselect=" + hourselect +
                ", minutesselect=" + minutesselect +
                ", treatment1='" + treatment1 + '\'' +
                ", treatment2='" + treatment2 + '\'' +
                ", additionalText='" + additionalText + '\'' +
                ", status='" + status + '\'' +
                ", patient_id=" + (customer != null ? getCustomer().getId() : null) + // patient ID만 출력
                ", createdat=" + createdat +
                ", updatedat=" + updatedat +
                '}';
    }
}