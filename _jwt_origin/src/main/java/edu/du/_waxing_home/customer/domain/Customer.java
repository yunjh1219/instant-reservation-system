package edu.du._waxing_home.customer.domain;

import edu.du._waxing_home.reservation.domain.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    private String name;
    private String email;
    private String phone;
    private String gender;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    // 엔티티가 저장되기 전에 호출되어 생성일을 설정
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();  // 생성일 설정
        this.updateAt = LocalDateTime.now();  // 생성일은 생성일로 동일하게 설정
    }

    // 엔티티가 업데이트되기 전에 호출되어 수정일을 설정
    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();  // 수정일 설정
    }

}
