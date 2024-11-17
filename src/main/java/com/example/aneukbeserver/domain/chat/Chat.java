package com.example.aneukbeserver.domain.chat;

import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "diary_id", nullable = false, unique = true)
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column
    private boolean isCompleted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

}
