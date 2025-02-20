package edu.du._waxing_home.user.dto;

import edu.du._waxing_home.user.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUser {
    private String userName;
    private Role role;

    @Builder
    public LoginUser(String userName, Role role) {
        this.userName = userName;
        this.role = role;
    }
}
