package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chat.ChatRepository;
import com.example.aneukbeserver.domain.chat.ChatTotalDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessages;
import com.example.aneukbeserver.domain.chatMessages.ChatMessagesRepository;
import com.example.aneukbeserver.domain.chatMessages.InitMessageDTO;
import com.example.aneukbeserver.domain.chatMessages.MessageType;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.diary.DiaryRepository;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessagesRepository chatMessagesRepository;

    @Autowired
    private DiaryRepository diaryRepository;


    private static final List<String> greetings = Arrays.asList(
            "오늘 하루는 어땠어",
            "오늘 어떤 일이 가장 즐거웠어",
            "오늘 누구 만났어",
            "오늘 어디 갔었어",
            "오늘 기분 좋은 일 있었어",
            "오늘 무슨 일 있었어",
            "오늘 어떤 일로 웃었어",
            "오늘 힘들었던 일 있었어",
            "오늘 어디서 시간을 보냈어",
            "오늘 특별한 일이 있었어",
            "오늘 누구랑 이야기했어",
            "오늘 무슨 생각 많이 했어",
            "오늘 날씨 어땠어",
            "오늘 기분 좋을만한 일이 있었어"
    );

    private static final int MIN_MESSAGE_NUM = 5;

    private String randomGreeting() {
        Random random = new Random();
        return greetings.get(random.nextInt(greetings.size())) + (random.nextBoolean() ? "?" : "!");
    }

    @Transactional
    public InitMessageDTO getInitMessage(Member member) {
        // 사용자 ID로 채팅 목록을 최근 순서로 정렬하여 가져옴
        Optional<Chat> latestChatOpt = chatRepository.findTopByMemberIdOrderByCreatedDateDesc(member.getId());

        // 가장 최근의 채팅이 존재하고, 그 채팅의 생성일이 오늘인 경우 해당 채팅 반환
//        if (latestChatOpt.isPresent()) {
//            Chat latestChat = latestChatOpt.get();
//            if (latestChat.getCreatedDate().toLocalDate().isEqual(LocalDate.now())) {
//                return new InitMessageDTO(latestChat.getId(), "오늘의 채팅이 이미 존재합니다.", MessageType.SYSTEM);
//            }
//        }

        // 최근 채팅이 없거나 가장 최근의 채팅이 오늘이 아닌 경우 새로운 채팅 생성
        Diary diary = new Diary();
        diary.setMember(member);
        diaryRepository.save(diary);

        Chat newChat = new Chat();
        newChat.setMember(member);
        newChat.setCreatedDate(LocalDateTime.now());
        newChat.setCompleted(false); // 초기 값 설정
        newChat.setDiary(diary);
        chatRepository.save(newChat);

        diary.setChat(newChat);
        diaryRepository.save(diary);

        String greetingMessage = randomGreeting();
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setChat(newChat);
        chatMessages.setContent(greetingMessage);
        chatMessages.setType(MessageType.ASSISTANT);
        chatMessagesRepository.save(chatMessages);


        return new InitMessageDTO(newChat.getId(), greetingMessage, MessageType.ASSISTANT);
    }

    @Transactional
    public List<ChatTotalDTO> getTotalChat(Long chatId) {
        List<ChatTotalDTO> chatTotalDTO = chatMessagesRepository.findAllByChatId(chatId).stream()
                .map(chatMessages -> new ChatTotalDTO(
                        chatMessages.getId(),
                        chatMessages.getContent(),
                        chatMessages.getType(),
                        chatMessages.getSentTime()
                ))
                .toList();

        if(chatTotalDTO.isEmpty()) return null;
        return chatTotalDTO;
    }

    public Optional<Chat> getChatById(Long chatId) {
        return chatRepository.findById(chatId);
    }
}
