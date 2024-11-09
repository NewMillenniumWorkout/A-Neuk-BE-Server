package com.example.aneukbeserver.auth.jwt;

import com.example.aneukbeserver.auth.dto.GeneratedToken;
import com.example.aneukbeserver.service.RefreshTokenService;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final RefreshTokenService tokenService;
    private String secretKey;

    @PostConstruct
    protected void init() {
            secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public GeneratedToken generateToken(String email, String role) {
        // refreshToken 과 accessToken 생성
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        // 토큰 디비에 저장
        tokenService.saveTokenInfo(email, refreshToken, accessToken);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateRefreshToken(String email, String role) {
        // 토큰의 유효 기간을 밀리초 단위로 설정
        long refreshPeriod = 1000L * 60L * 60L * 24L * 7; // 1주

        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        // 현재 시간과 날짜를 가져온다.
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // payload를 구성하는 속성 정의
                .setIssuedAt(now) // 발행일자
                .setExpiration(new Date(now.getTime() + refreshPeriod)) // 토큰 만료일시 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 30L; // 30분
//        long tokenPeriod = 1000L * 2L; // 2초

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // payload 구성하는 속성 정의
                .setIssuedAt(now) // 발행일자
                .setExpiration(new Date(now.getTime() + tokenPeriod)) // 토큰 만료일시 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 지정된 서명 알괴즘과 비밀키를 사용하여 토큰 서명
                .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey) // 비밀키를 설정하여 파싱
                    .parseClaimsJws(token); // 주어진 토큰을 파싱하여 Claims 객체 생성

            return claims.getBody()
                    .getExpiration()
                    .after(new Date()); // 만료 시간이 현재 이산 이후인지 확인하여 유효성 검사 결과 반환
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 Email을 추춘
    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 ROLE(권한)만 추출
    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }

}
