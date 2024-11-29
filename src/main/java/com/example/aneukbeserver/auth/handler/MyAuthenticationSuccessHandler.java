package com.example.aneukbeserver.auth.handler;

import com.example.aneukbeserver.auth.dto.GeneratedToken;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
// OAuth2 인정에 성공했을 경우 성공 처리
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAuthorities().stream()
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .getAuthority();

        // 회원 존재 여부 확인 및 저장
        boolean isExist = oAuth2User.getAttribute("exist");
        if (!isExist) {
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(oAuth2User.getName());
            newMember.setUserRole(role);
            memberRepository.save(newMember);
        }

        // JWT 토큰 생성
        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("JWT Token = {}", token.getAccessToken());

        String redirectUri = "http://localhost:3000"; // 테스트용 로컬 URI

        // 이메일과 인코딩된 액세스 토큰을 쿼리 파라미터로 추가
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("email", email)
                .queryParam("accessToken", token.getAccessToken())
                .build()
                .toUriString();

        log.info("Redirecting to: {}", targetUrl);

        // 리디렉션
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

}
