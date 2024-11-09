package com.example.aneukbeserver.config;

import com.example.aneukbeserver.auth.CustomOAuth2UserService;
import com.example.aneukbeserver.auth.jwt.JwtAuthFilter;
import com.example.aneukbeserver.auth.handler.MyAuthenticationFailureHandler;
import com.example.aneukbeserver.auth.handler.MyAuthenticationSuccessHandler;
import com.example.aneukbeserver.auth.jwt.JwtException;
import com.example.aneukbeserver.auth.jwt.JwtExceptionFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;

    @Autowired
    private JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 기능 비활성화
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/", "/login/**", "/h2-console/**", "/token/**").permitAll()
                                .anyRequest().authenticated()
                ) // 요청에 대한 인증 설정
                .oauth2Login(oauth2 -> oauth2 // OAuth2 로그인 설정 시작
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보를 가져오는 엔드포인트와 사용자 서비스 설정
                        .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패 시 처리할 핸들러 지정
                        .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공 시 처리할 핸들러 지정
                )
                .formLogin(formLogin ->
                        formLogin
                                .successHandler(oAuth2LoginSuccessHandler)
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login") // 임시
                );


        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }
}
