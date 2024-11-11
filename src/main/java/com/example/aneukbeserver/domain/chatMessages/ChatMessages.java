package com.example.aneukbeserver.domain.chatMessages;

import com.example.aneukbeserver.domain.chat.Chat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column
    @CreatedDate
    private LocalDateTime sentTime;
}
