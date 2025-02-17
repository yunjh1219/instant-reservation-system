package edu.du._waxing_home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    // 생성자 주입을 통해 DataSource를 주입받습니다.
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // PasswordEncoder 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // HTTP 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화 (비회원 요청을 허용할 때 필요할 수 있음)
                
                // 세션 관리 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션이 필요할 때만 생성
                .and()

                // 세션 관리 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션이 필요할 때만 생성
                .and()

                // 요청에 대한 접근 권한 설정
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()  // 인증 없이 접근 가능 (예약)
                .antMatchers("/**", "/css/**", "/js/**", "/img/**", "/reservation/**").permitAll()  // 로그인, 회원가입, CSS, JS, 이미지 파일은 인증 없이 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")  // /admin 하위 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                .anyRequest().authenticated()  // 나머지 경로는 인증 필요
                .and()

                // 로그인 설정
                .formLogin()
                .loginPage("/login")  // 사용자 정의 로그인 페이지
                .permitAll()  // 로그인 페이지는 누구나 접근 가능
                .usernameParameter("username")  // 로그인 폼에서 사용될 username 파라미터 이름 (기본값은 "username")
                .passwordParameter("password")  // 로그인 폼에서 사용될 password 파라미터 이름 (기본값은 "password")
                .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉트될 페이지 (홈페이지 등)
                .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트될 페이지
                .and()

                // 로그아웃 설정
                .logout()
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/")  // 로그아웃 성공 후 리다이렉트될 페이지 (홈페이지 등)
                .permitAll();




    }

    // AuthenticationManager 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 데이터베이스에서 사용자 정보를 조회하여 인증 처리를 할 수 있도록 설정
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, true FROM user WHERE username = ?")  // username을 기반으로 사용자 조회
                .authoritiesByUsernameQuery("SELECT username, role FROM user WHERE username = ?");  // username을 기반으로 권한 조회
    }

    // AuthenticationManager 빈으로 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}