package edu.du._waxing_home.user.controller;

import edu.du._waxing_home.global.security.JwtProvider;
import edu.du._waxing_home.global.security.Token;
import edu.du._waxing_home.user.domain.Role;
import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.dto.LoginRequestDto;
import edu.du._waxing_home.user.dto.UserRequestDto;
import edu.du._waxing_home.user.repository.UserRepository;
import edu.du._waxing_home.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
//변경해야함
    @Autowired
    private AuthService authService;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    //메인페이지
    @GetMapping("/")
    public String main() {
        // SecurityContext에서 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
        } else {
            System.out.println("No authentication found.");
        }

        return "pages/main";
    }


    @GetMapping("/signupone")
    public String signOnePage(){
        return "pages/auth/signupone";
    }

    // 약관 동의 완료 후 회원가입 페이지로 이동
    @PostMapping("/signupone")
    public String processSignupOne(UserRequestDto userRequestDto) {
        // 모든 필수 동의 확인 로직 (JavaScript로도 확인되지만 서버에서도 확인)
        if (!userRequestDto.isAgreeTerms() ||
                !userRequestDto.isAgreePrivacyPurpose() ||
                !userRequestDto.isAgreePrivacyPeriod()) {
            return "redirect:/signupone?error";
        }
        return "redirect:/signuptwo";
    }

    // 회원가입 페이지
    @GetMapping("/signuptwo")
    public String signupTwoPage(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        return "pages/auth/signuptwo";
    }

    // 회원가입 완료 처리
    @PostMapping("/signup")
    public String registerUser(UserRequestDto userRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "pages/auth/signuptwo"; // 에러 발생 시 회원가입 페이지로 이동
        }

        try {
            authService.registerUser(userRequestDto);
        } catch (IllegalArgumentException e) {
            result.reject("signupError", e.getMessage());
            return "pages/auth/signuptwo";
        }

        return "redirect:/login?success"; // 회원가입 성공 후 로그인 페이지로 리다이렉트
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "pages/auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        try {
            // 로그인 요청 처리
            Token token = authService.login(new LoginRequestDto(username, password));

            // JWT 토큰을 쿠키에 저장 (브라우저에서 클라이언트가 관리)
            Cookie cookie = new Cookie("JWT_TOKEN", token.getAccessToken().getData());
            cookie.setHttpOnly(true); // 클라이언트 스크립트에서 접근 못하게 설정
            cookie.setPath("/"); // 모든 경로에서 사용할 수 있도록 설정
            response.addCookie(cookie);

            return "redirect:/"; // 로그인 성공 후 리다이렉트

        } catch (IllegalArgumentException e) {
            return "pages/auth/login"; // 로그인 실패 시 로그인 페이지로 돌아가기
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // JWT_TOKEN 쿠키를 삭제하기 위해 만료 시간을 과거로 설정
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true); // 클라이언트 스크립트에서 접근 불가
        cookie.setPath("/"); // 모든 경로에서 사용할 수 있도록 설정
        cookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 쿠키 삭제
        response.addCookie(cookie); // 쿠키를 응답에 추가하여 브라우저가 삭제하도록 유도

        return "redirect:/"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }
}

