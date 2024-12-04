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
import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;
import com.example.aneukbeserver.service.ChatService;
import com.example.aneukbeserver.service.DiaryService;
import com.example.aneukbeserver.service.MemberService;
import com.example.aneukbeserver.service.S3Service;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private ChatService chatService;

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

    @Operation(summary = "특정 날짜의 일기 가져오기", description = "특정 날짜의 일기 가져오기")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "사용자의 일기가 존재하지 않습니다.")

    })
    @GetMapping("/day")
    public ResponseEntity<StatusResponseDto> getDateDiary(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam String date) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

       DiaryDTO diaryDTO = diaryService.getDateDiary(member.get(), date);

        if (diaryDTO == null)
            return ResponseEntity.badRequest().body(addStatus(401, "일기가 존재하지 않습니다."));

        return ResponseEntity.ok(addStatus(200, diaryDTO));
    }

    @Operation(summary = "chatId로 일기 가져오기", description = "채팅 아이디로 일기 가져오기")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "402", description = "일기가 존재하지 않습니다.")

    })
    @GetMapping("/chat-id")
    public ResponseEntity<StatusResponseDto> getDiaryByChatId(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam Long chatId) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        Optional<Chat> chat = chatService.getChatById(chatId);
        if (chat.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "채팅이 존재하지 않습니다."));
        Long diaryId = diaryService.getDiaryIdByChatId(chat.get());

        Optional<Diary> diary = diaryService.getDiary(diaryId);
        if (diary.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(402, "일기가 존재하지 않습니다."));


        List<Emotion> emotionList = diary.get().getParagraphs().stream()
                .flatMap(paragraph -> paragraph.getEmotionList().stream())
                .map(SelectedEmotion::getEmotion) // Emotion 객체를 반환
                .toList();


        return ResponseEntity.ok(addStatus(200, new DiaryDTO(diary.get().getId(), diary.get().getCreatedDate(), diaryService.mergeParagraph(diary.get().getParagraphs()), diary.get().getImageUrl(), emotionList)));
    }

    @Operation(summary = "diaryId로 일기 가져오기", description = "다이어리 아이디로 일기 가져오기")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "402", description = "일기가 존재하지 않습니다.")

    })
    @GetMapping("/diary-id")
    public ResponseEntity<StatusResponseDto> getDiaryByDiaryId(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam Long diaryId) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        Optional<Diary> diary = diaryService.getDiary(diaryId);
        if (diary.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(402, "일기가 존재하지 않습니다."));


        List<Emotion> emotionList = diary.get().getParagraphs().stream()
                .flatMap(paragraph -> paragraph.getEmotionList().stream())
                .map(SelectedEmotion::getEmotion) // Emotion 객체를 반환
                .toList();

        return ResponseEntity.ok(addStatus(200, new DiaryDTO(diary.get().getId(), diary.get().getCreatedDate(), diaryService.mergeParagraph(diary.get().getParagraphs()), diary.get().getImageUrl(), emotionList)));
    }

}
