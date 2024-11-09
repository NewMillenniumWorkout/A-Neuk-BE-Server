package com.example.aneukbeserver.auth.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginFailureResponse {
    private String status;
    private String message;
}
