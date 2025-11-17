package com.example.aneukbeserver.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuestLoginResponseDto {
    private Long memberId;
    private String email;
    private String name;
    private String role;
    private String accessToken;
    private String refreshToken;
}

