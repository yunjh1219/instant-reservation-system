package edu.du._waxing_home.user.domain;

import edu.du._waxing_home.reservation.domain.Reservation;
import edu.du._waxing_home.customer.domain.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String refreshToken;

    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = Role.USER; //
        }
    }


    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void invalidateRefreshToken() {
        this.refreshToken = null;
    }

}
