package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chatMessages.ChatMessageDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessages;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryAiResponseDTO;
import com.example.aneukbeserver.domain.diary.DiaryDTO;
import com.example.aneukbeserver.domain.diary.MonthDiaryDTO;
import com.example.aneukbeserver.domain.diaryParagraph.SelectParagraphDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.DiaryService;
import com.example.aneukbeserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/get-diary")
@Tag(name = "Get Diary Controller", description = "일기를 조건에 따라 가져오는 컨트롤러")
public class GetDiaryController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DiaryService diaryService;

    @Operation(summary = "전체 일기 가져오기", description = "사용자의 전체 일기를 가져옵니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "사용자의 일기가 존재하지 않습니다.")

    })
    @GetMapping("/all")
    public ResponseEntity<StatusResponseDto> getAllDiaries(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        List<DiaryDTO> diaries = diaryService.getAllDiary(member.get());

        if (diaries == null)
            return ResponseEntity.badRequest().body(addStatus(401, "사용자의 일기가 존재하지 않습니다."));

        Map<String, Object> result = new HashMap<>();
        result.put("diaries", diaries);

        return ResponseEntity.ok(addStatus(200, result));
    }

    @Operation(summary = "특정 달에 일기가 있는 날짜", description = "특정 달에 일기가 있는 날짜만 diary_id와 함께 response합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "사용자의 일기가 존재하지 않습니다.")

    })
    @GetMapping("/month")
    public ResponseEntity<StatusResponseDto> getMonthDiaries(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam String month) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        List<MonthDiaryDTO> monthDiaries = diaryService.getMonthDiary(member.get(), month);

        if (monthDiaries == null)
            return ResponseEntity.badRequest().body(addStatus(401, "사용자의 일기가 존재하지 않습니다."));

        Map<String, Object> result = new HashMap<>();
        result.put("diaries_with_diary", monthDiaries);

        return ResponseEntity.ok(addStatus(200, result));
    }

}
