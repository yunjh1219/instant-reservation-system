package edu.du._waxing_home.global.security;

import edu.du._waxing_home.global.filter.CustomUserDetails;
import edu.du._waxing_home.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
@Getter
public class JwtProvider {

    private final Key secretKey;
    private final Long accessTokenExpirationPeriod;
    private final String accessHeader;
    private final Long refreshTokenExpirationPeriod;
    private final String refreshHeader;
    private static final String BEARER = "Bearer ";

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey,
                       @Value("${jwt.access.expiration}") Long accessTokenExpirationPeriod,
                       @Value("${jwt.access.header}") String accessHeader,
                       @Value("${jwt.refresh.expiration}") Long refreshTokenExpirationPeriod,
                       @Value("${jwt.refresh.header}") String refreshHeader) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationPeriod = accessTokenExpirationPeriod;
        this.accessHeader = accessHeader;
        this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod;
        this.refreshHeader = refreshHeader;
    }

    public Token createToken(String userName, Role role) {
        AccessToken accessToken = AccessToken.builder()
                .header(accessHeader)
                .data(createAccessToken(userName, role))
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .header(refreshHeader)
                .data(createRefreshToken())
                .build();

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 3.isTokenValid 메서드
    // 이 메서드는 주어진 JWT가 유효한지 확인 유효-true / 비유효-false
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 4.getAuthentication 메서드
    // 이 메서드는 JWT에서 사용자 정보를 추출하고, 해당 정보를 사용하여 Authentication 객체를 생성
    // CustomUserDetails.of(extractToken(token))는 JWT의 claims를 사용하여 사용자 정보를 반환하고, 이를 바탕으로 Authentication 객체를 생성
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = CustomUserDetails.of(extractToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    // 5. extractToken 메서드
    // 주어진 JWT에서 **클레임(Claims)**을 추출하는 메서드입니다.
    public Claims extractToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private String createAccessToken(String userNum, Role role) { //Type type
        return Jwts.builder()
                .setClaims(Map.of("role", role))
                .setSubject(userNum)
                .setExpiration(expireTime(accessTokenExpirationPeriod))
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken() {
        return Jwts.builder()
                .setExpiration(expireTime(refreshTokenExpirationPeriod))
                .signWith(secretKey)
                .compact();
    }

    private Date expireTime(Long expirationPeriod) {
        return new Date(System.currentTimeMillis() + expirationPeriod);
    }

}
