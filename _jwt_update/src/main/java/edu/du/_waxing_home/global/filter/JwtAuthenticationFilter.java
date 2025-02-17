package edu.du._waxing_home.global.filter;

import edu.du._waxing_home.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 쿠키에서 JWT 토큰을 확인
        String bearerToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    bearerToken = "Bearer " + cookie.getValue();  // 쿠키에서 가져온 값을 Authorization 헤더 형식으로 변경
                    break;
                }
            }
        }

        // 2. JWT 토큰이 존재하면 인증 처리
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);  // "Bearer " 부분을 제외한 토큰만 추출

            if (jwtProvider.isTokenValid(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);  // 토큰에서 인증 정보 추출
                SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증 정보를 SecurityContext에 설정
            }
        }

        // 필터 체인을 계속 진행
        filterChain.doFilter(request, response);
    }
}