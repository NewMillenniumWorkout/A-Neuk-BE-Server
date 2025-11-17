package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.GeneratedToken;
import com.example.aneukbeserver.auth.dto.GuestLoginResponseDto;
import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.refreshToken.RefreshToken;
import com.example.aneukbeserver.domain.refreshToken.RefreshTokenRepository;
import com.example.aneukbeserver.service.MemberService;
import com.example.aneukbeserver.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@Tag(name = "Token Controller", description = "token logout / token refresh")
public class AuthController {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Operation(summary = "게스트 로그인", description = "게스트 전용 임시 계정을 생성하고 JWT 토큰을 발급합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @PostMapping("/guest")
    public ResponseEntity<StatusResponseDto> guestLogin() {
        Member guestMember = memberService.createGuestMember();
        GeneratedToken token = jwtUtil.generateToken(guestMember.getEmail(), guestMember.getUserRole());

        GuestLoginResponseDto response = GuestLoginResponseDto.builder()
                .memberId(guestMember.getId())
                .email(guestMember.getEmail())
                .name(guestMember.getName())
                .role(guestMember.getUserRole())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        return ResponseEntity.ok(addStatus(200, response));
    }

    @Operation(summary = "토큰 로그아웃", description = "accessToken을 기반으로 refreshToken을 삭제합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @PostMapping("/logout")
    public ResponseEntity<StatusResponseDto> logout(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        refreshTokenService.removeRefreshToken(accessToken.substring(7)); //Bearer 제거
        return ResponseEntity.ok(addStatus(200));
    }

    @Operation(summary = "토큰 refresh", description = "AccessToken을 기반으로 RefreshToken을 찾아 새로운 Access Token을 만듭니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @PostMapping("/refresh")
    public ResponseEntity<StatusResponseDto> refresh(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {

        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(accessToken.substring(7)); //Bearer 제거

        log.info(String.valueOf(refreshToken));
        // RefreshToken이 존재하고 유효하다면
        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(resultToken);
            // 새로운 액세스 토큰을 반환해준다.
            return ResponseEntity.ok(addStatus(200, newAccessToken));
        }
        return ResponseEntity.badRequest().body(addStatus(400, "RefreshToken이 존재하지 않거나 유효하지 않습니다"));
    }

}
