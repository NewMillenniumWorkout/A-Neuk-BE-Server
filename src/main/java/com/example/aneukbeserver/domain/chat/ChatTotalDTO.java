package com.example.aneukbeserver.domain.chat;

import com.example.aneukbeserver.domain.chatMessages.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatTotalDTO {
    private Long id;
    private String content;
    private MessageType type;
    private LocalDateTime sentTime;
}


