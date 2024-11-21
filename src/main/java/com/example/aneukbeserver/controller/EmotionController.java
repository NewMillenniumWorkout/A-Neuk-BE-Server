package com.example.aneukbeserver.controller;


import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.emotion.EmotionRepository;
import com.example.aneukbeserver.domain.emotion.EmotionResponseDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.member.MemberRepository;
import com.example.aneukbeserver.service.EmotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emotion")
@Tag(name = "Emotion Controller", description = "감정 정보를 가져오는 컨트롤러")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberRepository memberService;


    @Operation(summary = "감정 정보 가져오기", description = "id에 따른 감정 정보를 가져옵니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDto> getEmotionInfo(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @PathVariable Long id) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);
        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        EmotionResponseDTO emotionResponseDTO = emotionService.getEmotionInfo(id);

        if (emotionResponseDTO == null)
            return ResponseEntity.badRequest().body(addStatus(401, "해당하는 감정 정보가 존재하지 않습니다"));

        return ResponseEntity.ok(addStatus(200, emotionResponseDTO));
    }
}
