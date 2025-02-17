package edu.du._waxing_home.user.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = Role.USER; //
        }
    }

}
