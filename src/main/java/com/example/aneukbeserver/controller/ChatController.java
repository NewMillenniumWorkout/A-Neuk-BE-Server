package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.chatMessages.InitMessageDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.service.ChatService;
import com.example.aneukbeserver.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/chat")
@Tag(name = "Chat Controller", description = "챗봇과의 채팅에 필요한 api")
public class ChatController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChatService chatService;

    @GetMapping("/init-message")
    public ResponseEntity<StatusResponseDto> sendInitMessage(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));


        InitMessageDTO initMessageDTO = chatService.getInitMessage(member.get());
        return ResponseEntity.ok(addStatus(200, initMessageDTO));


    }

}
