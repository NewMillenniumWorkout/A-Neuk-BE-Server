package com.example.aneukbeserver.controller;

import com.example.aneukbeserver.auth.dto.StatusResponseDto;
import com.example.aneukbeserver.auth.jwt.JwtUtil;
import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chatMessages.ChatMessageDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessages;
import com.example.aneukbeserver.domain.collection.Collection;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryAiResponseDTO;
import com.example.aneukbeserver.domain.diary.DiaryDTO;
import com.example.aneukbeserver.domain.diaryParagraph.*;
import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.emotion.EmotionDTO;
import com.example.aneukbeserver.domain.emotion.SaveEmotionDTO;
import com.example.aneukbeserver.domain.member.Member;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;

import com.example.aneukbeserver.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private EmotionService emotionService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SelectedEmotionService selectedEmotionService;

    @Autowired
    private CollectionService collectionService;


    @Value("${spring.ai.url}")
    private String aiUrl;

    @Operation(summary = "1차 일기 생성", description = "현재 진행 중인 채팅을 1차 일기를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")

    })
    @PostMapping("/emotion/list")
    public ResponseEntity<StatusResponseDto> sendInitDiary(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam("chatId") Long chatId) {
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

            String aiChatUrl = aiUrl + "/ai/diary/";

            ResponseEntity<DiaryAiResponseDTO> aiResponse = restTemplate.postForEntity(aiChatUrl, entity, DiaryAiResponseDTO.class);

            log.info(String.valueOf(aiResponse.getBody()));
            diaryService.saveDiary(chat.get(), member.get());
            diaryParagraphService.saveParagraphs(chat.get(), aiResponse.getBody());

            List<DiaryParagraphDTO> paragraphs = aiResponse.getBody().getContent_list().stream()
                    .map(
                            paragraph ->
                            {
                                List<EmotionDTO> emotionDetails = emotionService.getEmotionDetail(paragraph.getRecommend_emotion());
                                DiaryParagraphDTO dto = new DiaryParagraphDTO();
                                dto.setOrder_index(paragraph.getOrder_index());
                                dto.setOriginal_content(paragraph.getOriginal_content());
                                dto.setRecommend_emotion(emotionDetails); // EmotionDTO 리스트로 설정
                                return dto;
                            }).toList();


            SelectParagraphDTO selectParagraphDTO = new SelectParagraphDTO();
            selectParagraphDTO.setDiary_id(diaryService.getDiaryIdByChatId(chat.get()));
            selectParagraphDTO.setContent_list(paragraphs);

            return ResponseEntity.ok(addStatus(200, selectParagraphDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(addStatus(500, "Error communicating with AI server : " + e.getMessage()));
        }

    }

    @Operation(summary = "2차(최종)일기 생성", description = "재구성된 문장을 모아서 최종 일기를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")

    })
    @GetMapping("/second-generate")
    public ResponseEntity<StatusResponseDto> getFinalDiary(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestParam("diaryId") Long diaryId) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);
        Optional<Diary> diary = diaryService.getDiary(diaryId);

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));
        if (diary.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "Diary가 존재하지 않습니다."));

        List<Emotion> emotionList = diary.get().getParagraphs().stream()
                .flatMap(paragraph -> paragraph.getEmotionList().stream())
                .map(SelectedEmotion::getEmotion) // Emotion 객체를 반환
                .distinct()
                .toList();

        DiaryDTO diaryDTO = new DiaryDTO();
        diaryDTO.setDiary_id(diaryId);
        diaryDTO.setDate(diary.get().getCreatedDate());
        diaryDTO.setContent(diaryService.mergeParagraph(diary.get().getParagraphs()));
        diaryDTO.setImageUrl(diary.get().getImageUrl());
        diaryDTO.setEmotionList(emotionList);

        return ResponseEntity.ok(addStatus(200, diaryDTO));
    }

    @Operation(summary = "감정 선택 후 저장", description = "각 문단별로 감정 선택을 후 감정들을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")

    })
    @PostMapping("/emotion/save")
    public ResponseEntity<StatusResponseDto> saveParagraphEmotion(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestBody SaveEmotionDTO request) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);
        log.info(String.valueOf(request));
        Optional<DiaryParagraph> diaryParagraph = diaryParagraphService.findByParagraphId(request.getDiary_id(), request.getOrder_index());

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));
        if (diaryParagraph.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "Paragraph이 존재하지 않습니다."));

        List<String> emotion_list = request.getEmotions();

        List<Emotion> emotionList = emotionService.getEmotionObjectsByNames(emotion_list);

        selectedEmotionService.saveSelectedEmotions(diaryParagraph.get(), emotionList);
        collectionService.saveEmotionCollection(emotionList, member.get());

        return ResponseEntity.ok().body(addStatus(200, "성공적으로 저장되었습니다."));
    }



        @Operation(summary = "감정 선택 후 문장 바뀜", description = "각 문단별로 감정 선택을 통해 문단을 재구성하여 response 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다."),
            @ApiResponse(responseCode = "400", description = "사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "401", description = "채팅이 존재하지 않습니다.")

    })
    @PostMapping("/emotion/select")
    public ResponseEntity<StatusResponseDto> remakeParagraph(@Parameter(hidden = true) @RequestHeader("Authorization") final String accessToken, @RequestBody RemadeRequestDTO request) {
        String userEmail = jwtUtil.getEmail(accessToken.substring(7));
        Optional<Member> member = memberService.findByEmail(userEmail);
        log.info(String.valueOf(request));
        Optional<DiaryParagraph> diaryParagraph = diaryParagraphService.findByParagraphId(request.getDiary_id(), request.getOrder_index());

        if (member.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(400, "사용자가 존재하지 않습니다."));
        if (diaryParagraph.isEmpty())
            return ResponseEntity.badRequest().body(addStatus(401, "Paragraph이 존재하지 않습니다."));

        List<String> emotion_list = request.getEmotions();

        Map<String, Object> aiRequest = Map.of(
                "original_content", request.getOriginal_content(),
                "emotion_list", emotion_list
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(aiRequest, headers);

            String aiChatUrl = aiUrl + "/ai/remake/";

            log.info(String.valueOf(restTemplate.postForEntity(aiChatUrl, entity, Map.class)));
            ResponseEntity<Map> aiResponse = restTemplate.postForEntity(aiChatUrl, entity, Map.class);

            diaryParagraphService.updateDiaryParagraph(diaryParagraph.get(), aiResponse.getBody().get("remade_content").toString());

            RemadeParagraphDTO remadeParagraphDTO = new RemadeParagraphDTO();
            remadeParagraphDTO.setParagraph_id(diaryParagraph.get().getId());
            remadeParagraphDTO.setOrder_index(diaryParagraph.get().getOrderIndex());
            remadeParagraphDTO.setFinal_content(aiResponse.getBody().get("remade_content").toString());

            return ResponseEntity.ok(addStatus(200, remadeParagraphDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(addStatus(500, "Error communicating with AI server : " + e.getMessage()));
        }
    }


}
