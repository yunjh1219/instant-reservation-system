package edu.du._waxing_home.user.controller;

import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.dto.UserRequestDto;
import edu.du._waxing_home.user.repository.UserRepository;
import edu.du._waxing_home.user.service.AuthService;
import edu.du._waxing_home.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private AuthService authService;

    private final AuthenticationManager authenticationManager;

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

    // 로그인 폼을 제출할 때
//    @PostMapping("/login")
//    public String login(String username, String password, Model model) {
//
//        System.out.println("로그인 메서드 호출됨");  // 메서드가 호출되는지 확인
//
//        // AuthenticationManager를 사용하여 인증 시도
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//
//        // 인증 성공시 SecurityContext에 인증 정보 저장
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        return "redirect:/"; // 로그인 후 리다이렉트할 페이지 (홈페이지 등)
//    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext(); // 로그아웃 후 인증 정보 초기화
        return "redirect:/"; // 로그아웃 후 리다이렉트할 페이지
    }


}
