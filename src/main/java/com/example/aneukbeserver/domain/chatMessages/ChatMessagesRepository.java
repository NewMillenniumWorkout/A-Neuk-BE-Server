package com.example.aneukbeserver.domain.chatMessages;

import com.example.aneukbeserver.domain.chat.ChatTotalDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Long> {
    List<ChatMessages> findAllByChatId(Long chatId);
}
