package com.example.aneukbeserver.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 자동으로 게터, 세터, toString(), equals(), hashCode() 메서드를 생성
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedToken {
    private String accessToken;
    private String refreshToken;
}
