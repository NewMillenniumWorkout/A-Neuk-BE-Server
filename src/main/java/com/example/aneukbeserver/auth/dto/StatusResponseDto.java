package com.example.aneukbeserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // DTO를 JSON으로 변환시 null 값인 field 제외
public class StatusResponseDto {
    private int status;
    private Object data;

    public StatusResponseDto(int status) {
        this.status = status;
    }

    public static StatusResponseDto addStatus(Integer status) {
        return new StatusResponseDto(status);
    }

    public static StatusResponseDto addStatus(Integer status, Object data) {
        return new StatusResponseDto(status, data);
    }

    public static StatusResponseDto success() {
        return new StatusResponseDto(200);
    }

    public static StatusResponseDto success(Object data) {
        return new StatusResponseDto(200, data);
    }

}
