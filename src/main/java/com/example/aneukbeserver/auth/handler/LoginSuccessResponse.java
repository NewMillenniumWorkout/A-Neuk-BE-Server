package com.example.aneukbeserver.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse {
    private String status;
    private String accessToken;
}
