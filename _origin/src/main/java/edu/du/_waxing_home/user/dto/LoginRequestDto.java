package edu.du._waxing_home.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String username;
    private String password;

    @Builder
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
