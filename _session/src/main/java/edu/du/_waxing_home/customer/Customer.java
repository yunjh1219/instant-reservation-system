package edu.du._waxing_home.customer;

import edu.du._waxing_home.reservation.Reservation;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_sequence", initialValue = 1000, allocationSize = 1)
    @NotNull
    private Long id;  // 환자의 고유 ID (1000부터 시작)

    @NotNull  // null 값 허용하지 않음
    private String name;  // 환자의 이름
    @NotNull  // null 값 허용하지 않음
    private String phonenumber;  // 환자의 전화번호
    @NotNull  // null 값 허용하지 않음
    private String email;  // 환자의 이메일
    @NotNull  // null 값 허용하지 않음
    private String gender;  // 환자의 성별

    @NotNull  // null 값 허용하지 않음
    private LocalDateTime created_at;  // 환자 등록일 (LocalDateTime으로 처리)
    @NotNull  // null 값 허용하지 않음
    private LocalDateTime updated_at;  // 환자 정보 수정일 (LocalDateTime으로 처리)

    // 환자와 예약 간의 관계 설정 (One-to-Many)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation;  // 해당 환자가 가진 예약들

    // 환자 생성 시 자동으로 날짜 설정
    @PrePersist
    public void onCreate() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        if (created_at == null) {
            created_at = LocalDateTime.now(koreaZoneId);
        }
        updated_at = LocalDateTime.now(koreaZoneId);
    }

    // 환자 정보 수정 시 자동으로 날짜 설정
    @PreUpdate
    public void onUpdate() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        updated_at = LocalDateTime.now(koreaZoneId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }


}