package com.example.aneukbeserver.domain.chatMessages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitMessageDTO {
    private Long chatId;
    private String message;
}
