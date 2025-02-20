package edu.du._waxing_home.global.filter;

import edu.du._waxing_home.user.domain.Role;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private String username;
    private Role role;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    private CustomUserDetails(String username, Role role, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    public static CustomUserDetails of(String username, Role role, Collection<? extends GrantedAuthority> authorities) {
        return CustomUserDetails.builder()
                .username(username)
                .role(role)
                .authorities(authorities)
                .build();
    }

    public static CustomUserDetails of(Claims claims) {
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        return CustomUserDetails.builder()
                .username(username)
                .role(Role.valueOf(role))  // Role Enum으로 변환하여 설정
                .authorities(convertAuthorities(role))
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 사용자가 만료되지 않았으면 true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 사용자가 잠기지 않았으면 true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 사용자의 자격 증명이 만료되지 않았으면 true
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용자가 활성화되었으면 true
    }

    private static List<? extends GrantedAuthority> convertAuthorities(String... roles) {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}
