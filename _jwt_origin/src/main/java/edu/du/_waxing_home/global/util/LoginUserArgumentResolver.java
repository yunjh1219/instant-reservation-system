package edu.du._waxing_home.global.util;

import edu.du._waxing_home.global.error.exception.InvalidLoginUserException;
import edu.du._waxing_home.global.filter.CustomUserDetails;
import edu.du._waxing_home.global.security.Login;
import edu.du._waxing_home.user.domain.Role;
import edu.du._waxing_home.user.dto.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Login 어노테이션이 있는지 확인
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null) throw new InvalidLoginUserException();

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Role role = customUserDetails.getAuthorities().stream()
                .map(authority -> Role.ofValue(authority.getAuthority()))
                .findFirst()
                .orElseThrow(InvalidLoginUserException::new);

        return LoginUser.builder()
                .userName(customUserDetails.getUsername()) // userName을 사용하여 로그인 사용자 이름 설정
                .role(role) // role을 설정
                .build();
    }
}
