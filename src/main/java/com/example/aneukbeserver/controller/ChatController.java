package com.example.aneukbeserver.controller;

import ch.qos.logback.core.util.TimeUtil;
import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chat.ChatTotalDTO;
import com.example.aneukbeserver.domain.chatMessages.*;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.ChatMessagesService;
import com.example.aneukbeserver.service.ChatService;
import com.example.aneukbeserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.aneukbeserver.auth.dto.StatusResponseDto.addStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Chat Controller", description = "챗봇과의 채팅에 필요한 api")
public class ChatController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessagesService chatMessagesService;

    @Autowired
    private  RestTemplate restTemplate;

    @Operation(summary = "초기 채팅 메시지", description = "오늘 날짜의 채팅이 있다면 그 chatId를 리턴하고, 없다면 새로운 chatId를 리턴합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다.")
    })
    @GetMapping("/init-message")
    public ResponseEntity<StatusResponseDto> sendInitMessage(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));


        InitMessageDTO initMessageDTO = chatService.getInitMessage(member.get());
        return ResponseEntity.ok(addStatus(200, initMessageDTO));

    }

    @Operation(summary = "대화 내역 모두 불러오기", description = "해당 chatId에 해당 하는 대화 내역을 모두 불러옵니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")
    })
    @GetMapping("/total")
    public ResponseEntity<StatusResponseDto> getTotalChat(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam("chatId") Long chatId) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        List<ChatTotalDTO> chatTotalDTO = chatService.getTotalChat(chatId);

        if(chatTotalDTO.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "채팅이 존재하지 않습니다."));

        return ResponseEntity.ok(addStatus(200, chatService.getTotalChat(chatId)));

    }

    @Operation(summary = "메지시 전송", description = "사용자가 챗봇의 질문에 답변할 때 호출합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "chatId와 content가 올바르지 않습니다")
    })
    @PostMapping("/submit-message")
    public ResponseEntity<StatusResponseDto> submitMessage(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestBody Map<String, String> request) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));

        Long chatId = Long.valueOf(request.get("chat_id"));
        String content = request.get("content");


        Optional<Chat> chat = chatService.getChatById(chatId);

        if (chat.isEmpty() || content == null) {
            return ResponseEntity.badRequest().body(addStatus(401, "chatId와 content가 올바르지 않습니다."));
        }

        chatMessagesService.saveUserChat(chat.get(), content);
        List<ChatMessages> chatMessagesList = chatMessagesService.getChatMessages(chatId);


        // AI에게 보낼 메시지 리스트 생성
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

            String aiChatUrl = "http://43.203.232.54:2518/ai/chat/";

            ResponseEntity<ChatAiResponseDTO> aiResponse = restTemplate.postForEntity(aiChatUrl, entity, ChatAiResponseDTO.class);

            chatMessagesService.saveAssistantChat(chat.get(), aiResponse.getBody());

            return ResponseEntity.ok(addStatus(200, aiResponse.getBody()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(addStatus(500, "Error communicating with AI server : " + e.getMessage()));
        }
    }

}
