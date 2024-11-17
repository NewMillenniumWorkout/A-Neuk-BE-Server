package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.chatMessages.ChatAiResponseDTO;
import com.example.aneukbeserver.domain.chatMessages.ChatMessages;
import com.example.aneukbeserver.domain.chatMessages.ChatMessagesRepository;
import com.example.aneukbeserver.domain.chatMessages.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatMessagesService {
    @Autowired
    private ChatMessagesRepository chatMessagesRepository;
    public List<ChatMessages> getChatMessages(Long chatId) {
        return chatMessagesRepository.findAllByChatId(chatId);

    }

    @Transactional
    public void saveUserChat(Chat chat, String content) {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setChat(chat);
        chatMessages.setContent(content);
        chatMessages.setType(MessageType.MEMBER);

        chatMessagesRepository.save(chatMessages);

    }

    public void saveAssistantChat(Chat chat, ChatAiResponseDTO body) {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setChat(chat);
        chatMessages.setContent(body.getMessage());
        chatMessages.setType(MessageType.ASSISTANT);
        chatMessagesRepository.save(chatMessages);
    }
}
