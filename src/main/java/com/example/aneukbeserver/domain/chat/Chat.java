package com.example.aneukbeserver.domain.chat;

import com.example.aneukbeserver.domain.CreatedTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat extends CreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long diary_id;

    @Column
    private Long member_id;

    @Column
    private boolean isCompleted;

    @CreatedDate
    private LocalDateTime createdDate;

}
