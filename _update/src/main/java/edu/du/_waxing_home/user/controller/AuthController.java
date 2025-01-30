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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {

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
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {

        try {
            LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);
            Token token = authService.login(loginRequestDto);


            // 액세스 토큰을 HTTP-only 쿠키로 설정
            Cookie accessTokenCookie = new Cookie("accessToken", token.getAccessToken().getData());
            accessTokenCookie.setHttpOnly(true); // JavaScript에서 접근할 수 없도록 설정
            accessTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정 (실제 운영 환경에서)
            accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키를 사용할 수 있도록 설정
            accessTokenCookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (1시간)

            // 리프레시 토큰도 동일하게 설정
            Cookie refreshTokenCookie = new Cookie("refreshToken", token.getRefreshToken().getData());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(3600); // 리프레시 토큰 유효 시간

            // 쿠키를 응답에 추가
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            model.addAttribute("message", "로그인 성공!");


            return "redirect:/"; // 홈 페이지로 리다이렉트

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "pages/auth/login"; // 로그인 실패 시 로그인 페이지로 돌아감
        }
    }

}


