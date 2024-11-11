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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
// OAuth2 인정에 성공했을 경우 성공 처리
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User로 캐스팅하여 인증된 사용자의 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 이메일 가져오기
        String email = oAuth2User.getAttribute("email");

        // 서비스 제공 플랫폼(google, kakao, naver)이 어딘지 가져오기
        String provider = oAuth2User.getAttribute("provider");

        // 로그인한 회원 존재 여부 가져오기
        boolean isExist = oAuth2User.getAttribute("exist");

        // OAuth2User로 부터 Role 가져오기
        String role = oAuth2User.getAuthorities().stream().findFirst() // 첫번째 Roler 찾기
                .orElseThrow(IllegalAccessError::new) // 존재하지 않을시 예외 던지기
                .getAuthority(); // Role 가져오기


        if(!isExist) {
            // 회원이 존재하지 않을 경우
            // 회원을 DB에 저장
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(oAuth2User.getName());
            newMember.setUserRole(role);

            memberRepository.save(newMember);
        }

        // jwt token 발행
        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("jwt Token = {}", token.getAccessToken());

//        // accessToken을 쿼리 스트링에 담는 url
//        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:7010/login-success")
//                .queryParam("accessToken", token.getAccessToken())
//                .build()
//                .encode(StandardCharsets.UTF_8)
//                .toUriString();
//
//        log.info("redirect 준비");

//        // 로그인 확인 페이지로 리다이렉트
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        // JSON으로 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 응답 데이터 생성
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new LoginSuccessResponse("success", token.getAccessToken())));
    }

}
