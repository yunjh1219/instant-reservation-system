package edu.du._waxing_home.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"), USER("USER");
    private final String key;

    public static Role ofValue(String value) {
        Role result;

        if (Objects.equals(value, "ADMIN")) {
            result = Role.ADMIN;
        } else if (Objects.equals(value, "USER")) {
            result = Role.USER;
        } else {
            result = null;
        }

        return result;
    }
}