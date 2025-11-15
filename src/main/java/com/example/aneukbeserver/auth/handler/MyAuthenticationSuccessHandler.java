package com.example.aneukbeserver.auth.handler;

import com.example.aneukbeserver.auth.dto.GeneratedToken;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
// OAuth2 인정에 성공했을 경우 성공 처리
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private final MemberRepository memberRepository;

    private final String googleRedirectUri;

    private final String kakaoRedirectUri;

    private final String naverRedirectUri;

    public MyAuthenticationSuccessHandler(
            JwtUtil jwtUtil,
            MemberRepository memberRepository,
            @Value("${spring.jwt.success-redirect.google:http://localhost:3000}") String googleRedirectUri,
            @Value("${spring.jwt.success-redirect.kakao:http://localhost:3000}") String kakaoRedirectUri,
            @Value("${spring.jwt.success-redirect.naver:http://localhost:3000}") String naverRedirectUri) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.googleRedirectUri = googleRedirectUri;
        this.kakaoRedirectUri = kakaoRedirectUri;
        this.naverRedirectUri = naverRedirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            log.warn("Unsupported authentication type: {}", authentication.getClass().getSimpleName());
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAuthorities().stream()
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .getAuthority();

        if (email == null) {
            log.error("OAuth2 authentication succeeded but email was null. Aborting redirect.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "OAuth2 provider did not return an email address.");
            return;
        }

        // 회원 존재 여부 확인 및 저장
        boolean isExist = Boolean.TRUE.equals(oAuth2User.<Boolean>getAttribute("exist"));
        if (!isExist) {
            Member newMember = new Member();
            newMember.setEmail(email);
            String nickname = Optional.ofNullable(oAuth2User.<String>getAttribute("name"))
                    .orElse(email);
            newMember.setName(nickname);
            newMember.setUserRole(role);
            memberRepository.save(newMember);
        }

        // JWT 토큰 생성
        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("JWT Token = {}", token.getAccessToken());

        String redirectUri = resolveRedirectUri(oauthToken.getAuthorizedClientRegistrationId());

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

    private String resolveRedirectUri(String registrationId) {
        if (registrationId == null) {
            log.warn("Registration ID is null. Falling back to Google redirect URI.");
            return googleRedirectUri;
        }

        switch (registrationId.toLowerCase()) {
            case "kakao":
                return kakaoRedirectUri;
            case "naver":
                return naverRedirectUri;
            case "google":
                return googleRedirectUri;
            default:
                log.warn("Unknown registration ID: {}. Falling back to Google redirect URI.", registrationId);
                return googleRedirectUri;
        }
    }

}
