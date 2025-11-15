package com.example.aneukbeserver.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 authentication failed: {}", exception.getMessage(), exception);

        // JSON으로 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 응답 데이터 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String failureMessage = "Invalid credentials";

        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            failureMessage = Optional.ofNullable(oAuth2AuthenticationException.getError().getDescription())
                    .filter(StringUtils::hasText)
                    .orElseGet(oAuth2AuthenticationException::getMessage);
        } else if (StringUtils.hasText(exception.getMessage())) {
            failureMessage = exception.getMessage();
        }

        response.getWriter().write(objectMapper.writeValueAsString(new LoginFailureResponse("error", failureMessage)));
        // 인증 실패시 메인 페이지로 이동
//        response.sendRedirect("http://localhost:7010/");

        // JSON 응답 작성
//        response.getWriter().write(errorResponse);
    }
}
