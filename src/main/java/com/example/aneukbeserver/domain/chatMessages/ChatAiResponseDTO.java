package com.example.aneukbeserver.domain.chatMessages;

import com.example.aneukbeserver.domain.chat.Chat;
import lombok.Data;

@Data
public class ChatAiResponseDTO {
    private Long chat_id;
    private MessageType role;
    private String message;

}
