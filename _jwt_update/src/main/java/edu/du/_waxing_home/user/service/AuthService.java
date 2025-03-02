package edu.du._waxing_home.user.service;

import edu.du._waxing_home.global.security.JwtProvider;
import edu.du._waxing_home.global.security.Token;
import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.dto.LoginRequestDto;
import edu.du._waxing_home.user.dto.UserRequestDto;
import edu.du._waxing_home.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    //로그인
    @Transactional
    public Token login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 조회 시 존재하지 않으면 null 반환
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        Token token = jwtProvider.createToken(user.getUsername(), user.getRole());

        user.updateRefreshToken(token.getRefreshToken().getData());

        return token;
    }


    /**
     * 회원가입 로직
     * @param userRequestDto 회원가입 요청 데이터
     * @throws IllegalArgumentException 비밀번호 불일치 또는 중복된 아이디인 경우 예외 발생
     */
    public void registerUser(UserRequestDto userRequestDto) {
        // 아이디 중복 검사
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        // 비밀번호 일치 여부 검사
        if (!userRequestDto.getPassword().equals(userRequestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        // User 엔티티 생성 및 데이터 저장
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(encodedPassword);  // 암호화된 비밀번호 저장
        user.setName(userRequestDto.getName());
        user.setAddress(userRequestDto.getAddress());
        user.setPhone(userRequestDto.getPhone());
        user.setEmail(userRequestDto.getEmail());
        user.setGender(userRequestDto.getGender());

        // 데이터베이스에 저장
        userRepository.save(user);
    }


}