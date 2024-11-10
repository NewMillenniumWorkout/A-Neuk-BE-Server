package com.example.aneukbeserver.domain.chatMessages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Long> {
}
