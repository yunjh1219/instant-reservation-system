package edu.du._waxing_home.global.config;

import edu.du._waxing_home.global.filter.JwtAuthenticationFilter;

import edu.du._waxing_home.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화 (필요에 따라)
                // 요청에 대한 접근 권한 설정

                .authorizeRequests()
                .antMatchers("/api/**").permitAll()  // 인증 없이 접근 가능 (예약)
                .antMatchers("/**", "/css/**", "/js/**", "/img/**", "/reservation/**").permitAll()  // 로그인, 회원가입, CSS, JS, 이미지 파일은 인증 없이 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")  // /admin 하위 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
                .and()
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .logout()
                .permitAll();  // 로그아웃 기능을 누구나 사용할 수 있도록 허용

        // JWT 인증 필터 추가
        http.addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }
}