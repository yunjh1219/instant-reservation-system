package edu.du._waxing_home.reservation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.du._waxing_home.customer.domain.Customer;
import edu.du._waxing_home.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String content; //고객 할 말
    private String category; // 분류

    @Column(name = "reservation_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")  // ISO 8601 형식으로 지정
    private LocalDateTime reservationTime; // 예약 날짜와 시간(분)
    private String status; // 예약 상태 (확정/취소)
    private LocalDateTime createdAt; //예약 생성일
    private LocalDateTime updatedAt; //예약 수정일

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();  // 생성일 설정
        this.updatedAt = LocalDateTime.now();  // 수정일도 생성 시 동일하게 설정
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();  // 수정일 설정
    }


}
