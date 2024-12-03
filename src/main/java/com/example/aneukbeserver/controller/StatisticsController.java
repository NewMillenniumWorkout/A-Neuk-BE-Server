package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.ChatMessagesService;
import com.example.aneukbeserver.service.ChatService;
import com.example.aneukbeserver.service.DiaryService;
import com.example.aneukbeserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
@Tag(name = "Statistics Controller", description = "통계에 필요한 api")
public class StatisticsController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessagesService chatMessagesService;

    @Autowired
    private DiaryService diaryService;
//    @Operation(summary = "랜덤 일기", description = "랜덤 일기를 response 합니다")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "일기가 존재하지 않습니다.")
//
//    })
//    @GetMapping("/monthly")
//    public ResponseEntity<StatusResponseDto> getMonthlyStatistics(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {
//        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
//        Optional<Member> member = memberService.findByEmail(userEmail);
//
//        if (member.isEmpty())
//            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));
//
//
//    }
}