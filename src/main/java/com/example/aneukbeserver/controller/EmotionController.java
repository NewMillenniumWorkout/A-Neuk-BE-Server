package com.example.aneukbeserver.controller;


import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.dto.SecurityUserDto;
import com.example.aneukbeserver.domain.emotion.EmotionResponseDTO;
import com.example.aneukbeserver.service.EmotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emotion")
@Tag(name = "Emotion Controller", description = "감정 정보를 가져오는 컨트롤러")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;


    @Operation(summary = "감정 정보 가져오기", description = "id에 따른 감정 정보를 가져옵니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDto> getEmotionInfo(@AuthenticationPrincipal SecurityUserDto user, @PathVariable Long id) {
        if (user == null)
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        EmotionResponseDTO emotionResponseDTO = emotionService.getEmotionInfo(id);

        if (emotionResponseDTO == null)
            return ResponseEntity.badRequest().body(addStatus(401, "해당하는 감정 정보가 존재하지 않습니다"));

        return ResponseEntity.ok(addStatus(200, emotionResponseDTO));
    }
}
