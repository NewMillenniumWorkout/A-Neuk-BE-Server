package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chatMessages.ChatAiResponseDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessageDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessages;
import com.example.aneukbeserver.domain.chatMessages.InitMessageDTO;
import com.example.aneukbeserver.domain.diary.DiaryAiResponseDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.*;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Tag(name = "Diary Controller", description = "일기 생성, 수정 및 최종 일기 저장에 필요한 api")
public class DiaryController {
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

    @Autowired
    private DiaryParagraphService diaryParagraphService;

    @Autowired
    private RestTemplate restTemplate;


    @Operation(summary = "1차 일기 생성", description = "현재 진행 중인 채팅을 1차 일기를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")

    })
    @PostMapping("/emotion/list")
    public ResponseEntity<StatusResponseDto> sendInitMessage(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam("chatId") Long chatId) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);
        Optional<Chat> chat = chatService.getChatById(chatId);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));
        if (chat.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "채팅이 존재하지 않습니다."));

        List<ChatMessages> chatMessagesList = chatMessagesService.getChatMessages(chatId);

        // AI 에게 보낼 메시지 리스트
        List<ChatMessageDTO> messageListForAI = chatMessagesList.stream()
                .map(chatMessages -> new ChatMessageDTO(
                        chatMessages.getType(),
                        chatMessages.getContent()
                ))
                .toList();

        Map<String, Object> aiRequest = Map.of(
                "chat_id", chatId,
                "messages", messageListForAI
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(aiRequest, headers);

            String aiChatUrl = "http://43.203.232.54:2518/ai/diary/";

            ResponseEntity<DiaryAiResponseDTO> aiResponse = restTemplate.postForEntity(aiChatUrl, entity, DiaryAiResponseDTO.class);

            log.info(String.valueOf(aiResponse.getBody()));
            diaryService.saveDiary(chat.get(), member.get());
            diaryParagraphService.saveParagraphs(chat.get(), aiResponse.getBody());

            return ResponseEntity.ok(addStatus(200, aiResponse.getBody()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(addStatus(500, "Error communicating with AI server : " + e.getMessage()));
        }

    }
}
