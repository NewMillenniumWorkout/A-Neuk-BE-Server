package com.example.aneukbeserver.auth.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SecurityUserDto {
    private Long id;    // 사용자 번호
    private String email;      // 사용자 이메일
    private String role;       // 사용자 권한 (ROLE_USER, ROLE_ADMIN 등)
    private String name;       // 사용자 이름
}
